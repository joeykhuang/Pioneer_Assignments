class Bullet{
	int w = 5;
	int h = 10;
	int speed = 8;
	boolean player;

	public Bullet(int posx, int posy, boolean player){
		this.x = posx;
		this.y = posy;
		this.player = player;
	}

	void render(){
			rectMode(CENTER);
			noStroke();
			if(this.player) {
					fill(0,255,0);
			} else {
					fill(255);
			}
			rect(this.x, this.y, this.w, this.h);
	}

	void update(){
			if(this.player) {
					this.y -= this.speed;
			} else {
					this.y += this.speed/2 ;
			}
	}

	void despawn(){
			if(this.player) {
					if(this.y < - this.h) {
							return true;
					}
			} else {
					if(this.y > height+this.h) {
							return true;
					}
			}

			return false;
	}
}
