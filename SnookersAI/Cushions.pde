//an object containing all the cushions on the pool table
class Cushions {
  Body[] bodies = new Body[6];//the cushions
  Box2DProcessing world;

//------------------------------------------------------------------------------------------------------------------------------------------------------------------
   //constructor
  Cushions ( Box2DProcessing box2d) {

    world = box2d;
    
    //Create top left 
    createCushion(0, 200, 40, 22, 70, 40, 687, 40, 703, 22, 0);
    
    //Create top right 
    createCushion(0, 600, 737, 22, 753, 40, 1368, 40, 1398, 22, 1);
    
   
    //Create right
    createCushion(200, 0, 1400, 72, 1400, 686, 1418, 713, 1418, 45, 2);

    //Create right bottom
    createCushion(200, 0, 737, 736, 1398, 736, 1368, 717, 753, 717, 3);

    //Create left bottom
    createCushion(200, 0, 40, 736, 703, 736, 687, 717, 70, 717, 4);
    
    //Create left
    createCushion(200, 0, 22, 45, 22, 713, 40, 686, 40, 72, 5);
  }
  
//------------------------------------------------------------------------------------------------------------------------------------------------------------------  
 //creates a cushion in the box2d world with input position and dimensions 
  void createCushion(int posX, int posY, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4, int bodyNo) {
    //CREATE BODY DEFITION
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;//STATIC means that the body is fixed 
    bd.position.set(world.coordPixelsToWorld(posX, posY));

    //CREATE BODY
    bodies[bodyNo] = world.createBody(bd);//add body to the array

    //CREATE SHAPE 
    //a polygon with 4 vertices 
    PolygonShape ps = new PolygonShape();
    Vec2[] vertices = new Vec2[4];
    vertices[0] = world.vectorPixelsToWorld(new Vec2(x1-posX, y1-posY));
    vertices[1] = world.vectorPixelsToWorld(new Vec2(x2-posX, y2-posY));
    vertices[2] = world.vectorPixelsToWorld(new Vec2(x3-posX, y3-posY));
    vertices[3] = world.vectorPixelsToWorld(new Vec2(x4-posX, y4-posY));

    ps.set(vertices, vertices.length);
    //CREATE FIXTURE
    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 1;
    fd.friction = 0.0001;
    fd.restitution  = 0.9;//high restitution or bounciness 

    //ATTACH FIXTURE TO BODY
    bodies[bodyNo].createFixture(fd);
  }
//------------------------------------------------------------------------------------------------------------------------------------------------------------------
 //draw the cushions as polygons
  void show() {
    
    noStroke();
    fill(0, 100, 0);
    
    //top left 
    beginShape();
      vertex(40, 22);
      vertex(70, 40);
      vertex(687, 40);
      vertex(703, 22);
    endShape(CLOSE);
    
    //top right 
    beginShape();
      vertex(737, 22);
      vertex(753, 40);
      vertex(1368, 40);
      vertex(1398, 22);
    endShape(CLOSE);
    
    //left 
    beginShape();
      vertex(22, 45);
      vertex(40, 72);
      vertex(40, 686);
      vertex(22, 713);
    endShape(CLOSE);
    
    //right
    beginShape();
      vertex(1418, 45);
      vertex(1400, 72);
      vertex(1400, 686);
      vertex(1418, 713);
    endShape(CLOSE);

    //bottom left
    beginShape();
      vertex(40, 736);
      vertex(70, 717);
      vertex(687, 717);
      vertex(703, 736);
    endShape(CLOSE);
    
    //bottom right
    beginShape();
      vertex(737, 736);
      vertex(753, 717);
      vertex(1368, 717);
      vertex(1398, 736);
    endShape(CLOSE);
  }
}
