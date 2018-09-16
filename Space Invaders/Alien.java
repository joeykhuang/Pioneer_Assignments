class Alien{
	int x;
	int y;
	int size;
	PImage sprite;
	public Alien(int posx, int posy, int s, PImage sprite) {
		this.x = posx;
		this.y = posy;

		this.size = s;
		this.sprite = sprite;
	}

	void render() {
			 imageMode(CENTER);
			 image(this.sprite, this.x, this.y, this.size, this.size);
	}

	void shoot(ArrayList<Bullet> bullets, float shootRate) {
        if(random() < shootRate) {
            bullets.add(new Bullet(this.x, this.y, false));
        }
    }
}
