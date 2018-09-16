class Shield{
	public int x;
	public int y;

	int rows = 5;
	int cols = 10;
	int segmentSize = 5;
	ArrayList<ArrayList<ShieldSegement>> segments = new ArrayList<ArrayList<ShieldSegment>>();

	public Shield(int posx, int posy) {
		this.x = posx;
		this.y = posy;
	}
	void init(){
			for(int r = 0; r < this.rows; r++) {
				segments.add(new ArrayList<ShieldSegment>());
				for(int c = 0; c < this.cols; c++) {
						this.segments.get(r).add(new ShieldSegment(c*this.segmentSize, r*this.segmentSize, this.segmentSize));
				}
			}
	}

	void render(){
			pushMatrix();
			translate(this.x-(this.cols*this.segmentSize)/2,
								this.y - (this.rows*this.segmentSize)/2);

			for(int r = 0; r < this.rows; r++) {
					for(int c = 0; c < this.cols; c++) {
							this.segments.get(r).get(c).render();
					}
			}

			popMatrix();
	}

	void update(ArrayList<Bullet> bullets) {
			for(int r = 0; r < this.rows; r++) {
					for(int c = 0; c < this.cols; c++) {
							this.segments.get(r).get(c).checkHit(bullets, this.x - (this.cols*this.segmentSize)/2,
																									 					this.y - (this.rows*this.segmentSize)/2);
					}
			}
	}
}
