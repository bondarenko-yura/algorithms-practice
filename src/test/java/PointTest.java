import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

  @Test
  void slopeTo() {
    Point[] points = new Point[] {
        new Point(10000, 0),
        new Point(8000, 2000),
        new Point(2000, 8000),
        new Point(0, 10000),
        new Point(20000, 0),
        new Point(18000, 2000),
        new Point(2000, 18000),
        new Point(10000, 20000),
        new Point(30000, 0),
        new Point(0, 30000),
        new Point(20000, 10000),
        new Point(13000, 0),
        new Point(11000, 3000),
        new Point(5000, 12000),
        new Point(9000, 6000)
    };

    LineSegment[] bruteSeq = new BruteCollinearPoints(points.clone()).segments();
    LineSegment[] fastSeq = new FastCollinearPoints(points.clone()).segments();

    assertThat(bruteSeq.length).isEqualTo(fastSeq.length);
  }
}