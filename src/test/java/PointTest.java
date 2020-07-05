import edu.princeton.cs.algs4.StdOut;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

  @Test
  void slopeTo() {
    Point a = new Point(1, 1);
    Point b = new Point(2, 2);
    Point c = new Point(3, 3);
    Point d = new Point(4, 4);
    Point e = new Point(6, 18);

    Point[] points = new Point[] {a, b, c, d, e};
    Arrays.sort(points, Comparator.reverseOrder());

    StdOut.println(Arrays.toString(points));

    new FastCollinearPoints(points);

  }
}