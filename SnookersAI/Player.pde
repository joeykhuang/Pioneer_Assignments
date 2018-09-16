class Player {
  Genotype DNA;//the behaviour of the player
  int upToShot = 0; //which point in the shots array the player is up to 
  float fitness; // the quality of the player used for natural selection
  Ball whiteBall;
  Ball[] balls;  
  boolean gameOver = false;
  boolean won = false; //whether the player has sunk all the balls with the black ball last
  Box2DProcessing World;//the box2d world that the player is playing in
  
  int score = 0;
  int prevBallPoint = 0;
  int numRedBallsLeft = 15;
  boolean isColoredBall;
  
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
  
  float radius = 15;
    
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //constructor
  Player(Box2DProcessing world) { 
    World = world;
    DNA = new Genotype();
    fitness = 0;
    
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
    
    whiteBall = new Ball(width/8, 4*height/7, world, 0);//set white ball
    
    //create all 15 balls
    balls = new Ball[21]; 
    //first row
    
    balls[0] = new Ball(yellowBall_x, yellowBall_y, world, 2);
    balls[1] = new Ball(greenBall_x, greenBall_y, world, 3);
    balls[2] = new Ball(brownBall_x, brownBall_y, world, 4);
    balls[3] = new Ball(blueBall_x, blueBall_y, world, 5);
    balls[4] = new Ball(pinkBall_x, pinkBall_y, world, 6);
    balls[5] = new Ball(blackBall_x, blackBall_y, world, 7);
  
    // creates the triangle of red balls (using mathematics)
    balls[6] = new Ball(redBalls_x - 0.1015*width, redBalls_y, world, 1);
    balls[7] = new Ball(redBalls_x - 0.078*width, redBalls_y - 16, world, 1);
    balls[8] = new Ball(redBalls_x - 0.078*width, redBalls_y + 16, world, 1);
    balls[9] = new Ball(redBalls_x - 0.0545*width, redBalls_y, world, 1);
    balls[10] = new Ball(redBalls_x - 0.0545*width, redBalls_y - 32, world, 1);
    balls[11] = new Ball(redBalls_x - 0.0545*width, redBalls_y + 32, world, 1);
    balls[12] = new Ball(redBalls_x - 0.031*width, redBalls_y - 16, world, 1);
    balls[13] = new Ball(redBalls_x - 0.031*width, redBalls_y + 16, world, 1);
    balls[14] = new Ball(redBalls_x - 0.031*width, redBalls_y - 48, world, 1);
    balls[15] = new Ball(redBalls_x - 0.031*width, redBalls_y + 48, world, 1);
    balls[16] = new Ball(redBalls_x - 0.0075*width, redBalls_y, world, 1);
    balls[17] = new Ball(redBalls_x - 0.0075*width, redBalls_y + 32, world, 1);
    balls[18] = new Ball(redBalls_x - 0.0075*width, redBalls_y - 32, world, 1);
    balls[19] = new Ball(redBalls_x - 0.0075*width, redBalls_y + 64, world, 1);
    balls[20] = new Ball(redBalls_x - 0.0075*width, redBalls_y - 64, world, 1);
  }


  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------  
  //called every step to update the balls
  void update() {
    //apply friction to the balls
    for (int i =0; i<balls.length; i++) {
      balls[i].update() ;
    }
    whiteBall.update();
    for (Ball b: balls){
      for (int i = 0; i < 6; i++) {
        Vec2 pos = b.world.getBodyPixelCoord(b.body);
        if (dist(pos.x, pos.y, tables[0].holes[i].pos.x, tables[0].holes[i].pos.y) < 20) {
          calculateScore(b);
        }
      }
    }
    
    
    //check if able to shoot
    if (!gameOver && ballsStopped()) {
      shoot();
    } 
    
    //check if white ball is sunk
    if (whiteBall.isInHole()) {
      gameOver = true;
    }
    
    //check if the player has won
    if (gameFinished()) {
      gameOver = true;
    }
  }
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //apply a force to the white ball in the direction of the next vector in the DNA
  void shoot() {
    if (upToShot >= DNA.shots.length || gameOver) {//if all shots are done
      gameOver = true;//dont shoot
      return;
    }
    
    //apply the force
    whiteBall.applyForce(DNA.shots[upToShot]);
    upToShot +=1;
  }
  
  void calculateScore(Ball b){
    if (numRedBallsLeft > 0){
        // check to see if the ball is the first potted ball
        if (prevBallPoint == 0) { 
          // check to see if ball is red
          if (b.point == 1) {
            score += 1; 
            prevBallPoint = 1;
            b.display = false;
            numRedBallsLeft--;
          } else {
            score -= 4;
            b.display = false;
            isColoredBall = true;
          }
        } 
        // check to see if the next ball is of different color and is colored
        else if (prevBallPoint == 1 && b.point > 1) {
          score += b.point;
          prevBallPoint = b.point;
          b.display = false;
          isColoredBall = true; // we need to add the second ball back onto the table after it has been potted
        } 
        // check to see if the next ball is of different color and is red
        else if (prevBallPoint > 1 && b.point == 1) {
          score += b.point;
          prevBallPoint = b.point;
          b.display = false;
          numRedBallsLeft--;
          // we don't need to add the second ball since it is red
        } 
        // check to see if a foul is committed
        else if (prevBallPoint == b.point) {
          score -= 4;
          b.display = false;
          isColoredBall = true; // return the balls back to the table no matter what color
        }
       
         if (isColoredBall) {
          // add a new ball at its starting location if it is colored and potted
          switch(b.point) {
            case 2: 
              balls[b.point - 2] = new Ball(yellowBall_x, yellowBall_y, this.World, 2);
              break;
            case 3: 
              balls[b.point - 2] = new Ball(greenBall_x, greenBall_y, this.World, 3);
              break;
            case 4: 
              balls[b.point - 2] = new Ball(brownBall_x, brownBall_y, this.World, 4);
              break;
            case 5: 
              balls[b.point - 2] = new Ball(blueBall_x, blueBall_y, this.World, 5);
              break;
            case 6: 
              balls[b.point - 2] = new Ball(pinkBall_x, pinkBall_y, this.World, 6);
              break;
            case 7: 
              balls[b.point - 2] = new Ball(blackBall_x, blackBall_y, this.World, 7);
              break;
          }
        }
      }
  }
  
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //draw all the balls
  void show() {
    whiteBall.show();
    for (int i =0; i<balls.length; i++) {
      balls[i].show() ;
    }
  }
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //sets the fitness based on where the balls ended up
  void calculateFitness(int ballsSunkPreviously) { //ballsSunkPreviously is the number of balls sunk before this shot

    fitness = -1000;
    if (whiteBall.sunk) {
      return;//if white ball sunk then finish with the fitness = 0
    }
    float totalDistance = 0;//the sum of all the distances of the balls
    int ballsSunk = 0;//number of balls in pockets
    for (int i = 0; i < balls.length; i++) {
      if (!balls[i].sunk) { //if the ball isnt sunk calculate the distance to the closest hole
        float min = 1000;
        Vec2 ballPos = balls[i].world.getBodyPixelCoord(balls[i].body);
        for (int j= 0; j<6; j++) {
          if (dist(tables[0].holes[j].pos.x, tables[0].holes[j].pos.y, ballPos.x, ballPos.y) < min) {
            min = dist(tables[0].holes[j].pos.x, tables[0].holes[j].pos.y, ballPos.x, ballPos.y);
          }
        }
        totalDistance += min;//add the smallest distance to a whole to the total
      } else {//if the ball is sunk
        ballsSunk++;
        fitness += 1000;
      }
    }

    if (totalDistance == 0) { //if all balls sunk
      fitness = 5000;
    } else {
      fitness = 100 * (score + 1) * ((1 +(ballsSunk - ballsSunkPreviously))*(1+(ballsSunk - ballsSunkPreviously)))/(totalDistance);//fitness function
    }
  }


  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //returns a clone with the same DNA as this player
  Player clone(Box2DProcessing world) {
    Player clone = new Player(world);
    clone.DNA = DNA.clone();
    return clone;
  }
  //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //true if all the balls are stopped
  boolean ballsStopped() {
    if (!whiteBall.isStopped()) {
      return false;
    }
    for (int i =0; i<balls.length; i++) {
      if (!balls[i].sunk && !balls[i].isStopped()) {
        return false;
      }
    }
    return true;
  }
  //-----------------------------------------------------------------------------------------------------------------------------------------------
  //true if the game is won
  boolean gameFinished() {
    for (int i =0; i<balls.length; i++) {
      if (!balls[i].isInHole()) {//if any of the balls are not sunk then return false
        return false;
      }
    }
    //if non of the balls are not in a hole
    won = true;
 
    return true;
  }

  //-----------------------------------------------------------------------------------------------------------------------------------------------
  //returns the number of balls sunk
  int ballsSunk() {
    int ballsSunk = 0;
    for (int i =0; i<balls.length; i++) {
      if (balls[i].sunk) {
        ballsSunk+=1;
      }
    }

    return ballsSunk;
  }
}
