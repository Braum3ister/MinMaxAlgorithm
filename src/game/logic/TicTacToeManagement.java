package game.logic;

import game.ressources.SemanticsException;
import utitlity.Position;

public class TicTacToeManagement {
    private final GameField gameField;

    public TicTacToeManagement() {
        this.gameField = new GameField();
    }


    public String placePiece(Position position) throws SemanticsException {
        if(gameField.placePiece(position)) {
            return gameField.createWinningMessage();
        }
        return "OK";
    }

    public String evaluate() throws SemanticsException {
        return String.valueOf(gameField.minMaxAlgorithm());
    }

    public String printBoard() {
        return gameField.toString();
    }
}
