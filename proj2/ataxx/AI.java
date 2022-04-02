/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.Random;

import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

import java.util.ArrayList;

/** A Player that computes its own moves.
 *  @author John Glen Siy
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 2;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        /*
        for (int i = 0; i < Board.SIDE; i++) {
            for (int j = 0; j < Board.SIDE; j++) {
                for (int m = 0; m < Board.SIDE; m++) {
                    for (int n = 0; n < Board.SIDE; n++) {
                        if (b.legalMove((char) (i + 'a'), (char) (j + '1'),
                                (char) (m + 'a'), (char) (n + '1'))) {
                            legalMoves.add(Move.move((char) (i + 'a'), (char) (j + '1'),
                                    (char) (m + 'a'), (char) (n + '1')));
                        }
                    }
                }
            }
        }
        */
        //_lastFoundMove = legalMoves.get((int) Math.floor(Math.random()*legalMoves.size()));
        return _lastFoundMove;
        //return null;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */

        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        ArrayList<Move> legalMoves = new ArrayList<Move>();
        Move best;
        best = null;
        int bestScore;
        if (sense == 1) {
            bestScore = -WINNING_VALUE;
        } else {
            bestScore = WINNING_VALUE;
        }
        for (int i = 0; i < Board.SIDE; i++) {
            for (int j = 0; j < Board.SIDE; j++) {
                for (int c = -2; c <= 2; c++) {
                    for (int d = -2; d <= 2; d++) {
                        if (board.legalMove((char) (i + 'a'), (char) (j + '1'),
                                (char) (i + c + 'a'), (char) (j + d + '1'))) {
                            legalMoves.add(Move.move((char) (i + 'a'), (char) (j + '1'),
                                    (char) (i + c + 'a'), (char) (j + d + '1')));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < legalMoves.size(); i++) {
            Board testBoard = new Board(board);
            Move testMove = legalMoves.get(i);
            testBoard.makeMove(testMove);
            if (sense == 1) {
                int response = minMax(testBoard, depth - 1,
                        false, -1, alpha, beta);
                if (response > bestScore || best == null) {
                    bestScore = response;
                    best = testMove;
                    alpha = max(alpha, bestScore);
                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
            } else {
                int response = minMax(testBoard, depth - 1,
                        false, 1, alpha, beta);
                if (response < bestScore || best == null) {
                    bestScore = response;
                    best = testMove;
                    beta = min(beta, bestScore);
                    if (alpha >= beta) {
                        return bestScore;
                    }
                }
            }
        }
        /*
        for (int ci = 0; ci < Board.SIDE; ci++) {
            for (int ri = 0; ri < Board.SIDE; ri++) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (board.legalMove((char) (ci + 'a'), (char) (ri + '1'),
                                (char) (ci + i + 'a'), (char) (ri + j + '1'))) {
                            Board testBoard = new Board(board);
                            Move testMove = Move.move((char) (ci + 'a'), (char) (ri + '1'),
                                    (char) (ci + i + 'a'), (char) (ri + j + '1'));
                            testBoard.makeMove(testMove);
                            if (sense == 1) {
                                int response = minMax(testBoard, depth - 1,
                                        false, -1, alpha, beta);
                                if (response > bestScore) {
                                    bestScore = response;
                                    best = testMove;
                                    alpha = max(alpha, bestScore);
                                    if (alpha >= beta) {
                                        return bestScore;
                                    }
                                }
                            } else {
                                int response = minMax(testBoard, depth - 1,
                                        false, 1, alpha, beta);
                                if (response < bestScore) {
                                    bestScore = response;
                                    best = testMove;
                                    beta = min(beta, bestScore);
                                    if (alpha >= beta) {
                                        return bestScore;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
         */
        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        return board.numPieces(RED) - board.numPieces(BLUE);
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
