import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Lightning extends PApplet {

//Aaron Chow
/* @pjs preload="ShipVersion4.png"; */

boolean [] keys;
int opacity = 255;
int score = 0;
boolean isFading = false;
boolean isAppearing = false;
boolean accelerating;
boolean deccelerating;
boolean clockWise;
boolean counterClockWise;
boolean destroyBullet = false;
SpaceShip apollo;
Star [] galaxy = new Star[30];
ArrayList <Asteroid> meteor = new ArrayList <Asteroid>();
ArrayList <Bullet> bulletHolder = new ArrayList <Bullet>();
public void setup() 
{
  keys = new boolean[4];
  background(0);
  frameRate(61);
  size(500,500, P2D);
  apollo = new SpaceShip();
  for(int index = 0; index < galaxy.length; index++)
    galaxy[index] = new Star();
  for(int index = 0; index < 11; index++){
    Asteroid myMeteor = new Asteroid();
    meteor.add(myMeteor);  
  }
    imageMode(CENTER);
}

public void draw() 
{
  background(0);
  textSize(30);
  textAlign(CENTER);
  fill(135,206,235);
  text("Score: " + score, 420, 40);

  //Displays background stars
  for(Star tempStar: galaxy){
    tempStar.blink();
    tempStar.show();
  }

  //Creates new set of asteroids if all have been destroyed
  if(meteor.size() == 0){
    for(int index = 0; index < 11; index++){
      Asteroid myMeteor = new Asteroid();
      meteor.add(myMeteor);  
    }
  }

  //Shows and Moves asteroids
  for(int index = 0; index < meteor.size(); index++){
    Asteroid tempRock = meteor.get(index);
    tempRock.show();
    tempRock.move();
    if(dist(tempRock.getX(),tempRock.getY(), apollo.getX(), apollo.getY()) < 35){
      meteor.remove(index);
    }
  }
 
  //Causes ship to disappear and reappear with random location and direction
  if(isFading == true)
    opacity = opacity - 5;
  if(opacity <= 0){
    apollo.setX((int)(Math.random()*450) + 25);
    apollo.setY((int)(Math.random()*450) + 25);
    apollo.setPointDirection((int)(Math.random()*360));
    apollo.setDirectionX(0);
    apollo.setDirectionY(0);
    isFading = false;
    isAppearing = true;
    opacity = 15;;
  }
  if(isAppearing){
    opacity = opacity + 10;    
  }
  if(opacity >= 255)
    isAppearing = false;

  //Causes scapeship to accelerate and deccelerate
  if(keys[0])
    apollo.accelerate(.1f);
  if(deccelerating)
    apollo.accelerate(-.1f);
  if(clockWise)
    apollo.spin(-10);
  if(counterClockWise)
    apollo.spin(10);

  apollo.show();
  apollo.move();

//Moves,shows, and removes bullet after a collision
  for(int index = 0; index < bulletHolder.size(); index++){
    Bullet tempBullet = bulletHolder.get(index);
    tempBullet.show();
    tempBullet.move();
    for(int i = 0; i < meteor.size(); i++){
      Asteroid tempRock = meteor.get(i);
      if(dist(tempRock.getX(),tempRock.getY(), tempBullet.getX(), tempBullet.getY()) < 20){
        meteor.remove(i);
        score++;
        destroyBullet = true;
      }
    }
    if(destroyBullet){
      bulletHolder.remove(tempBullet);
      destroyBullet = false;
    }
  }
/*
  if(gameOver){
    background(0);
    textSize(50);
    textAlign(CENTER);
    fill(255);
    text("You died",250,250);
  }
  */
}

public void keyPressed()
{
  if( key == 'w')
    keys[0] = true;
    //accelerating = true;
  if(key == 's')
    deccelerating = true;
  if(key == 'a')
    clockWise = true;
  if(key == 'd')
    counterClockWise = true;
  if(key == 'h')
    apollo.hyperspace();
  if(key == ' '){
    Bullet projectile = new Bullet(apollo);
    bulletHolder.add(projectile);
  }

}
public void keyReleased()
{
  if( key == 'w'){
    keys[0] = false;
    //accelerating = false;
  }
  if(key == 's'){
    deccelerating = false;
  }  
  if(key == 'a'){
    clockWise = false;
  }  
  if(key == 'd'){
    counterClockWise = false;   
  }
}

class SpaceShip extends Floater
{   
  private PImage picture;
  private double dRadians;
  SpaceShip()
  {
    picture = loadImage("ShipVersion4.png");
    myCenterX = 250;
    myCenterY = 250;
    myDirectionX = 0;
    myDirectionY = 0;
    myPointDirection = 0;
  }
  public void setX(int x) {myCenterX = x;}
  public int getX() {return myCenterX;}
  public void setY(int y) {myCenterY = y;}
  public int getY() {return myCenterY;}
  public void setDirectionX(double x) {myDirectionX = x;}
  public double getDirectionX() {return myDirectionX;}  
  public void setDirectionY(double y) {myDirectionY = y;}
  public double getDirectionY() {return myDirectionY;} 
  public void setPointDirection(int degrees) {myPointDirection = degrees;}   
  public double getPointDirection() {return myPointDirection;} 

  public void show()
  {
    pushMatrix();
      translate((float)myCenterX,(float)myCenterY);
      dRadians = myPointDirection*(Math.PI/180); 
      rotate((float)dRadians);
      tint(255,opacity);
      image(picture,0,0,width/6,height/6);              
    popMatrix();
  }

  public void accelerate (double dAmount)   
  { 
    int maxSpeed = 8;         
    //convert the current direction the floater is pointing to radians    
    dRadians = myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));
    if(myDirectionX > maxSpeed)
      myDirectionX = maxSpeed;
    if(myDirectionY > maxSpeed)
      myDirectionY = maxSpeed;   
  }
  public void hyperspace() //Causes ship to disappear and reappear with random location and direction
  {
    isFading = true;
  }
}

