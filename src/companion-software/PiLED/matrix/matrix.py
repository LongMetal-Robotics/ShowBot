from rgbmatrix import RGBMatrix, RGBMatrixOptions
from video import playVideo, loopVideo
from image import displayImage

import logging
import threading
import re

class Matrix:
    currentThread = None
    threadStop = False

    def __init__(self):
        print("Matrix initialized")
        options = RGBMatrixOptions()
        options.cols = 64
        self.matrix = RGBMatrix(options = options)
    
    def stopThread(self):
        if not self.currentThread is None:
            print("Stopping running thread...")
            self.threadStop = True
            self.currentThread.join()
            self.threadStop = False
            print("Stopped thread")
    
    def display(self, mediaType, file):
        result = False
        if mediaType == "image":
            # Display image
            try:
                self.stopThread()
                print("Displaying image")
                displayImage(file, self.matrix)
                result = True
            except Exception, e:
                print("Could not display image: " + str(e))
                logging.error('Could not display image file %s. Exception: ' + str(e), file)
        elif mediaType == "video" or mediaType == "video-one":
            # Create a new thread to display
            # the video (currentThread) and run it
            try:
                print("Displaying video")
                video = open("./" + file + "/video", "r")
                videoInfo = video.read()
                print("Video info:")
                print(videoInfo)
                basename = re.search("basename \\w*", videoInfo).group()[9:] # Regex search, then get everything past `basname `
                fileext = re.search("fileext \\w*", videoInfo).group()[8:]
                frames = int(re.search("frames \\d*", videoInfo).group()[7:])
                fps = int(re.search("fps \\d*", videoInfo).group()[4:])
                print()
                print("basename: " + basename)
                print("fileext: " + fileext)
                print("frames: " + str(frames))
                print("fps: " + str(fps))
                self.stopThread()
                if mediaType == "video":
                    print("Video loops. Creating thread...")
                    self.currentThread = threading.Thread(target=loopVideo, args=(self.matrix, file, basename, fileext, frames, fps, self))
                else:
                    print("Video is one-off. Creating thread...")
                    self.currentThread = threading.Thread(target=playVideo, args=(self.matrix, file, basename, fileext, frames, fps, self))
                print("Thread created. Starting thread...")
                self.currentThread.start()
                print("Thread started")
                result = True
            except Exception, e:
                print("Could not display video: " + str(e))
                logging.error('Could not display the video. Exception: ' + str(e))
        else:
            # Blank the display
            print("Media type " + mediaType + " not recognized. Blanking screen...")
            self.stopThread()
            self.matrix.Fill(0, 0, 0)
            result = True
            print("Screen blanked")
        return result
