/* Snooker Game
   Copyright ®2018 by Yanqing Huang

 Instructions: Drag and aim on the white cue ball to knock the billiards into the holes.
 You have to pot a red ball, then a colored ball, then a red ball, etc.

 Red: 1 point
 Yellow: 2 points
 Green: 3 points
 Brown: 4 points
 Blue: 5 points
 Pink: 6 points
 Black: 7 points

 Be careful, if you pot in the wrong order or get the white ball netted, you lose 4 points!
 However, you can take your time, and there will be no one playing against you.

 Get above 100 points to win!

 Can you get the perfect 147?

 Enjoy!
 */

// initialization of the holes array
Hole[] holes;
int numHoles = 6;

// the arraylist that are going to hold the red and colored balls
ArrayList<SnookerBall> balls = new ArrayList<SnookerBall>();

int points = 0; // total points the player has
int prevBallPoint = 0; // the point of the previously potted ball
int numRedBallsLeft = 15;

CueBall cueBall; // initialization of the cueBall;

float radius; // the radius of all the balls

color bgColor = color(11, 113, 45); // background color

// the colors for each individual billiard ball
color redBall = color(180, 0, 0);
color yellowBall = color(231, 195, 27);
color greenBall = color(14, 125, 63);
color brownBall = color(110, 64, 25);
color blueBall = color(19, 44, 170);
color pinkBall = color(255, 192, 203);
color blackBall = color(0);

// the x and y values of the colored balls
float yellowBall_x;
float yellowBall_y;
float greenBall_x;
float greenBall_y;
float brownBall_x;
float brownBall_y;
float blueBall_x;
float blueBall_y;
float pinkBall_x;
float pinkBall_y;
float blackBall_x;
float blackBall_y;
float redBalls_x;
float redBalls_y;


void setup() {
  /* initializes the canvas, the holes, and the billiard balls */

  // canvas settings
  size(1200, 600);
  background(bgColor);

  radius = 0.013 * width; // radius of the balls

  yellowBall_x = width/5;
  yellowBall_y = 5*height/6;
  greenBall_x = width/5;
  greenBall_y = height/6;
  brownBall_x = (float) width/5;
  brownBall_y = (float) height/2;
  blueBall_x = (float) width/2;
  blueBall_y = (float) height/2;
  pinkBall_x = (float) 4*width/5 - 10*radius;
  pinkBall_y = (float) height/2;
  blackBall_x = (float) 4*width/5 + 10*radius;
  blackBall_y = (float) height/2;
  redBalls_x = (float) 4*width/5;
  redBalls_y = (float) height/2;

  // creates the holes using a for loop
  holes = new Hole[numHoles];
  for (int i = 0; i < numHoles; i++) {
    if (i < 3) holes[i] = new Hole((i / 2.0) * width, 0);
    else holes[i] = new Hole(((i - 3) / 2.0) * width, height);
  }

  // creates a cueBall at a specific location
  cueBall = new CueBall(width/8, 4*height/7);

  // creates the colored balls at their respective locations
  balls.add(new SnookerBall(yellowBall_x, yellowBall_y, 2));
  balls.add(new SnookerBall(greenBall_x, greenBall_y, 3));
  balls.add(new SnookerBall(brownBall_x, brownBall_y, 4));
  balls.add(new SnookerBall(blueBall_x, blueBall_y, 5));
  balls.add(new SnookerBall(pinkBall_x, pinkBall_y, 6));
  balls.add(new SnookerBall(blackBall_x, blackBall_y, 7));

  // creates the triangle of red balls (using mathematics)
  balls.add(new SnookerBall(redBalls_x - 0.1015*width, redBalls_y, 1));
  balls.add(new SnookerBall(redBalls_x - 0.078*width, redBalls_y - 16, 1));
  balls.add(new SnookerBall(redBalls_x - 0.078*width, redBalls_y + 16, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0545*width, redBalls_y, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0545*width, redBalls_y - 32, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0545*width, redBalls_y + 32, 1));
  balls.add(new SnookerBall(redBalls_x - 0.031*width, redBalls_y - 16, 1));
  balls.add(new SnookerBall(redBalls_x - 0.031*width, redBalls_y + 16, 1));
  balls.add(new SnookerBall(redBalls_x - 0.031*width, redBalls_y - 48, 1));
  balls.add(new SnookerBall(redBalls_x - 0.031*width, redBalls_y + 48, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0075*width, redBalls_y, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0075*width, redBalls_y + 32, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0075*width, redBalls_y - 32, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0075*width, redBalls_y + 64, 1));
  balls.add(new SnookerBall(redBalls_x - 0.0075*width, redBalls_y - 64, 1));
}

