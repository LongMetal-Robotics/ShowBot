echo "Installing pynetworktables"
pip3 install pynetworktables

echo "Installing rgbmatrix"
curl https://raw.githubusercontent.com/adafruit/Raspberry-Pi-Installer-Scripts/master/rgb-matrix.sh >rgb-matrix.sh
sudo bash rgb-matrix.sh

echo "Add this line to /etc/rc.local:"
echo "sudo -E python /home/pi/PiLED/matrix/main.py &"