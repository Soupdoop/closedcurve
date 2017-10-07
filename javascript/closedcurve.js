
var generatePoints = function(image) {
  var tempCanvas = document.create('canvas');
  var tempContext = tempContext.getContext("2d");
  tempContext.draw(image, 0, 0);
  var imageRGB = tempContext.getImageData(0, 0, image.width, image.height).data;

  var generatedPoints = [];

  var cumulativeDarkness = 0;

  for(int i = 0; i < image.width; i++) {
    for(int j = 0; j < image.height; j++) {
      var loc = ((j * image.width) + i) * 4;
      var r = imageRGB[loc];
      var g = imageRGB[loc + 1];
      var b = imageRGB[loc + 2];
      var a = imageRGB[loc + 3];

      var darkness = (1 - (r + g + b)/(765)) * (a / 255);
      cumulativeDarkness += darkness;
      if(cumulativeDarkness >= 1) {
        cumulativeDarkness -= 1;
        generatedPoints.push(new Point(i,j));
      }
    }
  }

  return generatedPoints;
}

//ClosedCurve Class

var SHOULD_SORT = function() {
  return true; //Could check user input for this option
}

var SHOULD_CLEAR_INTERSECTIONS = function() {
  return true; //Could check user input for this option
}

var ClosedCurve = function(image) {
  if(image != null) {
    this.points = generatedPoints(image);
    this.width = image.width;
    this.height = image.height;
    if(SHOULD_SORT()) {
      this.sortPoints(this.width, this.height);
    }
    if(SHOULD_CLEAR_INTERSECTIONS()) {
      while(this.two_opt(0) != 0);
    }
  } else {
    this.points = [];
    this.height = 0;
    this.width = 0;
  }
}

ClosedCurve.prototype.sortPoints = function(imageWidth, imageHeight) {
  this.points = this.points.sort(new function(a,b) {
    return a.sierpinski_pi(imageWidth,imageHeight) - b.sierpinski_pi(imageWidth,imageHeight);
  });
}

ClosedCurve.prototype.two_opt = function(startIndex) {
  var swapped = 0;
  for(var j = start + 3; j < this.points.length; j++) {
    if(this.points[start].DistanceSquaredTo(this.points[start + 1]) + this.points[j - 1].DistanceSquaredTo(this.points[j]) >
       this.points[start].DistanceSquaredTo(this.points[j - 1]) + this.points[start + 1].DistanceSquaredTo(this.points[j])) {
      this.swap(start + 1, j - 1);
      swapped++;
    }
  }
  if(swapped > 0) {
    return swapped + this.two_opt(start);
  } else if(points.length - start > 4) {
    return this.two_opt(start + 1);
  }
  return 0;
}

//Point Class

var SPI_DEPTH = function() {
  return 10; //Could check user input for this option
};

var Point = function(x,y) {
  this.x = x;
  this.y = y;
  this.has_cached = false;
};

Point.prototype.sierpinski_pi = function(totalWidth, totalHeight) {
  if(this.has_cached) {
    return this.cached_spi;
  }
  if(this.DistanceSquaredTo(new Point(0,0)) < this.DistanceSquaredTo(new Point(totalWidth, totalHeight))) {
    this.cached_spi = this.raw_sierpinski_pi(new CurvePoint(0,totalHeight), new CurvePoint(0,0), new CurvePoint(totalWidth,0), SPI_DEPTH(), 0);
    this.has_cached = true;
    return this.cached_spi;
  } else {
    this.cached_spi = this.raw_sierpinski_pi(new CurvePoint(0,totalHeight), new CurvePoint(totalWidth,totalHeight), new CurvePoint(totalWidth,0), SPI_DEPTH(), 1);
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