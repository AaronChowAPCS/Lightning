//Aaron Chow
//AP Computer science Block 3

int startX = 0;
int startY = 210;
int endX = 0;
int endY = 150;
int colorOne = 0;
boolean flash = true;
void setup()
{
  size(425,425);
  strokeWeight(1);
  frameRate(10);
  background(0,0,0);
}
void draw()
{
	createLightning();
}
void mousePressed()
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
void createLightning()
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

