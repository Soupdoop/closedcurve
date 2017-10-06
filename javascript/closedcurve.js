

//Point Class

var SPI_DEPTH = function() {
  return 10; //Can be retrieved from user input later
};

var Point = function(x,y) {
  this.x = x;
  this.y = y;
  this.has_cached = false;
};

Point.prototype.sierpinski_pi = function() {
  if(this.has_cached) {
    return this.cached_spi;
  }
  if(this.DistanceSquaredTo(new Point(0,0)) < this.DistanceSquaredTo(new Point(1,1))) {
    this.cached_spi = this.raw_sierpinski_pi(new CurvePoint(0,1), new CurvePoint(0,0), new CurvePoint(1,0), SPI_DEPTH(), 0);
    this.has_cached = true;
    return this.cached_spi;
  } else {
    this.cached_spi = this.raw_sierpinski_pi(new CurvePoint(0,1), new CurvePoint(1,1), new CurvePoint(1,0), SPI_DEPTH(), 1);
    this.has_cached = true;
    return this.cached_spi;
  }
};

Point.prototype.raw_sierpinski_pi = function(A, B, C, depth, code) {
  if(depth <= 0) {
    return code;
  }
  if(this.DistanceSquaredTo(A) < this.DistanceSquaredTo(C)) {
    return this.raw_sierpinski_pi(A, A.average(C), B, depth - 1, code * 2);
  } else {
    return this.raw_sierpinski_pi(B, A.average(C), C, depth - 1, code * 2 + 1);
  }
};

Point.prototype.DistanceSquaredTo = function(otherPoint) {
  return Math.pow(this.x - otherPoint.x, 2) + Math.pow(this.y - otherPoint.y, 2);
};

Point.prototype.average = function(otherPoint) {
  return new Point((this.x + otherPoint.x) / 2, (this.y + otherPoint.y) / 2);
};