import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] testArr = new int[][] {{1}, {2}, {3}};
        assertEquals(3, MultiArr.maxValue(testArr));
    }

    @Test
    public void testAllRowSums() {
        int[][] testArr = new int[][] {{1, 2}, {3, 5}, {2, 4}};
        assertArrayEquals(new int[] {3, 8, 6}, MultiArr.allRowSums(testArr));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
