class AlienField{
	int x;
	int y;
	int rows;
	int cols;
	PImage sprite;

	int xSpeed = 1;
	int alienSize = 30;
	float alienSpacing = 1.3;
	float speedIncrease = 0.05;
	float alienShootRate = 0.0005;

	float shootRate;

	ArrayList<ArrayList<Alien>> aliens = new ArrayList<ArrayList<Alien>>();

	boolean gameOver = false;

	int dead = 0;
	boolean win = false;

	public AlienField(int posx, int posy, int ro, int co, PImage sprite){
		this.x = posx;
		this.y = posy;

		this.rows = ro;
		this.cols = co;
		this.sprite = sprite;
	}

	void init(){
			for(int r = 0; r < this.rows; r++) {
				this.aliens.add(new ArrayList<Alien>());
					for(int c = 0; c < this.cols; c++) {
							this.aliens.get(r).add(new Alien(this.x-(this.cols*(this.alienSize*this.alienSpacing))/2 + (this.alienSize*this.alienSpacing)*c,
																						   this.y-(this.rows*(this.alienSize*this.alienSpacing))/2 + (this.alienSize*this.alienSpacing)*r,
																						   this.alienSize, this.sprite));
					}
			}

			this.shootRate = this.alienShootRate/(this.rows*this.cols);
	}

	void render(){
			for(int r = 0; r < this.aliens.size(); r++) {
					for(int c = 0; c < this.aliens.get(r).size(); c++) {
							this.aliens.get(r).get(c).render();
					}
			}
	}

	void update(){
			boolean edge = false;

			this.shootRate = this.alienShootRate/(this.rows*this.cols-this.dead);

			if(this.dead == this.rows*this.cols) {
					this.win = true;
			}

			for(int r = 0; r < this.aliens.size(); r++) {
					for(int c = 0; c < this.aliens.get(r).size(); c++) {
							this.aliens.get(r).get(c).x += this.xSpeed;

							if(this.aliens.get(r).get(c).y + this.aliens.get(r).get(c).size > height - player.size*2 && !this.aliens.get(r).get(c).hit) {
									this.gameOver = true;
							}

							if((this.aliens.get(r).get(c).x + this.aliens.get(r).get(c).size > width ||
									this.aliens.get(r).get(c).x - this.aliens.get(r).get(c).size < 0) &&
								 !this.aliens.get(r).get(c).hit) {
									edge = true;
							}
					}
			}

			if (edge) {
					for(int r = 0; r < this.aliens.size(); r++) {
							for(int c = 0; c < this.aliens.get(r).size(); c++) {
									this.aliens.get(r).get(c).y += this.alienSize;
							}
					}
					this.xSpeed *= -1;
					this.y += this.alienSize
			}
			this.x += this.xSpeed;
	}

	void alienShoot(ArrayList<Bullet> bullets) {
			for(int r = 0; r < this.aliens.size(); r++) {
					for(int c = 0; c < this.aliens.get(r).size(); c++) {
							if(!this.aliens.get(r).get(c).hit) {
									this.aliens.get(r).get(c).shoot(bullets, this.alienShootRate);
							}
					}
			}
	}

	int alienHit(ArrayList<Bullet> bullets) {

			for(int r = this.aliens.size()-1; r >= 0; r--) {
					for(int c = this.aliens.get(r).size()-1; c >= 0; c--) {

							for(int i = bullets.size()-1; i >= 0; i--) {
									if(bullets.get(i).player) {

											if((bullets.get(i).x+bullets.get(i).w/2 >= this.aliens.get(r).get(c).x-this.aliens.get(r).get(c).size/2
											 && bullets.get(i).x-bullets.get(i).w/2 <= this.aliens.get(r).get(c).x+this.aliens.get(r).get(c).size/2)
											&& (bullets.get(i).y+bullets.get(i).h/2 <= this.aliens.get(r).get(c).y+this.aliens.get(r).get(c).size/2
											 && bullets.get(i).y-bullets.get(i).h/2 >= this.aliens.get(r).get(c).y-this.aliens.get(r).get(c).size/2)) {
															this.aliens.get(r).remove(c);
															this.dead++;
															if(this.xSpeed < 0) {
																	this.xSpeed -= this.speedIncrease;
															} else {
																	this.xSpeed += this.speedIncrease;
															}
															bullets.remove(i);
															return 10 * (this.aliens.size() - r);
											}

									}

							}

					}
			}
			return 0;
	}
}
