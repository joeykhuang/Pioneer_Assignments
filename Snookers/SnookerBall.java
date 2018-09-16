// the Snooker Ball class
class SnookerBall {

  // use PVectors for the ball's position and velocity
  PVector position;
  PVector velocity;

  color ballColor; // the color (point value) of the ball
  int point; // the point value of the colored ball

  float friction; // friction of the ball

  public SnookerBall(float x, float y, int point) {

    // initialize the variables
    this.position = new PVector(x, y);
    this.velocity = new PVector(0, 0);
    this.point = point;
    this.friction = 0.97;

    // show different colors for different point values of the ball
    switch (point) {
    case 1:
      this.ballColor = color(redBall);
      break;
    case 2:
      this.ballColor = color(yellowBall);
      break;
    case 3:
      this.ballColor = color(greenBall);
      break;
    case 4:
      this.ballColor = color(brownBall);
      break;
    case 5:
      this.ballColor = color(blueBall);
      break;
    case 6:
      this.ballColor = color(pinkBall);
      break;
    case 7:
      this.ballColor = color(blackBall);
      break;
    }

    // if another ball is in the place, repel the other ball so that no balls overlap
    this.repel();
  }

  void show() {
    /* draw and updates the ball */
    this.checkBoundaryCollision();
    this.checkCollision();
    this.update();

    // draws an ellipse at the x and y location, and draws a semi-transparent highlight
    fill(ballColor);
    ellipse(this.position.x, this.position.y, radius*2, radius*2);
    noStroke();
    fill(255, 180);
    ellipse(this.position.x + radius*0.1, this.position.y - radius*0.4, radius*0.5, radius*0.5);
    stroke(0);
  }


  void update() {
    /* updates the position by adding the velocity vector and updates the velocity by the friction */

    this.position.add(this.velocity);
    this.velocity.x *= this.friction;
    this.velocity.y *= this.friction;
  }

  void repel() {
    /* repels another ball if they land on top of each other */

    for (int i = 0; i < balls.size(); i++) {
      SnookerBall tempBall = balls.get(i);

      if (tempBall != this) {
        float distance = dist(this.position.x, this.position.y,
          tempBall.position.x, tempBall.position.y);

        if (distance <= (radius + radius)) {
          PVector direction = new PVector(this.position.x - tempBall.position.x,
            this.position.y - tempBall.position.y);

          direction.normalize();

          this.position.x += direction.x * distance * 1.1;
          this.position.y += direction.y * distance * 1.1;
        }
      }
    }
  }

  // checks to see if the ball collides with the walls (cushions)
  void checkBoundaryCollision() {
    if (this.position.x > width - radius) {
      // right wall
      this.position.x = width - radius;
      this.velocity.x *= -1;
    } else if (this.position.x < radius) {
      // left wall
      this.position.x = radius;
      this.velocity.x *= -1;
    } else if (this.position.y > height - radius) {
      // bottom wall
      this.position.y = height - radius;
      this.velocity.y *= -1;
    } else if (this.position.y < radius) {
      // top wall
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

      if (tempBall != this) {
        float distance = dist(this.position.x, this.position.y,
                              tempBall.position.x, tempBall.position.y);

        if(distance <= 2 * radius){
          tempBall.velocity.x += 0.9*this.velocity.x;
          tempBall.velocity.y += 0.9*this.velocity.y;

          float angle = atan2(tempBall.position.y - this.position.y, tempBall.position.x - this.position.x);
          this.position.x = tempBall.position.x - cos(angle) * (2 * radius);
          this.position.y = tempBall.position.y - sin(angle) * (2 * radius);

          this.velocity.x *= -0.3;
          this.velocity.y *= -0.3;
         }
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
}