class Bullet extends Floater
{
  private double dRadians;
  public Bullet(SpaceShip theShip)
  {
    myCenterX = theShip.getX();
    myCenterY = theShip.getY();
    myPointDirection = theShip.getPointDirection();
    dRadians = myPointDirection*(Math.PI/180);
    myDirectionX = 15 * Math.cos(dRadians) + theShip.getDirectionX();
    myDirectionY = 15 * Math.sin(dRadians) + theShip.getDirectionY();
  }
  public void setX(int x) {myCenterX = x;}
  public int getX() {return myCenterX;}
  public void setY(int y) {myCenterY = y;}
  public int getY() {return myCenterY;}
  public void setDirectionX(double x) {myDirectionX = x;}
  public double getDirectionX() {return myDirectionX;}  
  public void setDirectionY(double y) {myDirectionY = y;}
  public double getDirectionY() {return myDirectionY;} 
  public void setPointDirection(int degrees) {myPointDirection = degrees;}   
  public double getPointDirection() {return myPointDirection;} 

  public void show()
  {
    noStroke();
    fill(135,206,235);
    ellipse(myCenterX,myCenterY,10,10);
  }
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;   
  }
}

class Star
{
  private float mySize;
  private int starOpacity;
  private float shrinkValue;
  private int starCenterX;
  private int starCenterY;
  public Star()
  {
    shrinkValue = .05f;
    starOpacity = ((int)(Math.random()*255));
    starCenterX = ((int)(Math.random()*500));
    starCenterY = ((int)(Math.random()*500));
    mySize = ((float)(Math.random()*6));
  }
  public void blink()
  {
    /*
    starOpacity -= shrinkValue;
    if(starOpacity <= 50)
    {
      shrinkValue *= -1;
      starOpacity = 55;
    }
    else if(opacity >= 255 && shrinkValue == -5)
    {
      shrinkValue *= -1;
    }
  */
    mySize = mySize - shrinkValue;
    if(mySize <= 1)
      shrinkValue = shrinkValue * -1;
    if(mySize >= 5 && shrinkValue == -.05f)
      shrinkValue = shrinkValue * -1;
    
  }
  public void show()
  {
    fill(255, starOpacity);
    ellipse(starCenterX,starCenterY,mySize,mySize);
  }
}

