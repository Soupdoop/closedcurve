#ifndef POINT_H
#define POINT_H

#include <string>

class Point {
public:
	float x;
	float y;
	Point() {
		x = 0;
		y = 0;
	}
	Point(float ex, float why) {
		x = ex;
		y = why;
	}
	float distance_to(const Point & other) const {
		return (x - other.x) * (x - other.x) + (y - other.y) * (y - other.y);
	}
};

#endif
