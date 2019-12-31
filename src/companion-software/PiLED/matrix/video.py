import time
from PIL import Image

def loopVideo(matrix, folder, basename, fileext, frames, fps, callingMatrix):
    while not callingMatrix.threadStop:
       playVideo(matrix, folder, basename, fileext, frames, fps, callingMatrix)

def playVideo(matrix, folder, basename, fileext, frames, fps, callingMatrix):
    for frame in range(frames):
         if not callingMatrix.threadStop:
            image = Image.open(folder + "/" + basename + "{:04d}".format(frame + 1) + "." + fileext)

            image.thumbnail((matrix.width, matrix.height), Image.ANTIALIAS)
            matrix.SetImage(image.convert('RGB'))
            time.sleep(1.0/fps)
