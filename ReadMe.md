# ClosedCurve Image Generator #

This is an extra credit project by Jack Herd (jh4yr) for CS 3102 at UVA.

The program will take in an image file, and generate a simple closed curve that approximates the look of the image.

Note that this program may run for a while! Even with many optimizations made, large inputs (either by resolution of image or by density of points) will run very slowly! You can expect runtimes of 5 - 10 minutes on images around 1000 x 1000, and run times of over an hour on larger still images.

This program works for sure on OSX, and should work on any linux machine with java installed. It may not work on windows but I dont have the ability to test it currently; if it does not work, it likely is due to the call to a C++ program, which may need to be re-written on that platform. Submit an issue or pull request if you think you have a good solution!

In order to compile, please use make with the provided makefile. If you think you know better, it's your responsibility if it breaks.

There are several parameters that can be edited in the user interface that will affect the resulting curve:

1. Input Path: This the path to the image to be read into the program. Can be local or absolute.

2. Output Path: The name of the file to write the resulting image to. Should have a .png extension.

3. Line Width: The width of the curve, in pixels. The low end of the curve (left) is 1px, the high end is 5px.

4. Base White Density: Affects point generation. Essentially, the proportion of points that fill in a 100% white space. By default, this value is 0%. It can be raised to up to 100%. Note that this is a proportion of the maximum density. Raising this will cause more of the image to be populated with points, but also will reduce contrast.

5. Maximum Density: The maximum point density. The default is 20% (one point every 5 pixels in a 100% darkened area). Increasing this will increase the number of points linearly; this will cause the program to run slower in approximately cubic time. Be careful increasing this on large images, as it may take along time for the program to run.

6. Transform Type: This decides which type of interpretation of darkness the program uses. In linear, the value of darkness (1 - average value of R, G, B) is interpreted directly as the portion of a point to be made. In cubic, the darkness value is cubed. This causes a fully white pixel (darkness == 0) to still be fully white, while a fully black pixel is fully black. However, it will decrease the difference between relatively light pixels and increase the difference between relatively dark pixels. This often results in better images if the image is largely dark.
