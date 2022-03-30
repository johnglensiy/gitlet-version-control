import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author John Glen Siy
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        Node toPut = new Node(s);
        if (_root == null) {
            _root = toPut;
        }
        Node from = _root;
        Node to = _root;
        while (to != null) {
            from = to;
            to = traverse(s, from);
        }
        if (s.compareTo(from.s) >= 0) {
            from.right = toPut;
        } else {
            from.left = toPut;
        }

//        Node traverse = _root;
//        while (traverse != null) {
//            System.out.println(s.compareTo(traverse.s));
//            if (s.compareTo(traverse.s) >= 0) {
//                if (traverse.right == null) {
//                    traverse.right = toPut;
//                    traverse = null;
//                } else {
//                    traverse = traverse.right;
//                }
//            }
//            else if (s.compareTo(traverse.s) < 0) {
//                if (traverse.left == null) {
//                    traverse.left = toPut;
//                    traverse = null;
//                } else {
//                    traverse = traverse.left;
//                }
//            }
//        }
    }

    @Override
    public boolean contains(String s) {
        Node from = _root;
        Node to = _root;
        while (to != null) {
            from = to;
            if (s.equals(from.s)) {
                return true;
            }
            to = traverse(s, from);
        }
        return false;
    }

    public Node traverse(String s, Node node) {
        if (s.compareTo(node.s) >= 0) {
            return node.right;
        } else {
            return node.left;
        }
    }

    @Override
    public List<String> asList() {
        //List<String> sorted = new List<String>[];
        //BSTIterator asdf = new BSTIterator ()
        return null;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    public String getLabel(Node s) {
        if (s != null) {
            return s.s;
        }
        return null;
    }

    public Node getLeft(Node s) {
        return s.left;
    }

    public Node getRight(Node s) {
        return s.right;
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    // @Override
    public Iterator<String> iterator(String low, String high) {
        return null;  // FIXME: PART B (OPTIONAL)
    }

    Node root() {
        return _root;
    }

    /** Root node of the tree. */
    private Node _root;
}
