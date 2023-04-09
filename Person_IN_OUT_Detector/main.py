import os
import random

import cv2
from ultralytics import YOLO

from tracker import Tracker


video_path = "/home/aniket0128/Desktop/programs/python/Hackfest/filename.avi"

cap = cv2.VideoCapture(video_path)
ret, frame = cap.read()

# cap_out = cv2.VideoWriter(video_out_path, cv2.VideoWriter_fourcc(*'MP4V'), cap.get(cv2.CAP_PROP_FPS),
#                           (frame.shape[1], frame.shape[0]))

model = YOLO("yolov8n.pt")

tracker = Tracker()

colors = [(random.randint(0, 255), random.randint(0, 255), random.randint(0, 255)) for j in range(10)]

frame_width = 1920
frame_height = 1080
size = (frame_width, frame_height)

processed_Image = cv2.VideoWriter('Processed_Video_filename_desktop.avi', 
                         cv2.VideoWriter_fourcc(*'MJPG'),
                         30, size)

Line_Point = 1050
Line_Point2 = 1350
in_count = 0
out_count = 0
prev_positions = {}
going_left_transition = []
going_right_transition = []

detection_threshold = 0.5
while ret:

    results = model(frame)

    for result in results:
        detections = []
        for r in result.boxes.data.tolist():
            x1, y1, x2, y2, score, class_id = r
            x1 = int(x1)
            x2 = int(x2)
            y1 = int(y1)
            y2 = int(y2)
            class_id = int(class_id)
            if score > detection_threshold and class_id == 0:
                detections.append([x1, y1, x2, y2, score])

        tracker.update(frame, detections)

        for track in tracker.tracks:
            bbox = track.bbox
            x1, y1, x2, y2 = bbox
            track_id = track.track_id
            centroid = [int((x1 + x2)/2)]

            if track_id in prev_positions:
                prev_position = prev_positions[track_id]
                if prev_position[0] > Line_Point2 and centroid[0] < Line_Point2:
                    going_left_transition.append(track_id)
                elif prev_position[0] < Line_Point and centroid[0] > Line_Point:
                    going_right_transition.append(track_id)
                
                elif track_id in going_right_transition:
                    if prev_position[0]< Line_Point2 and centroid[0] > Line_Point2:
                        in_count += 1
                        going_right_transition.remove(track_id)
                    elif prev_position[0]>Line_Point and centroid[0]<Line_Point:
                        going_right_transition.remove(track_id)
                elif track_id in going_left_transition:
                    if prev_position[0]>Line_Point and centroid[0]<Line_Point:
                        out_count += 1
                        going_left_transition.remove(track_id)
                    elif prev_position[0]<Line_Point2 and centroid[0]>Line_Point2:
                        going_left_transition.remove(track_id)
                prev_positions[track_id] = centroid
            else:
                prev_positions[track_id] = centroid
            
    # display in_count and out_count
            cv2.putText(frame, "In Count: {}".format(in_count), (10, 20), cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 255, 0), 2)
            cv2.putText(frame, "Out Count: {}".format(out_count), (10, 50), cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 0, 255), 2)

            cv2.rectangle(frame, (int(x1), int(y1)), (int(x2), int(y2)), (colors[track_id % len(colors)]), 3)
            cv2.putText(frame, f"iD{track_id}", (int((x1 + x2)/2), int((y1 + y2)/2)), cv2.FONT_HERSHEY_COMPLEX, 1.0, (colors[track_id % len(colors)]), 2)

    cv2.imshow("window", frame)
    if cv2.waitKey(1) & 0xff == ord("q"):
        break

    processed_Image.write(frame)
    ret, frame = cap.read()

cap.release()
processed_Image.release()
cv2.destroyAllWindows()
