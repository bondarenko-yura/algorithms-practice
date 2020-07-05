import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

  private final LineSegment[] segments;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
    validateInput(points);
    this.segments = findLineSegments(points);
  }

  // the line segments

  public int numberOfSegments() {
    return this.segments.length;
  }
  // the number of line segments

  public LineSegment[] segments() {
    return Arrays.copyOf(this.segments, this.segments.length);
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

    if (points.length < 4) {
      return new LineSegment[0];
    }

    List<LineSegment> segments = new ArrayList<>();

    for (int i = 0; i < points.length; i++) {
      for (int j = i + 1; j < points.length; j++) {
        double slopeJ = points[i].slopeTo(points[j]);
        for (int k = j + 1; k < points.length; k++) {
          double slopeK = points[i].slopeTo(points[k]);
          if (Double.compare(slopeJ, slopeK) != 0) {
            continue;
          }
          for (int q = k + 1; q < points.length; q++) {
            double slopeQ = points[i].slopeTo(points[q]);
            if (Double.compare(slopeJ, slopeQ) == 0) {
              segments.add(new LineSegment(points[i], points[q]));
            }
          }
        }
      }
    }

    return segments.toArray(new LineSegment[0]);
  }
}