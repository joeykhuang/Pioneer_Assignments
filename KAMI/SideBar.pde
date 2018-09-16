class SideBar{
  color[] colors;
  float rectHeight;
  int startLeft;
  int ht;
  int prevColor = -1;
  boolean[] isClickedOn;
  SideBar(color[] colors, int ht, int startLeft){
    this.colors = colors;
    this.rectHeight = ht/(colors.length);
    this.startLeft = startLeft;
    this.ht = ht;
    this.isClickedOn = new boolean[colors.length];
    for(int i = 0; i < colors.length; i++) this.isClickedOn[i] = false;
  }
  
  void show(){
    
    fill(0);
    rect(width - 100, 0, 100, height);
    
    for(int i = 0; i < colors.length; i++){
      noStroke();
      fill(colors[i]);
      rect(this.startLeft, this.ht + i*this.rectHeight, 100, this.rectHeight);
      if(this.isClickedOn[i]){
        fill(255);
        rect(this.startLeft + 90, this.ht + i*this.rectHeight, 10, this.rectHeight);
      }
    }
  }
  
  color update(color currentColor){
    if(mouseX > this.startLeft && mouseY > this.ht && mouseY < this.ht * 2){
      int whichColor = (int) ((mouseY - this.ht)/(this.rectHeight));
      if(prevColor >= 0) this.isClickedOn[prevColor] = false;
      this.isClickedOn[whichColor] = true;
      prevColor = whichColor;
      return colors[whichColor];
    } else {
      return currentColor;
    }
  }
}
