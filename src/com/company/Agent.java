package com.company;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private Board board;
    private byte playerTurn;

    public Agent(Board board) {
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, Integer.MIN_VALUE, Integer.MAX_VALUE,0);
        this.playerTurn = playerTurn;
        return temp.move;
    }

    private Pair max(Tile[][] currentBoard, byte currentColor, int alpha, int beta, int depth) {
        int value = Integer.MIN_VALUE;
        Move bestMovement = null;
        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        if (depth == 3)
            return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));
        depth++;
        Board newBoard = new Board();
        for (Move move : possibleMoves) {
            Tile[][] tiles = newBoard.doMove(move, currentBoard);
            Pair pair = min(tiles, (byte) 2, alpha, beta, depth);
            if (pair.value > value) {
                value = pair.value;
                bestMovement = move;
            }
            if (alpha < value)
                alpha = value;
            if (beta < value)
                return new Pair(move, value);
        }

        return new Pair(bestMovement, value);
    }

    private Pair min(Tile[][] currentBoard, byte currentColor, int alpha, int beta, int depth) {
        int value = Integer.MAX_VALUE;
        Move bestMovement = null;
        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
        if (depth == 3)
            return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));
        depth++;
        Board newBoard = new Board();
        for (Move move : possibleMoves) {
            Tile[][] tiles = newBoard.doMove(move, currentBoard);
            Pair pair = max(tiles, (byte) 1, alpha, beta, depth);
            if (pair.value < value) {
                value = pair.value;
                bestMovement = move;
            }
            if (beta > value)
                beta = value;
            if (alpha > value)
                return new Pair(move, value);
        }

        return new Pair(bestMovement, value);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
        short score = 0;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {
                    score += (7 - i);
                    score += (7 - j);
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {
                    score -= i;
                    score -= j;
                }
                if (Math.abs(i - j) > 3) {
                    score-= Math.abs(i - j);
                }
                if (i > 0 && j > 0) {
                    if (i < 7 && j < 7 && (currentBoard[i + 1][j].color == playerTurn ||
                            currentBoard[i][j + 1].color == playerTurn ||
                            currentBoard[i + 1][j + 1].color == playerTurn ||
                            currentBoard[i - 1][j].color == playerTurn ||
                            currentBoard[i - 1][j - 1].color == playerTurn) ||
                            currentBoard[i][j - 1].color == playerTurn) {
                        score++;
                    }
                }
            }
        }
        return score;
    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }


    public boolean checkTerminal(Tile[][] currentTiles) {

        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}