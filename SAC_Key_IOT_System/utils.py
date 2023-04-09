import RPi.GPIO as GPIO
import time

class servo():
     
    def __init__(self,pin, frequency=50):
        self.servoPin = pin

        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.servoPin,GPIO.OUT)
        GPIO.setwarnings(False)

        self.pwm = GPIO.PWM(self.servoPin,frequency)
        self.pwm.start(2.5)

    def setAngle(self,angle):
        duty = angle / 18 + 3
        self.pwm.ChangeDutyCycle(duty)
        time.sleep(1)


class keypad():
     
    def __init__(self, L, C):
         
        self.L1 = L[0]
        self.L2 = L[1]
        self.L3 = L[2]
        self.L4 = L[3]
        
        self.C1 = C[0]
        self.C2 = C[1]
        self.C3 = C[2]
        self.C4 = C[3]

        self.keys = [['1', '2', '3', 'J'],
                     ['4', '5', '6', 'E'],
                     ['7', '8', '9', 'C'],
                     ['*', '0', '#', 'D']]
        
        GPIO.setwarnings(False)
        GPIO.setmode(GPIO.BCM)
        
        GPIO.setup(self.L1, GPIO.OUT)
        GPIO.setup(self.L2, GPIO.OUT)
        GPIO.setup(self.L3, GPIO.OUT)
        GPIO.setup(self.L4, GPIO.OUT)
        
        GPIO.setup(self.C1, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
        GPIO.setup(self.C2, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
        GPIO.setup(self.C3, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
        GPIO.setup(self.C4, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
        
        self.tmp = []
    
    def readLine(self,line, characters):
       
       GPIO.output(line, GPIO.HIGH)
       
       if(GPIO.input(self.C1) == 1):
           self.tmp.append(characters[0])
       if(GPIO.input(self.C2) == 1):
           self.tmp.append(characters[1])
       if(GPIO.input(self.C3) == 1):
           self.tmp.append(characters[2])
       if(GPIO.input(self.C4) == 1):
           self.tmp.append(characters[3])
       
       GPIO.output(line, GPIO.LOW)
       
    def grabKey(self):
       for i in range(1,5):
           self.readLine(getattr(self,f"L{i}"), self.keys[i-1])