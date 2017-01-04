#!/usr/bin/env python
# encoding: utf-8

import os, time
from threading import Thread, Lock
from sets import Set
from picamera import PiCamera
import qrtools
import requests
from apscheduler.schedulers.background import BackgroundScheduler
import logging
import sys
logging.basicConfig()

syncInterval = 5.0
syncFile = 'workspace/sync.txt'

queuedMessages = Set([])
submittedMessages = Set([])

qr = qrtools.QR()
offlineMode = False
syncMutex = Lock()
offlineScheduler = BackgroundScheduler()

def qrAnalysis(filename):
    if qr.decode(filename):
        print "Detected QR!"
        if qr.data not in queuedMessages and qr.data not in submittedMessages:
            print "Message read: " + qr.data
            syncMutex.acquire()
            queuedMessages.add(qr.data)
            syncMutex.release()
            syncQRCodes()
    os.remove(filename)

def syncQRCodes():
    try:
        syncMutex.acquire()
        submits = Set([])
        print("sync elements: " + str(len(queuedMessages)))

        for key in queuedMessages:
            print("try to submit message: " + key)
            try:
                response = requests.get("http://www.google.de")
                if response.status_code == 200:
                    submits.add(key)
                    print "submitted: " + key
            except requests.exceptions.RequestException as e: 
                break

        for key in submits:
            queuedMessages.remove(key)
            submittedMessages.add(key)

        # if there are still not synced elements, store them local for long time storage
        if len(queuedMessages) > 0:
            if offlineScheduler.state == 2:
                offlineScheduler.resume()

            syncStorage = open(syncFile, "w+")
            for item in queuedMessages:
                syncStorage.write("%s\n" % item)
        else:
            if offlineScheduler.state == 1:
                offlineScheduler.pause()
            try:
                os.remove(syncFile)
            except OSError:
                pass
        syncMutex.release()

    except RuntimeError as e:
        print repr(e)

def _main():
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
    for filename in camera.capture_continuous('workspace/scan_{counter}.jpg', use_video_port=True):
        Thread(target = qrAnalysis, args = (filename,)).start()
        currentTime = time.time()
        scanCount += 1
        #print 'Scans/s: %f' % (scanCount / (currentTime-startTime))


if __name__ == '__main__':
    _main()