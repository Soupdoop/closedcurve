#include <iostream>
#include <fstream>
#include <string>

#include "Point.h"

using namespace std;

void swap(Point ** ppointer, int start, int end) {
  Point * points = *ppointer;
  for(int i = 0; i < (end - start + 1) / 2; i++) {
    Point temp = points[start + i];
    points[start + i] = points[end - i];
    points[end - i] = temp;
  }
}

int two_opt(Point ** pointpointer, int plength) {
  Point * points = *pointpointer;
  int swapped = 0;
  for(int start = 0; start < plength; start++) {
    for(int j = start + 3; j < plength; j++) {
      if(points[start].distance_to(points[start + 1]) + points[j - 1].distance_to(points[j]) >
        points[start].distance_to(points[j - 1]) + points[start + 1].distance_to(points[j])) {
        swap(&points, start + 1, j - 1);
        swapped++;
      }
    }
  }
  return swapped;
}

int generate_from_file(char * file_name, Point ** to_fill, int * number_of_points, int * width, int * height) {
  ifstream pfile(file_name);
  if(!pfile.is_open()) {
    cout << "Invalid filename!" << endl;
    return -1;
  }
  int numPoints;
  pfile >> numPoints;
  string s;
  getline(pfile, s);
  pfile >> *width;
  getline(pfile, s);
  pfile >> *height;
  getline(pfile,s);
  s = "";
  Point * points = new Point[numPoints];
  int i = 0;
  while(pfile.good() && i < numPoints) {
    getline(pfile, s);
    float x = stof(s);
    if(pfile.good()) {
      getline(pfile, s);
      float y = stof(s);
      points[i] = Point(x,y);
    }
    i++;
  }
  *to_fill = points;
  *number_of_points = numPoints;
  return 0;
}

int write_to_file(char * file_name, Point ** pointspointer, int number_of_points, int width, int height) {
  ofstream pfile(file_name);
  if(!pfile.is_open()) {
    cout << "Invalid filename!" << endl;
    return -1;
  }
  pfile << number_of_points << endl;
  pfile << width << endl;
  pfile << height << endl;
  Point * points = *pointspointer;
  for(int i = 0; i < number_of_points; i++) {
    pfile << points[i].x << endl;
    pfile << points[i].y << endl;
  }
  return 0;
}

int main(int argc, char ** argv) {
  if(argc < 3) {
    cout << "Please give a filename parameter!" << endl;
  }
  Point * points;
  int number_of_points, width, height;
  int read_status = generate_from_file(argv[1], &points, &number_of_points, &width, &height);
  if(read_status == -1) {
    return -1;
  }
  while(two_opt(&points, number_of_points) > 0);
  int write_status = write_to_file(argv[2], &points, number_of_points, width, height);
  if(write_status == -1) {
    return -1;
  }
  return 0;
}
