class SnookerTable {//object containing all the cushions and the holes
  Cushions cushions;
  Hole[] holes;
  int holeRadius = 22;
//------------------------------------------------------------------------------------------------------------------------------------------------------------------
 //constructor
  SnookerTable(Box2DProcessing box) {
    //add the side cushions
    cushions = new Cushions(box);

    //add the holes
    holes = new  Hole[6];
    holes[0] = new Hole(holeRadius + 10, holeRadius + 10); // top-left
    holes[1] = new Hole(width - holeRadius - 10, holeRadius + 10); // top right
    holes[2] = new Hole(width/2, holeRadius); // top-middle
    holes[3] = new Hole(width/2, height - holeRadius); // top-bottom
    holes[4] = new Hole(holeRadius + 10, height - holeRadius - 10); // bottom-left
    holes[5] = new Hole(width - holeRadius - 10, height - holeRadius - 10); // bottom-right
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------
  //draw the table
  void show() {
    // draws the green table cloth
    fill(34, 139, 34);
    rect(20, 20, width - 20, height - 20);
    
    // draws a yellow boarder
    stroke(255, 215, 0);
    noFill();
    strokeWeight(22);
    rect(11, 11, width - 22, height - 22, 11, 11, 11, 11);
    
    cushions.show();
    
    strokeWeight(1);
    stroke(255);
    line(width/5, 0, width/5, height);
    noFill();
    arc(width/5, height/2, 3*height/9, 3*height/9, HALF_PI, PI + HALF_PI);
    stroke(0);
    
    // draws the wooden borders
    
    fill(74, 33, 6);
    
    rect(40, 0, 661, 23);
    rect(739, 0, 661, 23);
    
    rect(0, 40, 23, 678);
    rect(1418, 40, 22, 678);
    
    rect(40, 736, 661, 22);
    rect(739, 736, 661, 22);
    
    
    
    for (int i = 0; i < holes.length; i++) {
      holes[i].show();
    }
    
  }
}
