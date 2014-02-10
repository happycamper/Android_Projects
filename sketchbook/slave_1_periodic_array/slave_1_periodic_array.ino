//This is the framework for the API to receive frames from the android phone

//Dynamic allocation of memory is important as the size of the payload may vary but is set to 2 for now...



//Need wire and SPI libraries

#include <Wire.h>

#include <SPI.h>



//This is for the API to capture the correct data

int startbyte,length,frameid,frametype;

int *payload = (int *)calloc(2,sizeof(int));

int *frame = (int *)calloc(6,sizeof(int));

float float_y,float_x,bright1,bright2,bright3,bright4,bright5,bright6,bright7,bright8;

float phases[16];

int y;

int count = 0;

int checksum = 0;

int payloadcount = 0;

float amin;

float x1=0;

float x2=0;

float x3=0;

float x4=0;

float x5=0.5;

float x6=0.5;

float x7=0.5;

float x8=0.5;

float x9=1;

float x10=1;

float x11=1;

float x12=1;

float x13=1.5;

float x14=1.5;

float x15=1.5;

float x16=1.5;

float y1=0;

float y2=0.5;

float y3=1;

float y4=1.5;

float y5=1.5;

float y6=1;

float y7=0.5;

float y8=0;

float y9 =0;

float y10=0.5;

float y11=1;

float y12=1.5;

float y13=1.5;

float y14=1;

float y15=0.5;

float y16=0;

////////////////////////////////////



//variables to control the circuitry, demux and opamps and digipots

const int _sck = 3;

const int _sdi = 4;

const int pinB = 10;

const int pinA = 9;

const int ctl = 8;

const int pinB2 = 7;

const int pinA2 = 6;

const int ctl2 = 5;

///////////////////////////////





void setup()

{

 

  //Initialize control circuitry pins

  pinMode (_sck, OUTPUT);

  pinMode (_sdi, OUTPUT);

   pinMode(pinB, OUTPUT);

  pinMode(pinA, OUTPUT);

  pinMode(ctl, OUTPUT);

  pinMode(pinB2, OUTPUT);

  pinMode(pinA2, OUTPUT);

  pinMode(ctl2, OUTPUT);

 

  digitalWrite(ctl,HIGH);

  digitalWrite(pinA, LOW);

  digitalWrite(pinB, LOW);

  digitalWrite(ctl2,HIGH);

  digitalWrite(pinA2, LOW);

  digitalWrite(pinB2, LOW);

  ///////////////////////////

 

  //Initialize frame

  init_values();

  Wire.begin(3);                // join i2c bus with address #4

  Wire.onReceive(receiveEvent); // register event

 

  //need 115200 for communication with arduino BT

  Serial.begin(115200);           // start serial for output

 

  // initialize SPI:

  SPI.begin();

}



void loop()

{

  delay(50);

}



// function that executes whenever data is received from master

// this function is registered as an event, see setup()

void receiveEvent(int howMany)

{

  /*while(1 < Wire.available()) // loop through all but the last

  {

      //int x = Wire.receive();

  //int y = Wire.receive();  // receive byte as an integer

//  Serial.println(x);

    //int c = Wire.receive(); // receive byte as a character

    //Serial.print(c);         // print the character

  }*/

  int x = Wire.read();

  //Serial.println(x);

    if(x == 255){ // Look for startbyte

        init_values();

        startbyte = x;

        checksum+=startbyte;

        count++;

    }else if(count == 1){ //only add to frame if you have received a startbyte

        length = x;

        checksum+=length;

        payload = (int *)realloc(payload,length);

        count++;

    }else if(count == 2){

      frametype = x;

      checksum+=frametype;

        count++;

    }else if(count == 3){

      frameid = x;

      checksum+=frameid;

      count++;

    }else if(count > 3){

        if(payloadcount < length){

          payload[payloadcount] = x; //store payload as array

          checksum+=x;

          payloadcount++;

          count++;

        }else{

            checksum = checksum % 6;

            if(x == checksum){

                  x=payload[0]; //reuse memory variables

                   y=payload[1];

                //Serial.println("Packets Match");

                if(y >=128){

                  y = y-256;

                }

              if(x>=128){

                x = x-256;

              }

  float_x=(float)x*-1;  //we are going to have no negative phase shifts in time...

  float_y=(float)y*-1;

  int total;

 

  phases[0] = (x1*float_x)*2+(y1*float_y)*2; //-400,-400

  phases[1] = (x2*float_x)*2+(y2*float_y)*2; //-440,20

  phases[2] = (x3*float_x)*2+(y3*float_y)*2; //-380,260

  phases[3] = (x4*float_x)*2+(y4*float_y)*2; // -200,340

  phases[4] = (x5*float_x)*2+(y5*float_y)*2; //-145,145

  phases[5] = (x6*float_x)*2+(y6*float_y)*2; //-350,-100

  phases[6] = (x7*float_x)*2+(y7*float_y)*2;//-150,-400

  phases[7] = (x8*float_x)*2+(y8*float_y)*2;//-190,-180

  phases[8] = (x9*float_x)*2+(y9*float_y)*2; //12,-124

  phases[9] = (x10*float_x)*2+(y10*float_y)*2; //80,295

  phases[10] = (x11*float_x)*2+(y11*float_y)*2; //180,420

  phases[11] = (x12*float_x)*2+(y12*float_y)*2; // 140,-200

  phases[12] = (x13*float_x)*2+(y13*float_y)*2; //0,0

  phases[13] = (x14*float_x)*2+(y14*float_y)*2; //180,-340

  phases[14] = (x15*float_x)*2+(y15*float_y)*2;//400,-200

  phases[15] = (x16*float_x)*2+(y16*float_y)*2;//450,200

 

  amin = phases[0];

  for(int i=0;i<16;++i){

    if(phases[i] < amin){

        amin = phases[i];

    }

  }

  amin*=-1;

 

  for(int i=0;i<16;++i){

      phases[i]+=amin;

      phases[i]-=(float)360*((int)phases[i]/360);

  }

 

     

 

 

 

 

  /*bright1 = map(bright1,0,360,0,255);

  bright2 = map(bright2,0,360,0,255);

  bright3 = map(bright3,0,360,0,255);

  bright4 = map(bright4,0,360,0,255);

  //bright5 = map(bright5,0,360,0,255);*/

  Serial.print(" phase0: ");Serial.print(phases[0]);Serial.print(" phase1: ");Serial.print(phases[1]);Serial.print(" phase2: ");Serial.print(phases[2]);

  Serial.print(" phase3: ");Serial.print(phases[3]);

  Serial.print(" phase4: ");Serial.print(phases[4]);

  Serial.print(" phase5: ");Serial.print(phases[5]);

  Serial.print(" phase6: ");Serial.print(phases[6]);

  Serial.print(" phase7: ");Serial.print(phases[7]);

  Serial.print("\n");

            setpotvalue(0,phases[0]);

            setpotvalue(1,phases[1]);

            setpotvalue(2,phases[2]);

            setpotvalue(3,phases[3]);

            setpotvalue(4,phases[4]);

            setpotvalue(5,phases[5]);

            setpotvalue(6,phases[6]);

            setpotvalue(7,phases[7]);

 

            }else{

                Serial.print(x);Serial.print(",");Serial.print(checksum);Serial.print("\n");

             }

            }

    }else{

  //int y = Wire.receive();  // receive byte as an integer

  Serial.println(x);         // print the integer

    }

}



