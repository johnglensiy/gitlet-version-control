package enigma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author John Glen Siy
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors.toArray();
        _rotorSet = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotorSet[k];
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        int i = 0;
        for (String rName: rotors){
            for (Object rotor: _allRotors) {
                Rotor rotorCast = (Rotor) rotor;
                if (rotorCast.name().equals(rName)) {
                    _rotorSet[i] = (Rotor) rotor;
                }
            }
            i++;
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotorSet[i + 1].set(setting.charAt(i));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        for (int i = 1; i < _numRotors; i++) {
            boolean isLast = (i == _numRotors - 1);
            if (isLast || _rotorSet[i + 1].atNotch()) {
                _rotorSet[i].advance();
                if (i < _numRotors - 1) {
                    _rotorSet[i + 1].advance();
                    i++;
                }
            }
        }
        //printSettings();
        /***
        ArrayList<Rotor> setToAdvance = new ArrayList<Rotor>();
        for (int i = 1; i < _numRotors; i++) {
            if (_rotorSet[i].atNotch()) {
                if (!setToAdvance.contains(_rotorSet[i - 1])) {
                    setToAdvance.add(_rotorSet[i - 1]);
                }
                setToAdvance.add(_rotorSet[i]);
            }
        }
        if (!setToAdvance.contains(_rotorSet[_numRotors - 1])) {
            setToAdvance.add(_rotorSet[_numRotors - 1]);
        }
        for (int i = 0; i < _numRotors; i++) {
            if (setToAdvance.contains(_rotorSet[i])) {
                _rotorSet[i].advance();
            }
            System.out.print(_rotorSet[i].setting() + " ");
        }
        System.out.println();
        **/
    }

    private void printSettings() {
        for (int i = 0; i < _numRotors; i++) {
            System.out.print(_rotorSet[i].setting() + " ");
        }
        System.out.println();
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        int result = c;
        for (int i = _numRotors - 1; i >= 0; i--) {
            result = _rotorSet[i].convertForward(result);
        }
        for (int i = 1; i < _numRotors; i++) {
            result = _rotorSet[i].convertBackward(result);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (char c: msg.toCharArray()) {
             result += alphabet().toChar(convert(alphabet().toInt(c)));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    private int _numRotors, _pawls;

    private Object[] _allRotors;

    private Rotor[] _rotorSet;

    private Permutation _plugboard;

}
