//// Project 2 - Converting to objects.

String title="Project #2a (start using objects)";
String author="Bruce Alan Martin; 2016/2/29";

//// GLOBAL DECLARATIONS ////
float horizon;
int score=0, total=0, game=1;

Gold nugget;
Hero mickey;
Monster darth;

// Position & speed of creatures, etc.
//
float xSun=50, ySun=50, dxSun=2;


//// SETUP ////
void setup() {
  size( 640, 480 );
  horizon=  height/4;
  // Instantiate the objects: gold, hero, monster.
  nugget=  new Gold();
  mickey= new Hero();
  darth= new Monster();
  reset();
}
void reset() {
  mickey.reset();
  darth.reset();
  // Place gold at random position.
  nugget.x=  random( 100, width-100 );
  nugget.y=  random( horizon+20, height-20 );
}

//// NEXT FRAME:  scene, show, action, messages ////
void draw() {
  scene();
  show();
  action();
  credits();
}

//// SCENE:  sky, sun, tree, house. ////
void scene() {
  background( 150, 200, 240 );            // blue sky
  noStroke();
  fill( 255, 255, 0 );                   // yellowish sun.
  ellipse( xSun, ySun, 30, 30 );
  fill( 50, 200, 100 );                   // greenish grass
  rectMode( CORNER );
  rect( 0, horizon, width, height*3/4 );
  // Sun moves across sky.  
  xSun =  xSun + dxSun;
  // Sunset.
  if (xSun > width) {
    xSun=  20;
    ySun=  random( 20, horizon-20 );
    dxSun=  random( 0.5, 4 );
    score -= 25;
    // Lose 25 points per day!
  }
}

//// SHOW:  display the creatures, etc.
void show() {
  // ++++ ADD CODE HERE +++
  nugget.show();
  mickey.show();
  darth.show();
}

//// ACTION:  move the creatures, etc.
void action() {
  // Check for collisions.
  if ( dist( mickey.x, mickey.y, nugget.x, nugget.y )  < 50 ) {
    //// Hero got the gold!
    score=  score + 100;
    reset();
  }
  if ( dist( darth.x, darth.y, mickey.x, mickey.y )  < 50 ) {
    //// Monster catches hero.  :-(
    score=  score - 100;              // Lose 100 points!
    reset();
  }
  // Change speeds, to chase.
  if (nugget.y>horizon) {
    // Hero chases gold.
    mickey.dx=  (nugget.x - mickey.x) / frameRate;
    mickey.dy=  (nugget.y - mickey.y) / frameRate;
    // Monster chases hero.
    darth.dx=  (mickey.x - darth.x) / frameRate;
    darth.dy=  (mickey.y - darth.y) / frameRate;
  }
  // Now, move the creatures.
  mickey.move();
  darth.move();
}

//// MESSAGES:  display title, author, messages
void credits() {
  // Title, author.
  fill(255, 0, 0);
  textSize(20);
  text( title, width/3, 20 );
  // Display the score (if any).
  if (score != 0) {
    text( "Score:  "+score, width*2/3, 50 );
  }
  textSize(12);
  if (game>1) {
    float grandTotal=  total = score;
    text( "Game "+ game +"  Total=" +grandTotal, width*2/3, 65 );
  }
  fill(0);
  text( " +100 for gold.  -150 if caught!", width*2/3, 90 );
  text( " -10 per day.  -25 to reset.", width*2/3, 105 );
  //
  text( author, 10, height-10 );
  text( "q to quit; r to reset; g for new game.", width/2, height-10 );
}

//// EVENT HANDLERS ////
void keyPressed() {
  if (key == 'q') { 
    exit();
  }
  if (key == 'r') { 
    reset();
    score -= 25;
    // It costs you 25 points to reset & move the gold!
  }
  if (key == 'g') { 
    game++;
    total += score;
    score=0;
    reset();
  }
}

//// OBJECTS:  Gold, Hero, Monster. ////

class Gold {
  float x=0, y=0;
  // Display the (sparkling) gold, if y is below the horizon. //
  void show() {
    // Gold.
    if (y > horizon) { 
      fill( random(240, 250), random(150, 180), random(0, 100) );
      stroke( random(250), random(150), 0 );
      ellipse( x, y, 50+random(-3, +3), 30+random(-1, +1) );
    }
  }
}


class Hero
{
  // PROPERTIES //
  float x, y, dx, dy;          // Coordinates & speed.
  float w=50, h=80;          // Dimensions of hero (small).
  float hHead= w * 2/3;      // Head height.
  float r=200, g=50, b=50;   // Color of hero

  // METHODS //
  void reset() {
    // (Mickey starts at left.)
    mickey.x=  50;
    mickey.y=  horizon+50;
    dx=5;
    dy=3;                    // (fast)
  }
  //// Display the hero;
  void show() {
    fill( r, g, b );
    rectMode( CENTER );
    rect( x, y, w, h );
    fill( r+50, g+50, b+50 );            // Lighter face.
    ellipse( x, y-h/2-hHead/2, hHead-10, hHead );
  }
  //// Move the hero; bounce off walls.
  void move() {
    if (x>width || x<0) { 
      dx= -dx;
    }
    if (y>height || y<horizon) { 
      dy= -dy;
    }
    x += dx;
    y += dy;
  }
}

class Monster
{
  // PROPERTIES //
  float x, y, dx, dy;          // Coordinates & speed.
  float w=90, h=150;         // Dimensions of mosnter (big).
  float hHead= w * 2/3;      // Head height.
  float r=20, g=100, b=50;   // Color of monster (dark);

  // METHODS //
  void reset() {
    // (Monster starts at right.)
    darth.x=  width-50;
    darth.y=  height-50;
    dx=2;                    // Speed (slow).
    dy=1;
  }

  //// Display the monster;
  void show() {
    fill( r, g, b );
    rectMode( CENTER );
    rect( x, y, w, h );
    fill( r+50, g+50, b+50 );            // Lighter face.
    ellipse( x, y-h/2-hHead/2, hHead-10, hHead );
  }
  //// Move & bounce off walls.
  void move() {
    if (x>width || x<0) { 
      dx= -dx;
    }
    if (y>height || y<horizon) { 
      dy= -dy;
    }
    x += dx;
    y += dy;
  }
}