void draw() {
  /* repeats */

  // clear the background
  background(bgColor);

  // draws the semicircle and line at the left of the table
  stroke(255);
  line(width/5, 0, width/5, height);
  noFill();
  arc(width/5, height/2, 3*height/9, 3*height/9, HALF_PI, PI + HALF_PI);
  stroke(0);

  // shows the current score of the player
  fill(255);
  textAlign(LEFT, TOP);
  textSize(30);
  text("Score: " + points, 50, 20);

  // shows the holes
  for (Hole h : holes) {
    h.show();
  }

  // game-termination condition
  if (balls.size() == 0){

    // show "win" screen if points is over 100
    if (points >= 100){
      background(0);
      textSize(width/10);
      fill(255);
      textAlign(CENTER, CENTER);
      text("You Win", width/2, height/2 - width/10);
      text("Score: " + points, width/2, height/2);
    } else {
    // show "lose" screen if points is less than 100
      textSize(width/10);
      fill(255);
      textAlign(CENTER, CENTER);
      text("Game Over!", width/2, height/2 - width/10);
      text("Score: " + points, width/2, height/2);
    }
  }
  // loops through all the colored balls to see if any is potted
  for (int i = 0; i < balls.size(); i++) {
    // checks to see if a ball is potted
    if (balls.get(i).isInHole()) {
      boolean isColoredBall = false; // whether we need to put the ball back or not
      SnookerBall tempBall = balls.get(i); // temporary ball to avoid changing the balls
      if (numRedBallsLeft > 0){
        // check to see if the ball is the first potted ball
        if (prevBallPoint == 0) {
          // check to see if ball is red
          if (tempBall.point == 1) {
            points += 1;
            prevBallPoint = 1;
            balls.remove(i);
            numRedBallsLeft--;
          } else {
            points -= 4;
            balls.remove(i);
            isColoredBall = true;
          }
        }
        // check to see if the next ball is of different color and is colored
        else if (prevBallPoint == 1 && tempBall.point > 1) {
          points += tempBall.point;
          prevBallPoint = tempBall.point;
          balls.remove(i);
          isColoredBall = true; // we need to add the second ball back onto the table after it has been potted
        }
        // check to see if the next ball is of different color and is red
        else if (prevBallPoint > 1 && tempBall.point == 1) {
          points += tempBall.point;
          prevBallPoint = tempBall.point;
          balls.remove(i);
          numRedBallsLeft--;
          // we don't need to add the second ball since it is red
        }
        // check to see if a foul is committed
        else if (prevBallPoint == tempBall.point) {
          points -= 4;
          balls.remove(i);
          isColoredBall = true; // return the balls back to the table no matter what color
        }

        // return the colored balls back to the table
        if (isColoredBall) {
          // add a new ball at its starting location if it is colored and potted
          switch(tempBall.point) {
          case 2:
            balls.add(new SnookerBall(yellowBall_x, yellowBall_y, 2));
            break;
          case 3:
            balls.add(new SnookerBall(greenBall_x, greenBall_y, 3));
            break;
          case 4:
            balls.add(new SnookerBall(brownBall_x, brownBall_y, 4));
            break;
          case 5:
            balls.add(new SnookerBall(blueBall_x, blueBall_y, 5));
            break;
          case 6:
            balls.add(new SnookerBall(pinkBall_x, pinkBall_y, 6));
            break;
          case 7:
            balls.add(new SnookerBall(blackBall_x, blackBall_y, 7));
            break;
          default:
            balls.add(new SnookerBall(redBalls_x, redBalls_y, 1));
            break;
          }
        }
      } else {
        points += tempBall.point;
        balls.remove(i);
      }
    }
  }

  // if the cue ball is potted, remove 4 points and return to starting location
  if (cueBall.isInHole()) {
    points -= 4;
    cueBall = new CueBall(width/8, 4*height/7);
  }

  // update and shows all the balls
  for (int i = 0; i < balls.size(); i++) balls.get(i).show();
  cueBall.show();
}

// calls the checkPressed() function in cueBall when mouse is pressed (starts dragging)
void mousePressed() {
  cueBall.checkPressed();
}

// calls the checkReleased() function in cueBall when mouse stops pressing (stops dragging)
void mouseReleased() {
  cueBall.checkReleased();
}
