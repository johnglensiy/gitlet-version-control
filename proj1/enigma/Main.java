package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author John Glen Siy
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine mach = readConfig();
        while (_input.hasNext()) {
            String nextLine = _input.nextLine();
            if (nextLine.charAt(0) == '*') {
                setUp(mach, nextLine);
            }
            else {
                String parsedLine = mach.convert(nextLine.replaceAll(" ", ""));
                if (nextLine.isEmpty()) {
                    _output.println();
                } else {
                    printMessageLine(parsedLine);
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();
            while (_config.hasNext()) {
                _configRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, _configRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String name = _config.next();
            String rotorType = _config.next();
            String perm = "";
            while (_config.hasNext("\\([\\p{ASCII}&&[^*()]]+\\)")) {
                perm += _config.next() + " ";
            }
            if (rotorType.charAt(0) == 'M') {
                return new MovingRotor(name, new Permutation(perm, _alphabet), rotorType.substring(1));
            } else if (rotorType.charAt(0) == 'N') {
                return new FixedRotor(name, new Permutation(perm, _alphabet));
            } else {
                return new Reflector(name, new Permutation(perm, _alphabet));
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] setArgs = settings.split(" ");
        String[] rotors = new String[M.numRotors()];
        int tracker = 0;
        if (setArgs.length - 1 < M.numRotors()) {
            throw new EnigmaException("Not enough rotor arguments");
        }
        for (int i = 1; i < M.numRotors() + 1; i++) {
            rotors[i - 1] = setArgs[i];
            tracker++;
        }
        M.insertRotors(rotors);
        M.setRotors(setArgs[M.numRotors() + 1]);
        tracker++;
        String perm = "";
        for (int i = tracker + 1; i < setArgs.length; i++) {
            perm = perm.concat(setArgs[i] + " ");
        }
        M.setPlugboard(new Permutation(perm, _alphabet));
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            int rest = msg.length() - i;
            if (rest <= 5) {
                _output.println(msg.substring(i, i + rest));
            } else {
                _output.print(msg.substring(i, i + 5) + " ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    private ArrayList<Rotor> _configRotors = new ArrayList<Rotor>();

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
