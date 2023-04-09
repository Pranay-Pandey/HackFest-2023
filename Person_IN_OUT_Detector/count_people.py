from pyimagesearch.centroidtracker import CentroidTracker
from imutils.video import VideoStream
import numpy as np
import argparse
import imutils
import time
import cv2
from ultralytics import YOLO

model = YOLO('yolov8n.pt')

# construct the argument parse and parse the arguments

# initialize our centroid tracker and frame dimensions
ct = CentroidTracker()
(H, W) = (None, None)

# load our serialized model from disk
net = cv2.dnn.readNetFromCaffe("deploy.prototxt", "res10_300x300_ssd_iter_140000.caffemodel")

# initialize the video stream and allow the camera sensor to warmup
print("[INFO] starting video stream...")
# vs = VideoStream(src=0).start()
cap = cv2.VideoCapture(0)

# video = cv2.VideoWriter('filename.avi', 
#                          cv2.VideoWriter_fourcc(*'MJPG'),
#                          10 ,(640,480))
time.sleep(2.0)

# loop over the frames from the video stream
while True:
	# read the next frame from the video stream and resize it
	ret, frame = cap.read()
	# frame = imutils.resize(frame, width=400)

	# if the frame dimensions are None, grab them
	if W is None or H is None:
		(H, W) = frame.shape[:2]

	# construct a blob from the frame, pass it through the network,
	# obtain our output predictions, and initialize the list of
	# bounding box rectangles
	# blob = cv2.dnn.blobFromImage(frame, 1.0, (W, H),
	# 	(104.0, 177.0, 123.0))
	# net.setInput(blob)
	# detections = net.forward()
	results = model(frame, iou=0.1)
	detections = []
	for result in results:
		for box in result.boxes:
			if (box.cls==0):

				detections.append(box.xyxy[0].detach().numpy().astype("int"))
				# frame = cv2.rectangle(frame, (int(x1),int(y1)), (int(x2),int(y2)), (0,255,0), 2)

	# update our centroid tracker using the computed set of bounding
	# box rectangles
	objects = ct.update(detections)

	# loop over the tracked objects
	for (objectID, centroid) in objects.items():
		# draw both the ID of the object and the centroid of the
		# object on the output frame
		text = "ID {}".format(objectID)
		cv2.putText(frame, text, (centroid[0] - 10, centroid[1] - 10),
			cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)
		cv2.circle(frame, (centroid[0], centroid[1]), 4, (0, 255, 0), -1)

	# loop over the bounding box rectangles and draw them on the frame
	for box in detections:
		(startX, startY, endX, endY) = box
		cv2.rectangle(frame, (startX, startY), (endX, endY),
			(0, 255, 0), 2)

	# show the output frame
	# video.write(frame)
	cv2.imshow("Frame", frame)
	key = cv2.waitKey(1) & 0xFF

	# if the `q` key was pressed, break from the loop
	if key == ord("q"):
		break

# do a bit of cleanup


cap.release()
# video.release()
cv2.destroyAllWindows()