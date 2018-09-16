class Player{

		// instantiate player variables
    int size = 30;
    public int x = width/2;
    public int y = height - (20 + this.size);
    int h = 10;
    int lives = 3;
    int score = 0;

    int speed = 5;

    boolean moveLeft = false;
    boolean moveRight = false;

    boolean gameOver = false;

    void render(){
        rectMode(CENTER);

        fill(0, 255, 0);
        noStroke();
        rect(this.x, this.y, this.size, this.h);
        rect(this.x, this.y - 6, 5, 5);

        for(int i = 0; i < this.lives; i++) {
            rect(width - 30 - i*(this.size + 5), this.y + this.size, this.size, this.h);
            rect(width - 30 - i*(this.size + 5), this.y - 6 + this.size, 5, 5);
        }

        fill(255);
        textAlign(LEFT, TOP);
        textSize(20);
        text("Score: " + this.score, 0, 0);
    }

    void update() {
        if(this.moveLeft) {
            move(-1);
        } else if(this.moveRight) {
            move(1);
        }

        if(this.x < 0) {
            this.x = width;
        }

        if(this.x > width) {
            this.x = 0;
        }

        if(this.lives <= 0) {
            this.gameOver = true;
        }
    }

    void move(int speedMult) {
        this.x += (this.speed * speedMult);
    }

    Bullet shoot() {
        return new Bullet(this.x, this.y, true);
    }

    void hit(ArrayList<Bullet> bullets) {
        for(int i = bullets.size()-1; i >= 0; i--) {
            if(!bullets.get(i).player) {
                if((bullets.get(i).x > this.x-this.size/2 && bullets.get(i).x < this.x+this.size/2) &&
									 (bullets.get(i).y > this.y-this.h/2 && bullets.get(i).y < this.y+this.h/2))
								{
                    bullets.remove(i);
                    this.lives--;
                }
            }
        }
    }
}
