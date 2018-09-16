/* Space Invaders Game
	 Copyright © 2018 by Yanqing Huang

	 Move around the spaceship with 'a' and 'd' keys.
	 Fire at the aliens with 'w' key.

	 Each alien is worth 10 points, and you have 3 lives.
	 You win if you take out all the aliens before they kill you.

	 Happy Defending!
*/

// initialization of global variables, including the player, the shields, the bullets, and the aliens
Player player;

ArrayList<Bullet> bullets = new ArrayList<Bullet>();
ArrayList<Shield> shields = new ArrayList<Shield>();

AlienField alienField;
PImage invaderSprite;

int shieldNum = 4;
int invaderCols = 10;
int invaderRows = 5;

void setup() {
	// create a 700 by 750 pixel black canvas.
	size(700, 750);
	background(0);

	// load the alien image for the aliens
	invaderSprite = loadImage("inv.png");

	// instantiate the player
	player = new Player();

	// create the aliens and initialize their position
	alienField = new AlienField(width/2, height/4, invaderRows, invaderCols, invaderSprite);
	alienField.init();

	// create the shields and initialize their positions
	for(int i = 0; i < shieldNum; i++) {
			shields.add(new Shield(width*0.1+(width*0.8)/(shieldNum-1)*i, height-150));
			shields.get(i).init();
	}
}

void draw() {
    background(0);
    // regular gameplay
    if(!player.gameOver && !alienField.gameOver && !alienField.win) {
			// update and display the positions of the bullets
			for(int i = bullets.size() - 1; i >= 0; i--) {
					bullets.get(i).update();
					bullets.get(i).render();
					if(bullets.get(i).despawn()){
							bullets.remove(i);
					}
			}
			// update and display the shield conditions
			for(int i = 0; i < shieldNum; i++) {
					shields.get(i).render();
					shields.get(i).update(bullets);
			}

			// update the alien field, and fire bullets
			alienField.update();
			alienField.alienShoot(bullets);
			alienField.render();

			// update the player scores, the player location, and remaining lives
			player.score += alienField.alienHit(bullets);
			player.render();
			player.update();
			player.hit(bullets);

			// display the game over screen if player loses
    } else if(!alienField.win && (player.gameOver || alienField.gameOver)) {
			// display "Game Over" and the player's score
			textSize(width/10);
			fill(255);
			textAlign(CENTER, CENTER);
			text("Game Over!", width/2, height/2 - width/10);
			text("Score: " + player.score, width/2, height/2);

			// display the winning screen if player wins
    } else if(alienField.win) {
        textSize(width/10);
        fill(255);
        textAlign(CENTER, CENTER);
        text("You Win", width/2, height/2 - width/10);
        text("Score: " + player.score, width/2, height/2);
    }
}

// if the key is pressed, move the player
void keyPressed() {
    switch(key) {
				// move left if the key is 'a'
        case 'a':
            player.moveLeft = true;
            break;
				// move right if the key is 'd'
        case 'd':
            player.moveRight = true;
            break;
				// fire bullet if the key is 'w'
        case 'w':
            bullets.add(player.shoot());
						setTimeout(keyPressed, 1000);
            break;
				case ' ':
            bullets.add(player.shoot());
						setTimeout(keyPressed, 1000);
            break;
		}
}

// if the key is released, stop moving the player
void keyReleased() {
    switch(key) {
				// stop moving left if the key is 'a'
        case 'a':
            player.moveLeft = false;
            break;
				// stop moving right if the key is 'd'
        case 'd':
            player.moveRight = false;
            break;
    }
}
