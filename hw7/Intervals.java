import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/** HW #7, Sorting ranges.
 *  @ John Glen Siy
  */
public class Intervals {
    /** Assuming that INTERVALS contains two-element arrays of integers,
     *  <x,y> with x <= y, representing intervals of ints, this returns the
     *  total length covered by the union of the intervals. */
    public static int coveredLength(List<int[]> intervals) {
        intervals.sort((a, b) -> a[0] - b[0]);
        ArrayList<int[]> sorted = new ArrayList<int[]>();
        int sum = 0;
        for (int i = 0; i < intervals.size(); i++) {
            if (sorted.isEmpty()) {
                sorted.add(intervals.get(i));
            }
            int[] curr = sorted.get(sorted.size() - 1);
            int[] ins = intervals.get(i);
            if (ins[0] > curr[1]) {
                sum += intervalLength(sorted.get(sorted.size() - 1));
                sorted.add(intervals.get(i));
            } else {
                curr[1] = Integer.max(curr[1], ins[1]);
            }
        }
        return sum + intervalLength(sorted.get(sorted.size() - 1));
    }

    public static int intervalLength (int[] a) {
        return a[1] - a[0];
    }

    /** Test intervals. */
    static final int[][] INTERVALS = {
        {19, 30}, {8, 15}, {3, 10}, {6, 12}, {4, 5},
    };
    /** Covered length of INTERVALS. */
    static final int CORRECT = 23;

    /** Performs a basic functionality test on the coveredLength method. */
    @Test
    public void basicTest() {
        assertEquals(CORRECT, coveredLength(Arrays.asList(INTERVALS)));
    }

    /** Runs provided JUnit test. ARGS is ignored. */
    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(Intervals.class));
    }

}
