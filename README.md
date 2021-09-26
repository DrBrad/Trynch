Trynch
========

Trynch is a motion / sound detection system that can run scripts in the case that the sensors are activated. A use case that you may want to use this for is in the case that you enjoy using your laptop at the coffee shop and have to go to the bathroom, you wont have to worry about anyone stealing your laptop.

If you want to test this out for yourself download the JAR here: [Trynch.jar](https://github.com/DrBrad/Trynch/blob/main/out/artifacts/Trynch_jar/Trynch.jar?raw=true)


Sensors
-----
- [x] Webcam sensors
- [x] Microphone sensors
- [x] Keyboard detection
- [x] Phone proximity detection
- [x] LockScreen detection
- [ ] Gyroscope sensor

When a sensor picks anything up you can set it to beep or shutdown your PC. With automation we can see if you are close to your computer without you having to start this every time.

Tested
-----
I have tested this and it works with:
- [x] Ubuntu
- [x] Windows
- [ ] Mac UNTESTED

Notes
-----
If your running Ubuntu you will need to install whats below if you want lockscreen detection.
*sudo apt install gnome-screensaver*


Commands
-----
Some commands you may want to try are:
*-start* will start the when you run within terminal
*-auto* will start automation the when you run within terminal

License
-----------
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR ANYONE DISTRIBUTING THE SOFTWARE BE LIABLE FOR ANY DAMAGES OR OTHER LIABILITY, WHETHER IN CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE
