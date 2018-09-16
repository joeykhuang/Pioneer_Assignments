class Board{
  
  TriPaper[][] pieces;
  
  int numCol;
  int numRow;
  
  color pieceColor = color(180, 40, 25);
  Board(int numRows, int numCols){
   this.numCol = numCols;
   this.numRow = numRows;
   pieces = new TriPaper[numRow][numCol];
   for(int i = 0; i < numRow; i++){
     for(int j = 0; j < 2; j++){
       pieces[i][j] = new TriPaper(i/2.0 - 0.5, j, pieceColor); 
     }
     pieces[i][2] = new TriPaper(i/2.0 - 0.5, 2, color(120, 10, 180)); 
     for(int j = 3; j < numCol; j++){
       pieces[i][j] = new TriPaper(i/2.0 - 0.5, j, pieceColor); 
     }
   }
  }
  
  void show(){
    for(int i = 0; i < numRow; i++){
      for(int j = 0; j < numCol; j++){
        pieces[i][j].show();
      }
    }
  }
}
