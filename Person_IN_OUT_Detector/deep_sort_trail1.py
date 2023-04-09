import cv2
import numpy as np
import os
import sys
from deep_sort.deep_sort import nn_matching
from deep_sort.tools import generate_detections as gdet
from deep_sort.deep_sort.detection import Detection
from deep_sort.deep_sort.tracker import Tracker
from collections import deque
from ultralytics import YOLO

model = YOLO('yolov8n.pt')


# Set up tracking parameters
max_cosine_distance = 0.5
nn_budget = None
nms_max_overlap = 1.0
model_filename = 'model_data/mars-small128.pb'
encoder = gdet.create_box_encoder(model_filename, batch_size=1)

# Set up detection threshold
conf_threshold = 0.5

# Set up deque to store the previous positions of tracked objects
track_dict = {}
pts_dict = {}
buffer_size = 10

# Set up video capture
video_path = 'path/to/your/video/file.mp4'
cap = cv2.VideoCapture(video_path)

# Initialize deep sort
metric = nn_matching.NearestNeighborDistanceMetric("cosine", max_cosine_distance, nn_budget)
tracker = Tracker(metric)

cap = cv2.VideoCapture(r"C:\Users\prana\Desktop\filename (2).avi")

frame_width = 1920
frame_height = 1080
size = (frame_width, frame_height)
   
# Below VideoWriter object will create
# a frame of above defined The output 
# is stored in 'filename.avi' file.
processed_Image = cv2.VideoWriter('Processed_Video_filename_desktop.avi', 
                         cv2.VideoWriter_fourcc(*'MJPG'),
                         30, size)


while True:
    # Read a frame from the video
    ret, frame = cap.read()
    if not ret:
        break

    # Resize the frame
    frame = cv2.resize(frame, (1280, 720))

    # Detect objects in the frame
    detections = []

    # Run your object detection algorithm here
    # ...
    results = model(frame, iou=0.5, conf=0.1)
    detections = []
    for result in results:
        for box in result.boxes:
            if (box.cls==0):
                detections.append(box.xyxy[0].detach().numpy().astype("int"))
                # frame = cv2.rectangle(frame, (int(x1),int(y1)), (int(x2),int(y2)), (0,255,0), 2)


    for det in detections:
        # Filter out detections with low confidence
        if det[4] < conf_threshold:
            continue

        # Create a detection object and encode its features
        bbox = det[:4]
        bbox = bbox.astype(np.int32)
        features = encoder(frame, bbox)
        detection = Detection(bbox, det[4], features)

        # Update the tracker with the new detection
        tracker.predict()
        tracker.update(detection)

        # Store the object's position in the deque
        track_id = int(tracker.tracks[-1].track_id)
        if track_id not in track_dict:
            track_dict[track_id] = deque(maxlen=buffer_size)
            pts_dict[track_id] = deque(maxlen=buffer_size)
        track_dict[track_id].append(tracker.tracks[-1].bbox)
        pts_dict[track_id].append((tracker.tracks[-1].bbox[0] + tracker.tracks[-1].bbox[2] // 2, tracker.tracks[-1].bbox[1] + tracker.tracks[-1].bbox[3]))

    # Draw the tracked objects and their paths
    for track_id in track_dict:
        pts = pts_dict[track_id]
        if len(pts) > 1:
            prev_pt = pts[0]
            for pt in pts:
                cv2.line(frame, prev_pt, pt, (0, 0, 255), thickness=2)
                prev_pt = pt

        bbox = track_dict[track_id][-1]
        cv2.rectangle(frame, (bbox[0], bbox[1]), (bbox[2], bbox[3]), (255, 0, 0), 2)


    processed_Image.write(frame)
    # Display the result
    cv2.imshow('frame', frame)

    # Press 'q' to exit
    if cv2.waitKey(1) == ord('q'):
        break

# Release resources
cap.release()
processed_Image.release()

cv2.destroyAllWindows()
