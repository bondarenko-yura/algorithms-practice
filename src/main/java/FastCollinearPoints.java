import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

  private static final int MIN_TARGET_POINT_COUNT = 4;

  private final LineSegment[] segments;

  // finds all line segments containing 4 points
  public FastCollinearPoints(Point[] points) {
    validateInput(points);
    this.segments = findLineSegments(points);
  }

  // the line segments

  public int numberOfSegments() {
    return this.segments.length;
  }
  // the number of line segments

  public LineSegment[] segments() {
    return this.segments.clone();
  }

  private static void validateInput(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }

    for (Point point : points) {
      if (point == null) {
        throw new IllegalArgumentException();
      }
    }
  }

  private static LineSegment[] findLineSegments(Point[] pointsInput) {
    Point[] points = pointsInput.clone();
    Arrays.sort(points, (o1, o2) -> {
      int compare = o1.compareTo(o2);
      if (compare == 0) {
        throw new IllegalArgumentException();
      }
      return compare;
    });

    if (points.length < MIN_TARGET_POINT_COUNT) {
      return new LineSegment[0];
    }

    ArrayList<LineSegment> segments = new ArrayList<>();

    for (Point point : points) {
      Point[] curPoints = points.clone();
      Arrays.sort(curPoints, point.slopeOrder().thenComparing(Point::compareTo));

      int pointsCount = 2;
      double lastSlope = point.slopeTo(curPoints[1]);

      for (int i = pointsCount; i < curPoints.length; i++) {
        double curSlope = point.slopeTo(curPoints[i]);
        if (Double.compare(lastSlope, curSlope) == 0) {
          Point firstMatchingPoint = curPoints[i - (pointsCount - 1)];
          if (point.compareTo(firstMatchingPoint) > 0) {
            continue;
          }
          pointsCount++;
          if (pointsCount >= MIN_TARGET_POINT_COUNT && i == curPoints.length - 1) {
            segments.add(new LineSegment(point, curPoints[i]));
          }
        } else if (pointsCount >= MIN_TARGET_POINT_COUNT) {
          segments.add(new LineSegment(point, curPoints[i - 1]));
          pointsCount = 2;
        } else {
          pointsCount = 2;
        }

        lastSlope = curSlope;
      }
    }

    return segments.toArray(new LineSegment[0]);
  }
}