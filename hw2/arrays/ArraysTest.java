package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author John Glen Siy
 */
public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] nullArray = {};
        int[] array1 = {1, 2, 3};
        int[] array2 = {4, 5};
        assertArrayEquals(new int[] {1, 2, 3, 4, 5},
                Arrays.catenate(array1, array2));
        assertArrayEquals(new int[] {1, 2, 3},
                Arrays.catenate(array1, nullArray));
        assertArrayEquals(new int[] {1, 2, 3},
                Arrays.catenate(nullArray, array1));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
