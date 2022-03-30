import java.util.List;

import java.util.LinkedList;

/** A set of String values.
 *  @author John Glen Siy
 */
class ECHashStringSet implements StringSet {

    private LinkedList<String>[] buckets;

    private int _bucketCount = 5;

    public ECHashStringSet() {
        buckets = (LinkedList<String>[]) new LinkedList[_bucketCount];
        for (int i = 0; i < _bucketCount; i++) {
            buckets[i] = new LinkedList<String>();
        }
    }

    @Override
    public void put(String s) {
        buckets[filter(s)].add(s);
    }

    @Override
    public boolean contains(String s) {
        return buckets[filter(s)].contains(s);
    }

    public int filter(String s) {
        return (s.hashCode() & 0x7fffffff) % _bucketCount;
    }
    @Override
    public List<String> asList() {
        return null; // FIXME
    }
}
