class TriPaper{
  float row;
  int col;

  boolean visited = false;
  
  float triWidth = 72; 
  float triHeight = 62.323829;
  
  color triColor;
  float[] triPoints;
  
  float theta = 0;
  
  int opacity;
  
  boolean pointsLeft;
  
  TriPaper(float row, int col, color triColor){
    this.row = row;
    this.col = col;
    this.triColor = triColor;
    this.triPoints = getPointsForTriangle(row, col);
    this.opacity = (int) random(210, 255);
  }
  
  boolean isInteger(float num){
    return num == (int) num;
  }
  
  float[] getPointsForTriangle(float row, int col) {
      float x = col * triHeight; // x starts at leftmost point of triangle
      float y = (row + 0.5) * triWidth; // y starts halfway down the width of triangle

      // See if this triangle should be drawn pointing left or right
      boolean wholeRow = isInteger(row); // bool
      pointsLeft = false; // bool
      if (col % 2 == 0) {
        pointsLeft = !wholeRow; // Even column
      } else {
        pointsLeft = wholeRow; // Odd column
      }
      
      // Draw the triangle.
      if (pointsLeft) {
        float[] points = {x + triHeight, y + triWidth/2, x, y, x + triHeight, y - triWidth/2 };
        return points;
      }
      else {
        float[] points = {x, y - triWidth/2, x + triHeight, y, x, y + triWidth/2 };
        return points;
      }
    }
    
    void show(){
      stroke(60);
      fill(this.triColor, this.opacity);
      triangle(this.triPoints[0],this.triPoints[1], this.triPoints[2], this.triPoints[3], this.triPoints[4], this.triPoints[5]);
    }
    
    void flip(color flippedColor, int dir){
      if(this.pointsLeft){
        if(dir == 0 || dir == 1){
          translate(this.triPoints[4], this.triPoints[5]);
          if(dir == 0){
            rotate(PI/3);
            scale(-1, 1);
            rotateY(this.theta);
            constrain(this.theta, 0, PI);
            fill(flippedColor);
            triangle(0, 0, this.triPoints[2] - this.triPoints[4], this.triPoints[3] - this.triPoints[5], 
                           this.triPoints[0] - this.triPoints[4], this.triPoints[1] - this.triPoints[5]);
          } else {
            rotateY(this.theta);
            constrain(this.theta, 0, PI);
            fill(flippedColor);
            triangle(0, 0, this.triPoints[2] - this.triPoints[4], this.triPoints[3] - this.triPoints[5], 
                           this.triPoints[0] - this.triPoints[4], this.triPoints[1] - this.triPoints[5]);
          }
          
        } else if(dir == 2){
          translate(this.triPoints[0], this.triPoints[1]);
          rotate(-PI/3);
          scale(-1, 1);
          rotateY(this.theta);
          constrain(this.theta, 0, PI);
          fill(flippedColor);
          triangle(0, 0, this.triPoints[2] - this.triPoints[0], this.triPoints[3] - this.triPoints[1], 
                         this.triPoints[4] - this.triPoints[0], this.triPoints[5] - this.triPoints[1]);
        }
      } else{
        if(dir == 0 || dir == 1){
          translate(this.triPoints[0], this.triPoints[1]);
          if(dir == 0){
            scale(-1, 1);
            rotateY(this.theta + PI);
            constrain(this.theta, 0, PI);
            fill(flippedColor);
            triangle(0, 0, this.triPoints[2] - this.triPoints[0], this.triPoints[3] - this.triPoints[1], 
                           this.triPoints[4] - this.triPoints[0], this.triPoints[5] - this.triPoints[1]);
          } else {
            rotate(-PI/3);
            rotateY(this.theta + PI);
            constrain(this.theta, 0, PI);
            fill(flippedColor);
            triangle(0, 0, this.triPoints[2] - this.triPoints[0], this.triPoints[3] - this.triPoints[1], 
                           this.triPoints[4] - this.triPoints[0], this.triPoints[5] - this.triPoints[1]);
          }
          
        } else if(dir == 2){
          translate(this.triPoints[4], this.triPoints[5]);
          rotate(PI/3);
          rotateY(this.theta + PI);
          constrain(this.theta, 0, PI);
          fill(flippedColor);
          triangle(0, 0, this.triPoints[0] - this.triPoints[4], this.triPoints[1] - this.triPoints[5], 
                         this.triPoints[2] - this.triPoints[4], this.triPoints[3] - this.triPoints[5]);
        }
      }
    }
    
    boolean checkInside(){
      float triangleArea = triWidth * triHeight/2;
      
      float tri1 = abs((mouseX * (this.triPoints[1] - this.triPoints[3]) + 
                        this.triPoints[0] * (this.triPoints[3] - mouseY) +
                        this.triPoints[2] * (mouseY - this.triPoints[1]))/2);
                        
      float tri2 = abs((mouseX * (this.triPoints[1] - this.triPoints[5]) + 
                        this.triPoints[0] * (this.triPoints[5] - mouseY) +
                        this.triPoints[4] * (mouseY - this.triPoints[1]))/2);
                        
      float tri3 = abs((mouseX * (this.triPoints[5] - this.triPoints[3]) + 
                        this.triPoints[4] * (this.triPoints[3] - mouseY) +
                        this.triPoints[2] * (mouseY - this.triPoints[5]))/2);
                        
      if (abs(tri1 + tri2 + tri3 - triangleArea) < 0.1) return true;
      else return false;
    }
}
