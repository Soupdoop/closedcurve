#Makefile for this version

main:
	javac CC2O.java ClosedCurveRunner.java CurvePoint.java ImageProcessor.java
	clang++ -O2 sorter.cpp -o sorter.exe

clean:
	-rm *.class
	-rm sorter.exe
	-rm *~
	-rm .points .spoints
	-rm ClosedCurve.jar
