import time
def warmup(matrix):
    for i in range(255):
        matrix.Fill(i, i, i)
        time.sleep(0.005)
#    for x in range(matrix.width):
#        for y in range(matrix.height):
#           matrix.SetPixel(x, y, 255, 255, 255)
#            time.sleep(0.01)
