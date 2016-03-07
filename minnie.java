//// Project 2 - Converting to objects.

String title="Project #2a (start using objects)";
String author="Bruce Alan Martin; 2016/2/29";

//// GLOBAL DECLARATIONS ////
float horizon;
float xSun=50, ySun=50, dxSun=2;
int score=0, total=0, game=1;

Nugget gold, silver, uranium;
Hero mickey, minnie;
Monster darth;


//// SETUP ////
void setup() {
  size( 800, 600 );
  horizon=  height/4;
  // Instantiate the objects: gold, hero, monster.
  mickey= new Hero();
  //
  minnie= new Hero();
  minnie.r=  100;
  minnie.g=  100;  
  minnie.b=  200;  
  minnie.x += 100;
  minnie.y = horizon + 100;
  darth= new Monster();
  gold=  new Nugget();
  // More metals.
  silver=  new Nugget();
  silver.r= 180; 
  silver.g= 180; 
  silver.b= 200; 
  uranium=  new Nugget();
  uranium.r= 200; 
  uranium.g= 50; 
  uranium.b= 100; 
  reset();
}
void reset() {
  mickey.reset();
  minnie.reset();
  //
  darth.reset();
  // Place metals at random position.
  gold.x=  random( 100, width-100 );
  gold.y=  random( horizon+20, height-20 );
  //
  silver.x=  random( 100, width-100 );
  silver.y=  random( horizon+20, height-20 );
  uranium.x=  random( 100, width-100 );
  uranium.y=  random( horizon+20, height-20 );
}

//// NEXT FRAME:  scene, show, action, messages ////
void draw() {
  background( 150, 200, 240 );            // blue sky  
  if (key == '?') {
    help();
    return;
  }
  scene();
  if (score < -500) {
    textSize( 30 );
    fill( 255, 0, 255 );
    text( "G A M E    O V E R", width/3, height/2 );
    textSize( 12 );
    fill( 255, 0, 255 );
    text( "Press 'g' key for new game.", width/2, 100+height/2 );
    return;
  }
  show();
  action();
  credits();
}
// Display help messages.
void help() {
  fill(0);
  text( "Click the mouse to reposition the hero.", width/3, height/2 );
  text( "q to quit; r to reset; g for new game.", width/3, 15+height/2 );
}

//// SCENE:  sun, tree, house. ////
void scene() {
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
    score -= 25;                      // Lose 25 points per day!
  }
}

//// SHOW:  display the creatures, etc.
void show() {
  mickey.show();
  minnie.show();
  darth.show();
  //
  gold.show();
  silver.show();
  uranium.show();
}

//// ACTION:  move the creatures, etc.
void action() {
  // Check for collisions.
  if ( dist( mickey.x, mickey.y, gold.x, gold.y )  < 50 ) {
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
  if (gold.y>horizon) {
    // Hero chases gold.
    mickey.dx=  (gold.x - mickey.x) / frameRate;
    mickey.dy=  (gold.y - mickey.y) / frameRate;
    // Monster chases hero.
    darth.dx=  (mickey.x - darth.x) / frameRate;
    darth.dy=  (mickey.y - darth.y) / frameRate;
  }
  // Now, move the creatures.
  mickey.move();
  minnie.move();
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
  text( "q to quit; r to reset; g for new game, ? for help", width/2, height-10 );
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
  if (key == 'x') {  
    score -= 200;
  }
  // Metals.
  if (key == 's') { 
    silver.x=  random( 100, width-100 );
    silver.y=  random( horizon+20, height-20 );
  }
  if (key == 'u') { 
    uranium.x=  random( 100, width-100 );
    uranium.y=  random( horizon+20, height-20 );
  }
}
void mousePressed() {
  mickey.x=  mouseX;
  mickey.y=  mouseY;
  score -= 50;
}


//// OBJECTS:  Nugget, Hero, Monster. ////

class Nugget {
  float x=0, y=0;
  float r=220, g=120, b=50;
  // Display the (sparkling metal, if y is below the horizon. //
  void show() {
    if (y < horizon) { 
      return;
    }    // Not visible.
    fill( r+random(25), g+random(25), b+random(25) );
    stroke( r+random(-25, +25), g+random(-25, +25), b+random(-25, +25) );
    ellipse( x, y, 50+random(-3, +3), 30+random(-1, +1) );
  }
}

class Hero
{
  //// PROPERTIES ////
  float x, y, dx, dy;        // Coordinates & speed.
  float w=50, h=80;          // Dimensions of hero (small).
  float hHead= w * 2/3;      // Head height.
  float r=200, g=50, b=50;   // Color of hero

  //// METHODS ////
  // (Mickey starts at left.)
  void reset() {
    mickey.x=  50;
    mickey.y=  horizon+50;
    dx=5;
    dy=3;                    // (fast)
  }
  // Display the hero;
  void show() {
    fill( r, g, b );
    rectMode( CENTER );
    rect( x, y, w, h );
    // Lighter face.
    fill( r+50, g+50, b+50 );
    ellipse( x, y-h/2-hHead/2, hHead-10, hHead );
  }
  // Move the hero; bounce off walls.
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
  //// PROPERTIES ////
  float x, y, dx, dy;          // Coordinates & speed.
  float w=90, h=150;         // Dimensions of mosnter (big).
  float hHead= w * 2/3;      // Head height.
  float r=20, g=100, b=50;   // Color of monster (dark);

  //// METHODS ////
  // (Monster starts at right.)
  void reset() {
    darth.x=  width-50;
    darth.y=  height-50;
    dx=2;                    // Speed (slow).
    dy=1;
  }
  // Display the monster;
  void show() {
    fill( r, g, b );
    rectMode( CENTER );
    rect( x, y, w, h );
    // Lighter face.
    fill( r+50, g+50, b+50 );
    ellipse( x, y-h/2-hHead/2, hHead-10, hHead );
  }
  // Move & bounce off walls.
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
