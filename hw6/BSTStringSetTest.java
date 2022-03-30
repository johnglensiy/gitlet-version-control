import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

import java.util.ArrayList;

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
    public void asListInOrder() {
        BSTStringSet bss = new BSTStringSet();
        String[] items = {"audrey", "harjot", "jose", "nathan"};
        for (int i = 0; i < items.length; i++) {
            bss.put(items[i]);
        }
        assertEquals("audrey", bss.getLabel(bss.root()));
        assertEquals("harjot", bss.getLabel(bss.getRight(bss.root())));
        ArrayList sorted = (ArrayList) bss.asList();
        for (int i = 0; i < sorted.size(); i++) {
            assertEquals(sorted.get(i), items[i]);
        }
    }

    @Test
    public void asListOutOfOrder() {
        BSTStringSet bss = new BSTStringSet();
        String[] items = {"nathan", "harjot", "jose", "audrey"};
        String[] sortedItems = {"audrey", "harjot", "jose", "nathan"};
        for (int i = 0; i < items.length; i++) {
            bss.put(items[i]);
        }
        assertEquals("nathan", bss.getLabel(bss.root()));
        assertEquals("harjot", bss.getLabel(bss.getLeft(bss.root())));
        ArrayList sorted = (ArrayList) bss.asList();
        for (int i = 0; i < sorted.size(); i++) {
            //System.out.println(sorted.get(i));
            assertEquals(sorted.get(i), sortedItems[i]);
        }
    }

    @Test
    public void testNothing() {
        // FIXME: Delete this function and add your own tests
    }
}
