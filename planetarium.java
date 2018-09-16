/* Gravity Simulation
	 Copyright © 2018 by Yanqing Huang
	 July ~ 2018

	 Instructions:
	 Click to create a new planet.
	 Sit back and enjoy!

*/

// initialization of radius & mass variables
int planetRadius = 25; // radius of the planets
int sunSize = 30; // radius of the sun
int d_sunSize = 20; // radius of the sun's glow
float sunRingAlpha = 70; // transparency of the sun's glow
int sunMass = 5; // mass of the sun
float planetMass = 1; // mass of the planets
float minisculePlanetMass = 0.01; // mass of the unshown planet

Planet minisculePlanet = new Planet(width/2, height/2, planetRadius,
																		minisculePlanetMass, 0, 0); // unshown planet to attract (if there are only one planet)
double GRAV_ACC = 7; // gravitational acceleration constant

ArrayList<Planet> planets = new ArrayList<Planet>(); // array of planets

// initialization of colors
color yellow = color(255, 255, 0);
color white = color(255);
color planetColor = color(255);

void setup(){

	// draw a black canvas
  size(1000,600);

	// create initial planets
	planets.add(new Planet(width / 2 - 60, height / 2, planetRadius, planetMass, 0, 0.763762615825973));
	planets.add(new Planet(width / 2 + 120, height / 2, planetRadius, planetMass, 0, 0.540061724867322));
}

void draw(){
	background(0);

	// draw the sun
  fill(yellow);
  ellipse(width/2, height/2, sunSize, sunSize);

	// draw the sun's glow
  fill(white, sunRingAlpha);
  ellipse(width/2, height/2, sunSize + d_sunSize, sunSize + d_sunSize);

	// when there are more than one planets
	if (planets.size() > 1){
		// loop through all the planets to draw and move them
		for(int i = 0; i < planets.size(); i++){
			// the first planet to be drawn
			if(i == 0){
				planets.get(i).calculateAcceleration(minisculePlanet,
																						 planets.get(i + 1)); }
			// planets in the middle
			else if (i == planets.size() - 1){
				planets.get(i).calculateAcceleration(planets.get(i - 1),
																						 minisculePlanet); }
			// last planet to be drawn
			else {
				planets.get(i).calculateAcceleration(planets.get(i - 1),
																						 planets.get(i + 1)); }

			// move and show all the planets
			planets.get(i).move();
			planets.get(i).show();

			// dealing with planets off the screen: deleting them from array
			if(planets.get(i).x > width || planets.get(i).x < 0 ||
				 planets.get(i).y < 0 || planets.get(i).y > height){
				planets.remove(i);
			}
		}
	}
	// when there are one or no planets left
	else if (planets.size() == 1){
		// inner and outer planets will the the sun
		planets.get(0).calculateAcceleration(minisculePlanet, minisculePlanet);
		// show and move planet
		planets.get(0).move();
		planets.get(0).show();
	}
}

// create new planet when mouse pressed
void mousePressed(){
  createNewPlanet();
}

// create a new planet
void createNewPlanet(){
  planets.add(new Planet(mouseX, mouseY, planetRadius, planetRadius,
												 calculateXVelocity(mouseX, mouseY), calculateYVelocity(mouseX, mouseY)));
}

// function to calculate the distance between two points
float calculateDistance(int x1, int y1, int x2, int y2){
	float	distance = (float)(Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)));

	// if distance is too small, suppose that the distance is larger so that the
	// mutual attraction effect is better. (CAN BE DELETED)
	if (distance < 5){distance *= 3;}
	if (distance < 1){distance *= 15;}
	return distance;
}

// function to calculate the angle between two points (point 1 as the origin)
float calculateAngle(int x1, int y1, int x2, int y2){
	return atan2(y2 - y1, x2 - x1);
}

// calculates
float calculateXVelocity(int planet_x, int planet_y){
	float distance = calculateDistance(width/2, height/2, planet_x, planet_y);
	float theta = calculateAngle(width/2, height/2, planet_x, planet_y);
	return Math.sqrt(GRAV_ACC * sunMass / distance) * cos(theta);
}
float calculateYVelocity(int planet_x, int planet_y){
	float distance = calculateDistance(width/2, height/2, planet_x, planet_y);
	float theta = calculateAngle(planet_x, planet_y, width/2, height/2);
	return Math.sqrt(GRAV_ACC * sunMass / distance) * sin(theta);
}

