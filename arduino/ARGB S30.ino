//pixels.setPixelColor(id, R, B, G);

#include <Adafruit_NeoPixel.h>
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(19, 6, NEO_GRB + NEO_KHZ800);


void setup()
{
  pixels.begin();
  pixels.clear();
  pixels.setBrightness(50);
}

int led[19];

void sparkle()
{
  if(!(random()%500))
  {
    led[random()%19] = random()%50 + 205;
  }

  for (int i=0; i<20; i++)
 {
    pixels.setPixelColor(i, 0, led[i], 0); 
    
   if(led[i]>60)
   {
     led[i]--;
   }

 }
pixels.show();
}


void loop()
{

  sparkle();

}
