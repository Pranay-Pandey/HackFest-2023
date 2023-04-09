from utils import keypad, servo
import RPi.GPIO as GPIO
import time
import sys

import firebase_admin
from firebase_admin import credentials, firestore

timestamp=time.time()
asctime=time.ctime(timestamp)

cred = credentials.Certificate("serviceAccountKey.json")
firebase_admin.initialize_app(cred)

L = [5,6,13,19]
C = [12,16,20,21]
ServoPin=18

key = keypad(L,C)
motor=servo(ServoPin)

motor.setAngle(0)

attempt=[]
adm_no=[]
count=0
ok=0

try:
    # For Taking Admission No
    while True:
        key.grabKey()

        if len(key.tmp) > 0 and len(adm_no)<8:
            keys=['1', '2', '3', 'J', 
                  '4', '5', '6', 'E', 
                  '7', '8', '9', 'C', 
                  '*', '0', '#', 'D']
        
            if(key.tmp[0] in keys):
                time.sleep(1)
                adm_no.append(key.tmp[0])
                ok=1
	    
        key.tmp = []
        count=count+1

        if (len(adm_no) in [1, 2, 3, 4, 5, 6, 7, 8]) and ok==1:
            print(adm_no)
            ok=0

        if(len(adm_no)==8):
            break
    
    adm_no_string=''
    for i in range(len(adm_no)):
        adm_no_string+=adm_no[i]

    #Taking THE Code from Firebase
    db = firestore.client()
    doc_ref = db.collection(u'SAC_users').document(adm_no_string)

    doc = doc_ref.get()
    if doc.exists:
        THE_CODE = doc.to_dict()['random_code']
        THE_ROOM = doc.to_dict()['room_no']
        print("The Code: "+str(THE_CODE))
        print("The Room: "+str(THE_ROOM))
    else:
        print("Room Not Booked")
        sys.exit

    THE_CODE=[i for i in THE_CODE]

    count=0
    ok=0
    # For The Code Verification
    while True:
        key.grabKey()

        if len(key.tmp) > 0 and len(attempt)<2:
            keys=['1', '2', '3', 'J', 
                  '4', '5', '6', 'E', 
                  '7', '8', '9', 'C', 
                  '*', '0', '#', 'D']
            if(key.tmp[0] in keys):
                time.sleep(1)
                attempt.append(key.tmp[0])
                ok=1

        key.tmp = []
        count=count+1

        if (len(attempt) in [1, 2]) and ok==1:
            print(attempt)
            ok=0
        if(len(attempt)==2):
            break
    
    if(attempt==THE_CODE):
        print('Password Matched')
        motor.setAngle(170)
        
        motor.setAngle(0)
        db = firestore.client()
        
        city = {
        u'admission_no':adm_no_string,
        u'checkout_time': u'',
        u'in_time': u'',
        u'room_no': THE_ROOM, 
        u'status': u'0',
        u'time':asctime
        }
        update_time, city_ref = db.collection(u'SAC').add(city)
        # print(f'Added Document with id{city_ref.id}')
        print("Updated to Firebase")

        time.sleep(3)
        db.collection(u'SAC_users').document(adm_no_string).delete()
    
    else:
        print("Wrong Password")

    attempt=[]
    adm_no=[]

except KeyboardInterrupt:
    GPIO.cleanup()
