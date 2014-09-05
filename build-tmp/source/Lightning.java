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
//AP Computer science Block 3

int startX = 0;
int startY = 210;
int endX = 0;
int endY = 150;
int colorOne = 0;
boolean flash = true;
public void setup()
{
  size(425,425);
  strokeWeight(1);
  frameRate(10);
  background(0,0,0);
}
public void draw()
{
	createLightning();
}
public void mousePressed()
{
	if(flash == true)
	{
		flash = false;
		background(0);
		colorOne = (int)(Math.random()*255);
	}
	else if(flash == false)
		flash = true;
}
public void createLightning()
{
	if(flash == true)
	{
	background(0);
 	while(endX < 425) //This loop draws a single lightning bolt
 		{
 			stroke(colorOne,startX - 100,startY - 100);
 			endX = startX + (int)(Math.random()*20);
 			endY = startY + (int)(Math.random() * 50) - 25;
 			line(startX, startY, endX, endY); 	
 			startX = endX;
 			startY = endY;
 		}
 	startX = 0;
	startY = 210;
	endX = 0;
	endY = 150;
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
