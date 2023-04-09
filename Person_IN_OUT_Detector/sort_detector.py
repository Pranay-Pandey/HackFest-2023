from sort import *
import cv2
from ultralytics import YOLO



model = YOLO('yolov8n.pt')

# construct the argument parse and parse the arguments

# initialize our centroid tracker and frame dimensions
mot_tracker = Sort() 
(H, W) = (None, None)

# initialize the video stream and allow the camera sensor to warmup
print("[INFO] starting video stream...")
# vs = VideoStream(src=0).start()
cap = cv2.VideoCapture(r"filename.avi")

# video = cv2.VideoWriter('filename.avi', 
#                          cv2.VideoWriter_fourcc(*'MJPG'),
#                          10 ,(640,480))

frame_width = 1920
frame_height = 1080
size = (frame_width, frame_height)
   
# Below VideoWriter object will create
# a frame of above defined The output 
# is stored in 'filename.avi' file.
processed_Image = cv2.VideoWriter('Processed_Video_filename_40conf.avi', 
                         cv2.VideoWriter_fourcc(*'MJPG'),
                         30, size)

ret = 1
Line_Point = 1050
Line_Point2 = 1350
in_count = 0
out_count = 0
prev_positions = {}
going_left_transition = []
going_right_transition = []
# loop over the frames from the video stream
while ret:
    # read the next frame from the video stream and resize it
    ret, frame = cap.read()
    # frame = imutils.resize(frame, width=400)

    # if the frame dimensions are None, grab them
    if W is None or H is None:
        (H, W) = frame.shape[:2]

    results = model(frame, iou=0.1, conf=0.4)
    detections = []
    for result in results:
        for box in result.boxes:
            if (box.cls==0):
                x1,y1,x2,y2 = box.xyxy[0].detach().numpy().astype("int")
                score = box.conf
                detections.append([int(x1),int(y1),int(x2),int(y2),score])
                # frame = cv2.rectangle(frame, (int(x1),int(y1)), (int(x2),int(y2)), (0,255,0), 2)

    # update our centroid tracker using the computed set of bounding
    # box rectangles
    track_bbs_ids = mot_tracker.update(np.array(detections))

    # loop over the tracked objects
    
    for (a,b,c,d, objectID) in track_bbs_ids:
        bbox = [int(a),int(b),int(c),int(d)]
        # draw both the ID of the object and the centroid of the
        # object on the output frame
        centroid = (int((bbox[0]+bbox[2])/2), int((bbox[1]+bbox[3])/2))
        text = "ID {}".format(objectID)
        cv2.putText(frame, text, (centroid[0] - 10, centroid[1] - 10),
            cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
        cv2.circle(frame, (centroid[0], centroid[1]), 4, (0, 255, 0), -1)
        
        # check if the centroid has crossed the line
        if objectID in prev_positions:
            prev_position = prev_positions[objectID]
            if prev_position[0] < Line_Point and centroid[0] > Line_Point:
                going_right_transition.append(objectID)
            elif prev_position[0] > Line_Point2 and centroid[0] < Line_Point2:
                going_left_transition.append(objectID)
            elif objectID in going_right_transition:
                if prev_position[0]< Line_Point2 and centroid[0] > Line_Point2:
                    in_count += 1
                    going_right_transition.remove(objectID)
                elif prev_position[0]>Line_Point and centroid[0]<Line_Point:
                    going_right_transition.remove(objectID)
            elif objectID in going_left_transition:
                if prev_position[0]>Line_Point and centroid[0]<Line_Point:
                    out_count += 1
                    going_left_transition.remove(objectID)
                elif prev_position[0]<Line_Point2 and centroid[0]>Line_Point2:
                    going_left_transition.remove(objectID)
            prev_positions[objectID] = centroid
        else:
            prev_positions[objectID] = centroid
            
    # display in_count and out_count
    cv2.putText(frame, "In Count: {}".format(in_count), (10, 20), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
    cv2.putText(frame, "Out Count: {}".format(out_count), (10, 50), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)

    # loop over the bounding box rectangles and draw them on the frame
    for box in detections:
        (startX, startY, endX, endY, conf) = box
        cv2.rectangle(frame, (startX, startY), (endX, endY),
            (0, 255, 0), 2)
            
    # draw line to show the direction of movement
    frame = cv2.line(frame, (Line_Point, 0), (Line_Point, H), (0, 0, 255), 2)
    frame = cv2.line(frame, (Line_Point2, 0), (Line_Point2, H), (0, 0, 255), 2)

    # show the output frame
    processed_Image.write(frame)
    cv2.imshow("Frame", frame)
    key = cv2.waitKey(1) & 0xFF

    # if the `q` key was pressed, break from the loop
    if key == ord("q"):
        break

# do a bit of cleanup
cap.release()
processed_Image.release()

cv2.destroyAllWindows()

