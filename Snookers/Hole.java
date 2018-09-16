class Hole {

  float xLocation; // x-center of the hole
  int yLocation; // y-center of the hole
  int diameter; // diameter of the hole

  public Hole(float x, int y) {
    /* instantiates the hole variables */
    this.xLocation = x;
    this.yLocation = y;
    this.diameter = 80;
  }

  void show() {
    /* show the hole */
    fill(50);
    ellipse(this.xLocation, this.yLocation, this.diameter, this.diameter);
  }
}