void init_values(){

    startbyte = 0;

     length = 0;

     frameid = 0;

     frametype = 0;

     count = 0;

     payloadcount = 0;

     checksum = 0;

}



int activatepot(int selectpot){

  if(selectpot == 0){

      digitalWrite(ctl,LOW);

      digitalWrite(pinA,LOW);

      digitalWrite(pinB,LOW);

  }else if(selectpot == 1){

    digitalWrite(ctl,LOW);

      digitalWrite(pinA,HIGH);

      digitalWrite(pinB,LOW);

  }else if(selectpot == 2){

    digitalWrite(ctl,LOW);

      digitalWrite(pinA,LOW);

      digitalWrite(pinB,HIGH);

  }else if(selectpot == 3){

    digitalWrite(ctl,LOW);

      digitalWrite(pinA,HIGH);

      digitalWrite(pinB,HIGH);

  }else if(selectpot == 4){

      digitalWrite(ctl2,LOW);

      digitalWrite(pinA2,LOW);

      digitalWrite(pinB2,LOW);

  }else if(selectpot == 5){

    digitalWrite(ctl2,LOW);

      digitalWrite(pinA2,HIGH);

      digitalWrite(pinB2,LOW);

  }else if(selectpot == 6){

    digitalWrite(ctl2,LOW);

      digitalWrite(pinA2,LOW);

      digitalWrite(pinB2,HIGH);

  }else if(selectpot == 7){

    digitalWrite(ctl2,LOW);

      digitalWrite(pinA2,HIGH);

      digitalWrite(pinB2,HIGH);

  }else{

    digitalWrite(ctl,HIGH);

    digitalWrite(ctl2,HIGH);

  }

  //delay(50);

}



int deactivatepot(){

  //delay(50);

   digitalWrite(ctl,HIGH);

   digitalWrite(ctl2,HIGH);

  

}



int getvoltage(int phasediff){

  int returnval;

 float temp = (.000000002*phasediff*phasediff*phasediff)+(.00003*phasediff*phasediff)+(.0118*phasediff)-.0816;

        temp = temp/.12;

        //Serial.println(temp);

       int lowval = (int) temp;

      float checklow = temp-.5;

      int check = (int) checklow;

           if(checklow < lowval){

               returnval = lowval;

           }else{

               //Serial.print("Made It");

               float rounding =  temp+.5;

               returnval = (int) rounding;

           }

           returnval=129-returnval;

           Serial.println(returnval);

           //Serial.print("Return Value: ");Serial.print(returnval);Serial.print("\n");

           return returnval;

              

 

 

}



int setpotvalue(int digipot, int potvalue){

  activatepot(digipot);

  potvalue+=90;//this makes the error smaller but still not great

  int newvolt = getvoltage(potvalue);

  shiftOut(_sdi, _sck, MSBFIRST, (newvolt>>8));

  shiftOut(_sdi, _sck, MSBFIRST, newvolt);

  //shiftOut(_sdi, _sck, MSBFIRST, lowvalue);

  deactivatepot();

}
