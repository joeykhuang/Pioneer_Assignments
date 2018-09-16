// Cue ball class
class CueBall {

  PVector position;
  PVector velocity;
  boolean isDragging;

  float friction;

  public CueBall(float x, float y) {

    // instantiates the cue ball variables
    this.position = new PVector(x, y);
    this.velocity = new PVector(0, 0);
    this.isDragging = false;
    this.friction = 0.97;
  }


  void show() {
    /* upadte and show the cue ball */
    this.checkBoundaryCollision();
    this.checkCollision();
    this.update();

    // draws the slightly gray cue ball and highlight
    fill(220);
    ellipse(this.position.x, this.position.y, radius*2, radius*2);
    noStroke();
    fill(255, 180);
    ellipse(this.position.x + radius*0.1, this.position.y - radius*0.4, radius*0.5, radius*0.5);
    stroke(0);

    // draw direction and force magnitude halo if the mouse is dragging
    if (isDragging) {

      // draw the halo showing the strength of the force
      stroke(0);
      strokeWeight(1);
      fill(0, 50);
      float haloDiameter = map(dist(this.position.x, this.position.y, mouseX, mouseY), 0, width, 60, 400);
      ellipse(this.position.x, this.position.y, haloDiameter, haloDiameter);

      // draw the direction of the force
      strokeWeight(9);
      stroke(255, 50);
      line(this.position.x, this.position.y, mouseX, mouseY);
      stroke(0);
      strokeWeight(1);
    }
  }

  void update() {
    /* update the velocity and position of the cue ball */
    this.position.add(this.velocity);
    this.velocity.x *= this.friction;
    this.velocity.y *= this.friction;
  }


  void checkBoundaryCollision() {
    /* check whether the cue ball hits the walls (cushions) */

    if (this.position.x > width - radius) { // right wall
      this.position.x = width - radius;
      this.velocity.x *= -1;
    } else if (this.position.x < radius) { // left wall
      this.position.x = radius;
      this.velocity.x *= -1;
    } else if (this.position.y > height - radius) { // bottom wall
      this.position.y = height - radius;
      this.velocity.y *= -1;
    } else if (this.position.y < radius) { // top wall
      this.position.y = radius;
      this.velocity.y *= -1;
    }
  }

  PVector rotate(float x, float y, float sin, float cos, boolean reverse){
    /* rotate a PVector */
    PVector rotatedVector = new PVector(0, 0);
    if(reverse){
      rotatedVector.x = x * cos + y * sin;
      rotatedVector.y = y * cos - x * sin;
    } else {
      rotatedVector.x = x * cos - y * sin;
      rotatedVector.y = y * cos + x * sin;
    }
    return rotatedVector;
  }

  void checkCollision() {
    /* checks to see if two balls collide with each other */
    // loops through all the balls
    for (int i = 0; i < balls.size(); i++) {

      SnookerBall tempBall = balls.get(i); // the other ball

      float dx = tempBall.position.x - this.position.x;
      float dy = tempBall.position.y - this.position.y;
      float distance = dist(position.x, position.y,
                            tempBall.position.x, tempBall.position.y);

      // if the ball collides with the other ball
      if (distance <= (2 * radius)) {

        float angle = atan2(dy, dx); // angle between the cue ball and the billiard ball
        float sin = sin(angle); // sin of the angle
        float cos = cos(angle); // cos of the angle

        // rotated positions of the two balls, with the cue ball as the center of rotation
        PVector thisBallPosition = new PVector(0, 0);
        PVector tempBallPosition = rotate(dx, dy, sin, cos, true);

        // rotated velocities of the two balls
        PVector thisBallVelocity = rotate(this.velocity.x, this.velocity.y, sin, cos, true);
        PVector tempBallVelocity = rotate(tempBall.velocity.x, tempBall.velocity.y, sin, cos, true);

        // using conservation of linear momentum, calculate the final velocities
        float vxTotal = thisBallVelocity.x - tempBallVelocity.x;
        thisBallVelocity.x = ((0.55 - 0.60) * thisBallVelocity.x + 2 * (0.60) * tempBallVelocity.x)/(0.55 + 0.60);
        tempBallVelocity.x = vxTotal + thisBallVelocity.x;

        // avoid clumping
        thisBallPosition.x += thisBallVelocity.x;
        tempBallPosition.x += tempBallVelocity.x;

        // rotate the position back to the original coordinate system
        PVector thisBallRotPosition = rotate(thisBallPosition.x, thisBallPosition.y, sin, cos, false);
        PVector tempBallRotPosition = rotate(tempBallPosition.x, tempBallPosition.y, sin, cos, false);

        // calculate the final position
        tempBall.position.x = this.position.x + tempBallRotPosition.x;
        tempBall.position.y = this.position.y + tempBallRotPosition.y;
        this.position.x = this.position.x + thisBallRotPosition.x;
        this.position.y = this.position.y + thisBallRotPosition.y;

        // rotate the velocity back to the original coordinate system
        PVector thisBallRotVelocity = rotate(thisBallVelocity.x, thisBallVelocity.y, sin, cos, false);
        PVector tempBallRotVelocity = rotate(tempBallVelocity.x, tempBallVelocity.y, sin, cos, false);

        // calculate the final velocities
        this.velocity.x = thisBallRotVelocity.x;
        this.velocity.y = thisBallRotVelocity.y;
        tempBall.velocity.x = tempBallRotVelocity.x;
        tempBall.velocity.y = tempBallRotVelocity.y;
      }
    }
  }

  boolean isInHole() {
    /* if the ball is in the hole, return true */
    if (this.position.x <= radius + 10 && this.position.y <= radius  + 10 || // upper-left
      this.position.x <= radius + 10 && this.position.y >= height - radius - 10 || // lower-left
      this.position.x >= width - radius - 10  && this.position.y <= radius + 10 || // upper-right
      this.position.x >= width - radius - 10 && this.position.y >= height - radius - 10 || // lower-right
      this.position.x <= width / 2 + 15 && this.position.x >= width / 2 - 15 && this.position.y <= radius + 10 || // upper-middle
      this.position.x <= width / 2 + 15 && this.position.x >= width / 2 - 15 && this.position.y >= height - radius - 10) return true; // lower-middle
    return false;
  }

  void checkPressed() {
    /* if the mouse is pressed, start the drag */
    float distance = dist(this.position.x, this.position.y, mouseX, mouseY);
    if (distance < radius) {
      isDragging = true;
    }
  }

  void checkReleased() {
    /* if the mouse is released, stop the drag and calculate the velocity */
    if (isDragging) {
      this.velocity.x = (mouseX - this.position.x)/10;
      this.velocity.y = (mouseY - this.position.y)/10;
    }
    isDragging = false;
  }
}
