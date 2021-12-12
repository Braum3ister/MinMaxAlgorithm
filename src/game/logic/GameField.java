package game.logic;

import game.ressources.SemanticsException;
import utitlity.Position;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class GameField {
    private static final int BOARD_SIZE = 3;
    private static final int DEPTH_OF_SEARCH = 10;
    private final Players[][] gameBoard;
    private Players currentPlayer;
    private boolean isGameOver;
    private Players winningPlayer;


    public GameField() {
        this.gameBoard = initialize();
        this.currentPlayer = Players.WHITE;
        this.isGameOver = false;
    }

    private Players[][] initialize() {
        var gameBoardAtStart = new Players[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoardAtStart[i][j] = Players.NONE;
            }
        }
        return gameBoardAtStart;
    }


    public boolean placePiece(Position position) throws SemanticsException {
        if (isGameOver) throw new SemanticsException("Game is already over");
        if (!isPositionValid(position)) throw new SemanticsException("Position is not valid");
        if (gameBoard[position.getRow()][position.getColumn()] != Players.NONE)
            throw new SemanticsException("Cant place here");

        //Place piece
        gameBoard[position.getRow()][position.getColumn()] = currentPlayer;
        //Check Winner
        if (isGameOver(gameBoard)) {
            isGameOver = true;
            this.winningPlayer = currentPlayer;
        }
        //Switch Players
        currentPlayer = currentPlayer.getOtherPlayer();
        return isGameOver;
    }


    public String createWinningMessage() {
        if (!isGameOver) throw new IllegalArgumentException();
        return winningPlayer.toString() + " has won!";
    }


    private boolean isGameOver(Players[][] gameBoard) {
        //rows

        for (var i = 0; i < BOARD_SIZE; i++) {
            var counterRow = 0;
            var counterColumn = 0;
            for (var j = 0; j < BOARD_SIZE; j++) {
                counterRow += gameBoard[i][j].getValue();
                counterColumn += gameBoard[j][i].getValue();
            }
            if (Math.abs(counterRow) == BOARD_SIZE || Math.abs(counterColumn) == BOARD_SIZE) return true;
        }


        var counterDiagonal = 0;
        var counterDiagonalTwo = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            counterDiagonal += gameBoard[i][i].getValue();
            counterDiagonalTwo += gameBoard[BOARD_SIZE - i - 1][i].getValue();
        }
        return Math.abs(counterDiagonal) == BOARD_SIZE || Math.abs(counterDiagonalTwo) == BOARD_SIZE;

    }


    private boolean isPositionValid(Position position) {
        return position.isInsideParameter(BOARD_SIZE);
    }

    @Override
    public String toString() {
        var output = new StringBuilder();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                output.append(gameBoard[i][j].toString()).append("|");

            }
            output.deleteCharAt(output.length() - 1);
            output.append(System.lineSeparator());
            if (i != 2) output.append("------");
            output.append(System.lineSeparator());
        }
        return output.deleteCharAt(output.length() - 1).toString();
    }

    private Players[][] copyOfGameBoard() {
        var copiedGameBoard = new Players[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                copiedGameBoard[i][j] = gameBoard[i][j];
            }
        }
        return copiedGameBoard;
    }

    public double minMaxAlgorithm() throws SemanticsException {
        int depthOfSearch = 9;
        var copyOfGame = copyOfGameBoard();
        var copyOfCurrentPlayer = currentPlayer;
        double score = max(copyOfGame, copyOfCurrentPlayer, depthOfSearch, depthOfSearch);
        return score;
    }

    private double max(Players[][] copyOfGame, Players currentPlayer, int depthOfSearch, int originalDepth) throws SemanticsException {
        if (depthOfSearch == 0 || !movesPossible(copyOfGame)) {
            return evaluate(copyOfGame, currentPlayer, currentPlayer);
        }

        double maxValue = Integer.MIN_VALUE;

        Queue<Position> possibleMoves = getPossibleMoves(copyOfGame);
        Position bestMove;

        while (!possibleMoves.isEmpty()) {
            var nextMove = possibleMoves.poll();
            copyOfGame[nextMove.getRow()][nextMove.getColumn()] = currentPlayer;
            double value = min(copyOfGame,currentPlayer.getOtherPlayer(), depthOfSearch - 1, originalDepth);

            //mache Zug rückgängig
            copyOfGame[nextMove.getRow()][nextMove.getColumn()] = Players.NONE;

            if (value > maxValue) {
                maxValue = value;

                if (depthOfSearch == originalDepth) {

                    bestMove = nextMove;
                    System.out.println(bestMove);
                }
            }


        }
        return maxValue;
    }

    private double min(Players[][] copyOfGame, Players otherPlayer, int depthOfSearch, int originalDepth) throws SemanticsException {
        if (depthOfSearch == 0 || !movesPossible(copyOfGame)) {
            return evaluate(copyOfGame, otherPlayer, otherPlayer.getOtherPlayer());
        }

        double minValue = Integer.MAX_VALUE;
        Queue<Position> possibleMoves = getPossibleMoves(copyOfGame);
        while(!possibleMoves.isEmpty()) {
            var nextMove = possibleMoves.poll();
            copyOfGame[nextMove.getRow()][nextMove.getColumn()] = otherPlayer;
            double value = max(copyOfGame, otherPlayer.getOtherPlayer(), depthOfSearch - 1, originalDepth);
            copyOfGame[nextMove.getRow()][nextMove.getColumn()] = Players.NONE;

            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }


    private Queue<Position> getPossibleMoves(Players[][] copyOfGame) {
        var queue = new LinkedList<Position>();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (copyOfGame[i][j] == Players.NONE) {
                    queue.add(new Position(i, j));
                }
            }
        }
        return queue;
    }

    private boolean movesPossible(Players[][] copiedGameBoard) {
        //checkWin ->
        if (isGameOver(copiedGameBoard)) return false;

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (copiedGameBoard[i][j] == Players.NONE) return true;
            }
        }
        return false;
    }

    private double evaluate(Players[][] copyGame, Players currentPlayer, Players playerToOptimize) {
        var possibleWinner = determineWinner(copyGame);
        if (possibleWinner == null) return 0;

        if (currentPlayer == possibleWinner) {
            throw new IllegalStateException();
        }
        if (playerToOptimize == possibleWinner) {
            return Integer.MIN_VALUE;
        } else {
            return Integer.MAX_VALUE;
        }

    }

    private Players determineWinner(Players[][] gameBoard) {

        for (var i = 0; i < BOARD_SIZE; i++) {
            var counterRow = 0;
            var counterColumn = 0;
            for (var j = 0; j < BOARD_SIZE; j++) {
                counterRow += gameBoard[i][j].getValue();
                counterColumn += gameBoard[j][i].getValue();
            }
            if (counterColumn == -1 * BOARD_SIZE || counterRow == -1 * BOARD_SIZE) return Players.BLACK;
            if (counterColumn == BOARD_SIZE || counterRow == BOARD_SIZE) return Players.WHITE;
        }


        var counterDiagonal = 0;
        var counterDiagonalTwo = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            counterDiagonal += gameBoard[i][i].getValue();
            counterDiagonalTwo += gameBoard[BOARD_SIZE - i - 1][i].getValue();
        }
        if (counterDiagonal == -1 * BOARD_SIZE || counterDiagonalTwo == -1 * BOARD_SIZE) return Players.BLACK;
        if (counterDiagonal == BOARD_SIZE || counterDiagonalTwo == BOARD_SIZE) return Players.WHITE;
        return null;
    }

    public double negaMax(Players[][] copyGame, Players currentPlayer, int depthOfSearch) {
        if (depthOfSearch == 0 || movesPossible(copyGame)) {
            return evaluate(copyGame, currentPlayer, currentPlayer);
        }

        double maxValue = Integer.MIN_VALUE;

        var queue = getPossibleMoves(copyGame);

        while(!queue.isEmpty()) {
            var nextMove = queue.poll();
            copyGame[nextMove.getRow()][nextMove.getColumn()] = currentPlayer;
            double value = -1 * negaMax(copyGame, currentPlayer.getOtherPlayer(), depthOfSearch - 1);
            copyGame[nextMove.getRow()][nextMove.getColumn()] = Players.NONE;

            if (value > maxValue) {
                maxValue = value;

                if (depthOfSearch == DEPTH_OF_SEARCH) {
                    System.out.println("Best Move" + nextMove);
                }
            }
        }

        return maxValue;
    }

}
