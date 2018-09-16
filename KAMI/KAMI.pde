/* KAMI simulation by Joey Huang
 July 2018
*/

Board board; // the board of triangles, contains all the TriPaper objects
SideBar sideBar; // the side bar that contains all the selectable colors

color[] colors = {color(120, 10, 180), color(13, 255, 15), color(15, 13, 255), color(180, 40, 25)}; // the colors selectable in the side bar
color selectedColor = color(180, 40, 25); // initial selected color
color oldColor; // the old color of the selected triangle

TriPaper selTri; // the selected triangle that will move through all its neighbors searching for triangles of the same color
TriPaper nextTri; // the next triangle in the search
TriPaper mouseTri; // the triangle the mouse clicked on
TriPaper fromTri; // the triangle that will flip to one of its neighbors
TriPaper toTri; // the triangle that will be flipped onto

ArrayList<TriPaper[]> recSearchArr; // the array that contains all the triangles being searched through
ArrayList<TriPaper[]> flipArr; //  the array that contains all the triangle that are flipping

boolean gameOn = false; // whether the triangles are flipping

float flipSpeed = 0.001; // the speed of the triangle flip
float triFlipState = PI; // used to control the flips. Go on to the next triangles in the flipArr array when triFlipState reaches PI

int numRows = 18; // the number of triangle rows
int numCols = 18; // the number of triangle columns
int sideBarWidth = 100; // the width of the side bar

void setup(){
  
  size(1222, 612, P3D); // the size of the board
  frameRate(1000); // quicken the frame rate
  lights();
  
  board = new Board(numRows, numCols); // create a new board with specified size
  sideBar = new SideBar(colors, (int) height/2, width - sideBarWidth); // create a new side bar with specified size
  recSearchArr = new ArrayList<TriPaper[]>(); // create the recSearch array
  flipArr = new ArrayList<TriPaper[]>(); // create the flip array

}  

void draw(){
  
  background(255); // draws a white background
  sideBar.show(); // shows the side bar
  board.show(); // shows the board of triangles
  
  // if a triangle has been added to the flip array and other triangles are not flipping
  if(flipArr.size() > 0 && !gameOn){
    
    int flipDir; // the direction of the triangle flip
    
    /*
      For triangles pointing left:
      0 /|  1
      2 \|  1
      
      For triangle pointing right:
      0 |\  1
      0 |/  2
    */
    
    // if the triangle has finished flipping
    if(triFlipState >= PI){
      
      // if the triangle has flipped onto the target triangle,  set the color of the target triangle to the selected color
      if(toTri != null) toTri.triColor = selectedColor; 
      
      TriPaper[] tempArray = flipArr.remove(0); // remove the head of the flip array to find the next two triangles to flip
      fromTri = tempArray[0]; // the first element of each flipArr element will the the triangle which starts to flip
      toTri = tempArray[1]; // the second element of each flipArr element will the the triangle is flipped onto
      triFlipState = 0; // restart the flipping process
      
    } else {

      if(fromTri == null && toTri != null) fromTri = mouseTri; // if the triangle is the one being clicked on
      
      flipDir = detFlipDir(fromTri, toTri); // determine the direction of the flip
      fromTri.flip(selectedColor, flipDir); // flip the first triangle in the direction of the flip
      fromTri.theta += flipSpeed * PI; // rotate the flipping animation
      triFlipState += flipSpeed * PI; // gets to PI and resets the process when the flipping triangle finishes flipping
      
    }
  }
}

void runThroughBoard(){
  /* goes through a large area of triangles and sort them in order */
 
  while(recSearchArr.size() > 0 && selTri != null){
    findNextItems(selTri);
    TriPaper[] ta = recSearchArr.remove(0);
    flipArr.add(ta);
    nextTri = ta[1];
    if(nextTri != null){
      selTri = nextTri;
    }
  }
  
  for(int i = 0; i < 18; i++){
    for(int j = 0; j < 18; j++){
      board.pieces[i][j].visited = false;
    }
  }
  gameOn = false;
}