// planet class
class Planet{

	// planet parameters
  public int x;
  public int y;
  public float mass;
	public int radius;
	double velocity_x = 0;
	double velocity_y = 0;
	double acceleration_x;
	double acceleration_y;

	// constructor of planets
  public Planet(int planetX, int planetY, int radius, float mass,
								 double velocity_x, double velocity_y){
    this.x = planetX;
    this.y = planetY;
		this.radius = radius;
		this.mass = mass;
		this.velocity_x += velocity_x;
		this.velocity_y += velocity_y;
		this.acceleration_x = 0;
		this.acceleration_y = 0;
  }

	// draw the planet
	public void show(){
		fill(planetColor);
		ellipse(this.x, this.y, this.radius, this.radius);
	}

	// calculate the accleration of the planet
	public void calculateAcceleration(Planet innerPlanet, Planet outerPlanet){

		// find the angle between the planet and the sun, as well as its two neighbors (mouse press order)
		float anglePlanetSun = calculateAngle(this.x, this.y, width/2, height/2);
		float anglePlaInPlanet = calculateAngle(this.x, this.y, innerPlanet.x, innerPlanet.y);
		float anglePlaOutPlanet = calculateAngle(this.x, this.y, outerPlanet.x, outerPlanet.y);

		// find the distance between the planet and the sun, as well as its two neighbors (mouse press order)
		float disPlanetSun = calculateDistance(this.x, this.y, width/2, height/2);
		float disPlaInPlanet = calculateDistance(this.x, this.y, innerPlanet.x, innerPlanet.y);
		float disPlaOutPlanet = calculateDistance(this.x, this.y, outerPlanet.x, outerPlanet.y);

		// find the x and y forces the the sun exerts on the planet
		float forcePlanetSun_x = (float) ((GRAV_ACC * this.mass * sunMass)
															/ Math.pow(disPlanetSun, 2)) * cos(anglePlanetSun);
															// derived from the law of Gravitation and circular motion
		float forcePlanetSun_y = (float) ((GRAV_ACC * this.mass * sunMass)
															/ Math.pow(disPlanetSun, 2)) * sin(anglePlanetSun);

		// find the x and y forces the the inner planet exerts on the planet
		float forcePlaInPlanet_x = (float) ((GRAV_ACC * this.mass * innerPlanet.mass)
																/ Math.pow(disPlaInPlanet, 2)) * cos(anglePlaInPlanet);
		float forcePlaInPlanet_y = (float) ((GRAV_ACC * this.mass * innerPlanet.mass)
																/ Math.pow(disPlaInPlanet, 2)) * sin(anglePlaInPlanet);

		// find the x and y forces the the outer planet exerts on the planet
		float forcePlaOutPlanet_x = (float) ((GRAV_ACC * this.mass * outerPlanet.mass)
																 / Math.pow(disPlaOutPlanet, 2)) * cos(anglePlaOutPlanet);
		float forcePlaOutPlanet_y = (float) ((GRAV_ACC * this.mass * outerPlanet.mass)
																 / Math.pow(disPlaOutPlanet, 2)) * sin(anglePlaOutPlanet);

		// find the total x and y forces exerted on planet
		float totalForce_x = forcePlanetSun_x + forcePlaInPlanet_x + forcePlaOutPlanet_x;
		float totalForce_y = forcePlanetSun_y + forcePlaInPlanet_y + forcePlaOutPlanet_y;

		// find the x and y acceleration of the planet
		this.acceleration_x = totalForce_x / this.mass; // derived from Newton's second law
		this.acceleration_y = totalForce_y / this.mass;
	}

	// move the planet
	public void move(){
		// increase the x and y velocities by the acceleration
		this.velocity_x += this.acceleration_x;
		this.velocity_y += this.acceleration_y;
		// increase the x and y coordinated by the velocity
		this.x += this.velocity_x;
		this.y += this.velocity_y;
	}
}
