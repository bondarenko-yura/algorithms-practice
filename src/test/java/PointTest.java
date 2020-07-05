import com.bondarenko.algo.sort.collinear.BruteCollinearPoints;
import com.bondarenko.algo.sort.collinear.FastCollinearPoints;
import com.bondarenko.algo.sort.collinear.LineSegment;
import com.bondarenko.algo.sort.collinear.Point;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointTest {

  @Test
  void slopeTo() {
    Point[] points = new Point[] {
        new Point( 4000, 30000),
        new Point( 3500, 28000),
        new Point( 3000, 26000),
        new Point( 2000, 22000),
        new Point( 1000, 18000),
        new Point(13000, 21000),
        new Point(23000, 16000),
        new Point(28000, 13500),
        new Point(28000, 5000),
        new Point(28000, 1000)
    };

    LineSegment[] bruteSeq = new BruteCollinearPoints(points.clone()).segments();
    LineSegment[] fastSeq = new FastCollinearPoints(points.clone()).segments();

    assertThat(bruteSeq.length).isEqualTo(fastSeq.length);
  }
}