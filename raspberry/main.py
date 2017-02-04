#!/usr/bin/env python
# encoding: utf-8
import os, time
import threading
from threading import Thread, Lock
from sets import Set
from picamera import PiCamera
import qrtools
import requests
from apscheduler.schedulers.background import BackgroundScheduler
import logging
import sys
from RPLCD import CharLCD
import RPi.GPIO as GPIO

lcd = CharLCD(pin_rs=4, pin_rw=18, pin_e=17, pins_data=[18, 22, 23, 24], numbering_mode=GPIO.BCM, cols=16, rows=2, dotsize=8, auto_linebreaks=True)

logging.basicConfig()
syncInterval = 5.0
syncFile = 'workspace/sync.txt'

queuedMessages = Set([])
submittedMessages = Set([])

qr = qrtools.QR()
offlineMode = False
syncMutex = Lock()
lcdMutex = Lock()
lcdTextState = 0
offlineScheduler = BackgroundScheduler()
bottomLine = ""
topLine = ""

lastMessage = ""
lastMessageTime = 0

def writeTop(text):
    lcdMutex.acquire()
    text = text.ljust(16, ' ')

    global topLine
    if(topLine != text):
        topLine = text.ljust(16, ' ')
        lcd.cursor_pos = (0,0)
        lcd.write_string(topLine)

    lcdMutex.release()

def writeBottom(text):
    lcdMutex.acquire()
    text = text.ljust(16, ' ')

    global bottomLine
    if(bottomLine != text):
        bottomLine = text.ljust(16, ' ')
        lcd.cursor_pos = (1,0)
        lcd.write_string(bottomLine)

    lcdMutex.release()


def qrAnalysis(filename):
    if qr.decode(filename):
        global lastMessage
        global lastMessageTime
        if lastMessage != qr.data or (time.time() - lastMessageTime) >= 3:
            lastMessage = qr.data
            lastMessageTime = time.time()
            print "Detected QR!"
            if(topLine != "QR-Code detected"):
                writeTop("QR-Code detected")
            if qr.data not in queuedMessages and qr.data not in submittedMessages:
                print "Message read: " + qr.data
                syncMutex.acquire()
                queuedMessages.add(qr.data)
                syncMutex.release()
                syncQRCodes()
            else:
                time.sleep(1)
                if(len(queuedMessages) > 0):
                    writeTop("Ready (offline)" )
                else:
                    writeTop("Ready")
                
    os.remove(filename)

def syncQRCodes():
    try:
        syncMutex.acquire()
        submits = Set([])
        print("sync elements: " + str(len(queuedMessages)))
        writeBottom("ToSync: " + str(len(queuedMessages)))

        for key in queuedMessages:
            print("try to submit message: " + key)
            try:
                response = requests.get(key)
                print response.status_code
                if response.status_code == 200:
                    submits.add(key)
                    print "submitted: " + key
                elif response.status_code < 500:
                    queuedMessages.remove(key)
            except (requests.exceptions.ConnectionError,requests.exceptions.Timeout) as e: 
                # keep the queued message for later submit
                print "Was offline, try to submit that later again"
                break
            except: 
                print 'Message was not well formed and thereby removed from the queue'
                queuedMessages.remove(key)
                break

        for key in submits:
            queuedMessages.remove(key)
            submittedMessages.add(key)

        # if there are still not synced elements, store them local for long time storage
        if len(queuedMessages) > 0:
            if offlineScheduler.state == 2:
                offlineScheduler.resume()

            syncStorage = open(syncFile +  str(len(queuedMessages)), "w+")
            for item in queuedMessages:
                syncStorage.write("%s\n" % item)
            syncStorage.close()
        else:
            if offlineScheduler.state == 1:
                offlineScheduler.pause()
            try:
                os.remove(syncFile)
            except OSError:
                pass

        time.sleep(1)
        if(len(queuedMessages) > 0):
            writeTop("Ready (offline)" )
        else:
            writeTop("Ready")

        writeBottom("ToSync: " + str(len(queuedMessages)))
        syncMutex.release()

    except RuntimeError as e:
        print repr(e)

def _main():
    writeTop("Initialize")
    offlineScheduler.add_job(syncQRCodes, 'interval', seconds=syncInterval)
    offlineScheduler.start(paused=True)
    # initialize the camera and grab a reference to the raw camera capture
    camera = PiCamera()
    camera.resolution = (640, 480)
    camera.framerate = 60
    camera.start_preview()
    # allow the camera to warmup
    time.sleep(0.1)
    
    # Check for old messages to be synced
    if os.path.isfile(syncFile) :
        with open(syncFile) as openedFile:
            for line in openedFile:
                queuedMessages.add(line.rstrip('\n'))  
        syncQRCodes()

    startTime = time.time()
    scanCount = 0

    writeTop("Ready")
    for filename in camera.capture_continuous('workspace/scan_{counter}.jpg', use_video_port=True):
        Thread(target = qrAnalysis, args = (filename,)).start()
        currentTime = time.time()
        scanCount += 1
        #print 'Scans/s: %f' % (scanCount / (currentTime-startTime))

if __name__ == '__main__':
    _main()
