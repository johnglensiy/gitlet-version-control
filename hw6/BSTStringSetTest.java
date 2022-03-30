import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Test of a BST-based String Set.
 * @author
 */
public class BSTStringSetTest  {

    @Test
    public void putAtRoot() {
        BSTStringSet bss = new BSTStringSet();
        assertEquals(null, bss.root());
        bss.put("hello");
        assertEquals("hello", bss.getLabel(bss.root()));
        assertEquals(true, bss.contains("hello"));
        bss.put("bye");
        assertEquals("hello", bss.getLabel(bss.root()));
        assertEquals("bye", bss.getLabel(bss.getLeft(bss.root())));
        assertEquals(true, bss.contains("bye"));
        assertEquals(false, bss.contains("by"));
    }

    @Test
    public void testNothing() {
        // FIXME: Delete this function and add your own tests
    }
}
