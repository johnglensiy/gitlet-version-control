package enigma;

import static enigma.EnigmaException.*;
import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author John Glen Siy
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new ArrayList<String>();
        permInit(cycles);
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void permInit(String cycles) {
        String cycleSoFar = "";
        for (char c : cycles.toCharArray()) {
            if (c == ')') {
                _cycles.add(cycleSoFar);
                cycleSoFar = "";
            }
            else if (c != '(' && c != ' ') {
                cycleSoFar += c;
            }
        }
    }

    private void addCycle(String cycle) {
        _cycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        for (String cycle : _cycles) {
            int cLength = cycle.length();
            for (int i = 0; i < cLength; i++) {
                if (cycle.charAt(i) == p) {
                    return cycle.charAt((i + 1) % cLength);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (String cycle : _cycles) {
            int cLength = cycle.length();
            for (int i = 0; i < cLength; i++) {
                if (cycle.charAt(i) == c) {
                    return cycle.charAt((i - 1) % cLength);
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return permute(_alphabet.toChar(p));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return permute(_alphabet.toChar(c));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return true;  // FIXME
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    private ArrayList<String> _cycles;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED
}