class Asteroid extends Floater
{
  private int rotSpeed = ((int)(Math.random()*10)-5);
  Asteroid()
  {
    corners = 11;
    xCorners = new int[corners];
    yCorners = new int[corners];
    myColor = color(255,255,255);
    xCorners[0] = -4;
    yCorners[0] = -6;
    xCorners[1] = 0;
    yCorners[1] = -8;
    xCorners[2] = 1;
    yCorners[2] = -7;
    xCorners[3] = 3;
    yCorners[3] = -8;
    xCorners[4] = 5;
    yCorners[4] = -4;
    xCorners[5] = 3;
    yCorners[5] = -2;
    xCorners[6] = 5;
    yCorners[6] = -2;
    xCorners[7] = 3;
    yCorners[7] = 2;
    xCorners[8] = -1;
    yCorners[8] = 2;
    xCorners[9] = -5;
    yCorners[9] = -1;
    xCorners[10] = -4;
    yCorners[10] = -4;
    for(int index = 0; index < xCorners.length; index++)
    {
      xCorners[index] = xCorners[index] * 3;
      yCorners[index] = yCorners[index] * 3;
    }
    myCenterX = (int)(Math.random()*425) + 25;
    myCenterY = (int)(Math.random()*425) + 25;    
    myDirectionX = ((int)(Math.random()*5)) - 2;
    myDirectionY = ((int)(Math.random()*5)) - 2;
    if(myDirectionX == 0)
    {
      if(Math.random() < .5f)
        myDirectionX--;
      else
        myDirectionX++;
    }
    if(myDirectionY == 0)
    {
      if(Math.random() < .5f)
        myDirectionY--;
      else
        myDirectionY++;
    }  
  }

  public void setX(int x) {myCenterX = x;}
  public int getX() {return myCenterX;}
  public void setY(int y) {myCenterY = y;}
  public int getY() {return myCenterY;}
  public void setDirectionX(double x) {myDirectionX = x;}
  public double getDirectionX() {return myDirectionX;}  
  public void setDirectionY(double y) {myDirectionY = y;}
  public double getDirectionY() {return myDirectionY;} 
  public void setPointDirection(int degrees) {myPointDirection = degrees;}   
  public double getPointDirection() {return myPointDirection;} 

  public void move(){
    spin(rotSpeed);
    super.move();
  }

  public void show()
  {
    fill(myColor);
    super.show();
  }

}

abstract class Floater //Do NOT modify the Floater class! Make changes in the SpaceShip class 
{   
  protected int corners;  //the number of corners, a triangular floater has 3   
  protected int[] xCorners;   
  protected int[] yCorners;   
  protected int myColor;   
  protected int myCenterX, myCenterY; //holds center coordinates   
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
  protected double myPointDirection; //holds current direction the ship is pointing in degrees    
  abstract public void setX(int x);  
  abstract public int getX();   
  abstract public void setY(int y);   
  abstract public int getY();   
  abstract public void setDirectionX(double x);   
  abstract public double getDirectionX();   
  abstract public void setDirectionY(double y);   
  abstract public double getDirectionY();   
  abstract public void setPointDirection(int degrees);   
  abstract public double getPointDirection(); 

  //Accelerates the floater in the direction it is pointing (myPointDirection)   
  public void accelerate (double dAmount)   
  {          
    //convert the current direction the floater is pointing to radians    
    double dRadians = myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians));       
  }   
  public void spin (int nDegreesOfRotation)   
  {     
    //rotates the floater by a given number of degrees    
    myPointDirection+=nDegreesOfRotation;   
  }   
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX > width)  
      myCenterX = 0;      
    else if (myCenterX<0)
      myCenterX = width;    

    if(myCenterY > height)
    {    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0)
    {     
      myCenterY = height;    
    }   
  }   
  public void show ()  //Draws the floater at the current position  
  {             
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotate and translate the coordinates of the floater using current direction 
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
  }   
} 


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Lightning" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
