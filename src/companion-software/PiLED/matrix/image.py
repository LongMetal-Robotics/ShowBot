from rgbmatrix import RGBMatrix, RGBMatrixOptions
from PIL import Image

def displayImage(image_file, matrix):
    image = Image.open(image_file)

    # Make image fit the screen
    image.thumbnail((matrix.width, matrix.height), Image.ANTIALIAS)
    matrix.SetImage(image.convert('RGB'))