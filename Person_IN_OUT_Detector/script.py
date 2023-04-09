import ultralytics
from ultralytics import YOLO
import cv2

model = YOLO('yolov8n.pt')

cap = cv2.VideoCapture(0)

CLASS_NAMES_DICT = model.model.names

while True:
    ret, frame = cap.read()
    results = model(frame, iou=0.1)
    for result in results:
        for box in result.boxes:

            
            if (box.cls==0):
                x1,y1,x2,y2 = box.xyxy[0]
                frame = cv2.rectangle(frame, (int(x1),int(y1)), (int(x2),int(y2)), (0,255,0), 2)
    cv2.imshow("frame",frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()