void mousePressed(){
  if(!gameOn){
    selectedColor = sideBar.update(selectedColor);
  }
  for(int i = 0; i < board.numRow; i++){
    for(int j = 0; j < board.numCol; j++){
      if(board.pieces[i][j].checkInside()){
        gameOn = true;
        oldColor = board.pieces[i][j].triColor;
        board.pieces[i][j].triColor = selectedColor;
        if(oldColor != selectedColor){
          selTri = board.pieces[i][j];
          TriPaper[] tArray = {null, selTri};
          mouseTri = selTri;
          selTri.visited = true;
          recSearchArr.add(tArray);
        }
      }
    }
  }
  
  runThroughBoard();
}

void findNextItems(TriPaper triangle){
  int row = (int) (triangle.row * 2) + 1;
  int col = triangle.col;
  color tCol = oldColor;
  if(triangle.pointsLeft){
    if(row > 0) if(board.pieces[row - 1][col].triColor == tCol && !board.pieces[row - 1][col].visited){
      board.pieces[row - 1][col].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row - 1][col]};
      recSearchArr.add(tArray);
    }
    if(row < 17) if(board.pieces[row + 1][col].triColor == tCol && !board.pieces[row + 1][col].visited){
      board.pieces[row + 1][col].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row + 1][col]};
      recSearchArr.add(tArray);
    }
    if(col < 17) if(board.pieces[row][col + 1].triColor == tCol && !board.pieces[row][col + 1].visited){
      board.pieces[row][col + 1].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row][col + 1]};
      recSearchArr.add(tArray);
    }
  } else {
    if(row > 0) if(board.pieces[row - 1][col].triColor == tCol && !board.pieces[row - 1][col].visited){
      board.pieces[row - 1][col].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row - 1][col]};
      recSearchArr.add(tArray);
    }
    if(row < 17) if(board.pieces[row + 1][col].triColor == tCol && !board.pieces[row + 1][col].visited){
      board.pieces[row + 1][col].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row + 1][col]};
      recSearchArr.add(tArray);
    }
    if(col > 0 ) if(board.pieces[row][col - 1].triColor == tCol && !board.pieces[row][col - 1].visited){
      board.pieces[row][col - 1].visited = true;
      TriPaper[] tArray = {triangle, board.pieces[row][col - 1]};
      recSearchArr.add(tArray);
    }
  }
}

int detFlipDir(TriPaper tri1, TriPaper tri2){
  if(tri1.pointsLeft){
    if(tri1.triPoints[2] == tri2.triPoints[4] && tri1.triPoints[3] == tri2.triPoints[5] &&
       tri1.triPoints[4] == tri2.triPoints[2] && tri1.triPoints[5] == tri2.triPoints[3]) return 0;
    else if(tri1.triPoints[4] == tri2.triPoints[0] && tri1.triPoints[5] == tri2.triPoints[1] &&
            tri1.triPoints[0] == tri2.triPoints[4] && tri1.triPoints[1] == tri2.triPoints[5]) return 1;
    else if(tri1.triPoints[2] == tri2.triPoints[0] && tri1.triPoints[3] == tri2.triPoints[1] &&
            tri1.triPoints[0] == tri2.triPoints[2] && tri1.triPoints[1] == tri2.triPoints[3]) return 2;
    else return -1;
  } 
  else {
    if(tri1.triPoints[0] == tri2.triPoints[4] && tri1.triPoints[1] == tri2.triPoints[5] &&
       tri1.triPoints[4] == tri2.triPoints[0] && tri1.triPoints[5] == tri2.triPoints[1]) return 0;
    else if(tri1.triPoints[0] == tri2.triPoints[2] && tri1.triPoints[1] == tri2.triPoints[3] &&
            tri1.triPoints[2] == tri2.triPoints[0] && tri1.triPoints[3] == tri2.triPoints[1]) return 1;
    else if(tri1.triPoints[2] == tri2.triPoints[4] && tri1.triPoints[3] == tri2.triPoints[5] &&
            tri1.triPoints[4] == tri2.triPoints[2] && tri1.triPoints[5] == tri2.triPoints[3]) return 2;
    else return -1;
  }
}
