import numpy as np
import cv2
import time
from PIL import Image
from edgetpu.detection.engine import DetectionEngine
import RPi.GPIO as GPIO
from bluetooth import *

#bluetooth setting
uuid = "00001101-0000-1000-8000-00805f9b34fb"

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(('', PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

advertise_service( server_sock, "BtChat",
			service_id=uuid,service_classes= [uuid, SERIAL_PORT_CLASS],
			profiles= [SERIAL_PORT_PROFILE])
print("Waiting for connenction : channel %d" %port)

client_sock, client_info = server_sock.accept()
print('accept',client_info)

#GPIO setting
GPIO.setmode(GPIO.BCM)
#GPIO.setup(6, GPIO.OUT)
GPIO.setup(21, GPIO.IN, pull_up_down=GPIO.PUD_UP)

model = "mobilenet_ssd_v2_coco_quant_postprocess_edgetpu.tflite"
label_path = "coco_labels.txt"
# creating DetectionEngine with model
engine = DetectionEngine(model)

labels = {}
box_colors = {}
prevTime = 0
font=cv2.FONT_HERSHEY_PLAIN

# reading label file
with open(label_path, 'r') as f:
	lines = f.readlines()
	for line in lines:    # ex) '87 teddy bear'
        	id, name = line.strip().split(maxsplit=1)   # ex) '87', 'teddy bear'
        	labels[int(id)] = name
print(f"Model loaded({model}) \nTrained object({len(labels)}):\n{labels.values()}")
print("Quit to ESC.")


#line_slope
def region_of_interest(img, vertices ,color3=(255,255,255), color1=255):
    mask = np.zeros_like(img) 
    if len(img.shape) >2: 
        color = color3
    else: 
        color = color1

    cv2.fillPoly(mask, vertices , color)
    ROI_image=cv2.bitwise_and(img, mask)
    return ROI_image

def draw_lines(img, lines, color=[0,255,255], thickness=2):
    global repeat
    count = 0
    for line in lines:
        count += len(line)
        for x1, y1, x2, y2 in line:
            cv2.line(img, (x1, y1), (x2,y2) ,color, thickness)
    if count>=7:
#        send_msg(repeat)
        repeat=True
    else:
        repeat=False

def send_msg(bool):
    repeat=bool
    if(not repeat):
        print("there is stairs in front of you")
        sendMsg="stairs".encode()+b'\n'
        client_sock.send(sendMsg)

def hough_lines(img, rho, theta, threshold, min_line_len, max_line_gap):
    lines= cv2.HoughLinesP(img, rho, theta, threshold , np.array([]), min_line_len, max_line_gap)
    return lines

def weighted_img(img, initial_img , alpa=1, beta=1., ramda=0.):
    return cv2.addWeighted(initial_img, alpa, img , beta, ramda)

def slope(image):
    height, width = image.shape[:2]
    gray_img = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    
    blur_img = cv2.GaussianBlur(gray_img, (3, 3) , 0)
        
    canny_img =cv2.Canny(blur_img, 70, 210)
    vertices = np.array([[(50,height), (width/8, 140), (width*7/8, 140), (width-50, height)]] , dtype=np.int32)
    ROI_img = region_of_interest(canny_img, vertices)
    
    line_arr = hough_lines(ROI_img, 1, 1 * np.pi/180, 130, 300, 200)
    line_arr = np.squeeze(line_arr)
    if((line_arr!=None).any()):
        
        b=len(line_arr)
        
        try:
            a=len(line_arr[0])
            
        except:
            line_arr=np.expand_dims(line_arr, axis=0)
            a=len(line_arr[0])

        slope_degree = (np.arctan2(line_arr[:,1] - line_arr[:,3], line_arr[:,0] - line_arr[:,2]) * 180) / np.pi
        line_arr = line_arr[np.abs(slope_degree)>167]
        slope_degree = slope_degree[np.abs(slope_degree)>167]

        line_arr = line_arr[(slope_degree>0),:], line_arr[(slope_degree<0),:]
        temp = np.zeros((image.shape[0], image.shape[1], 3), dtype=np.uint8)
        draw_lines(temp,line_arr)

        result = weighted_img(temp, image)
        cv2.imshow('hough_img', result)
    else:
        cv2.imshow('hough_img', image)


cap = cv2.VideoCapture(0)

rute=0
repeat=False

try:
	while( cap.isOpened()):
		detect=''
		inputIO = GPIO.input(21)

		ret,frame = cap.read()
		if not ret:
			print("cannot read frame")
			break
		img=frame[:,:,:: -1].copy()
		img=Image.fromarray(img)

		#line_slope
		if(rute > 25):
#			slope(frame)
			rute=0
		rute+=1
		
		candidates = engine.detect_with_image(img, threshold=0.5, top_k=5, keep_aspect_ratio=True, relative_coord=False, )
		
		
		if candidates:
			for obj in  candidates:
				if obj.label_id in box_colors:
					box_color = box_colors[obj.label_id]
				else:
					box_color=[int(j) for j in np.random.randint(0,255,3)]
					box_colors[obj.label_id]=box_color

				box_left, box_top, box_right, box_bottom= tuple(map(int, obj.bounding_box.ravel()))
				cv2.rectangle(frame, (box_left, box_top) , (box_right, box_bottom), box_color, 2)
				detect += labels[obj.label_id]+","
				accuracy = int(obj.score * 100)
				label_text = labels[obj.label_id] + "(" + str(accuracy)+")"
				(txt_w, txt_h), base = cv2.getTextSize(label_text, font, 2,3)
				cv2.rectangle(frame, (box_left -1, box_top - txt_h), (box_left + txt_w, box_top+txt_h) , box_color , -1)
				cv2.putText(frame, label_text ,(box_left, box_top +base) , font , 2, (255,255,255), 2)

#bluetooth receive
#		try:
#			data=client_sock.recv(1024)
#			if len(data) ==0: break
#			print("received [%s]" %data)
#			print("send re:[%s]" %detect)
#			sendMsg= detect.encode('ascii')
#			client_sock.send(sendMsg)
#		except IOError:
#			print("disconnected")
#			client_sock.close()
#			server_sock.close()
#			print("all done")
#			break

		if inputIO == False:
			print("Accepted connected from" , client_info)
#			GPIO.output(6,GPIO.HIGH)

			try:
				print(detect)
				if detect == " ":
					slient_sock.send(b'nothing \n')
				sendMsg=detect.encode() + b'\n'
				client_sock.send(sendMsg)
			except IOError:
				print("disconnected")
				client_sock.close()
				server_sock.close()
				print("all done")
				break
			except KeyboardInterrupt:
				print("disconnected")
				client_sock.close()
				server_sock.close()
				print("all done")
				break
			time.sleep(0.2)

#		else:
#			GPIO.output(6,GPIO.LOW)
#			print('button out')
			
		
		
		currTime=time.time()
		fps = 1/ (currTime - prevTime)
		prevTime = currTime
		cv2.putText(frame, "fps:%.1f"%fps , (10,30), font, 2,(0,255,0) ,2)
		cv2.imshow("Object Detection on buton", frame)
		if cv2.waitKey(1) &0xFF == 27:
			break
except KeyboardInterrupt:
	GPIO.cleanup
cap.release()

