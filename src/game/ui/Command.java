package game.ui;

import game.logic.TicTacToeManagement;
import game.ressources.Errors;
import game.ressources.SemanticsException;
import game.ressources.SyntaxException;
import utitlity.Position;

import java.util.List;

public enum Command {
    PRINT_BOARD("print", "print") {
        @Override
        public Result executeCommand(TicTacToeManagement management, List<String> parameters) {
            return new Result(Result.ResultType.SUCCESS, management.printBoard());
        }
    },
    PLACE_PIECE("place", "place [0-9],[0-9]") {
        @Override
        public Result executeCommand(TicTacToeManagement management, List<String> parameters) {
            String output;

            try {
                output = management.placePiece(new Position(Integer.parseInt(parameters.get(0))
                        , Integer.parseInt(parameters.get(1))));
            } catch (SemanticsException exception) {
                return new Result(Result.ResultType.FAILURE, exception.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, output);
        }
    },
    QUIT("quit", "quit") {
        @Override
        public Result executeCommand(TicTacToeManagement management, List<String> parameters) {
            return new Result(Result.ResultType.SUCCESS);
        }
    },
    EVALUATE("evaluate", "evaluate") {
        @Override
        public Result executeCommand(TicTacToeManagement management, List<String> parameters) {
            String output;
            try {
                output = management.evaluate();
            } catch (SemanticsException exception) {
                return new Result(Result.ResultType.FAILURE, exception.getMessage());
            }
            return new Result(Result.ResultType.SUCCESS, output);
        }
    };


    private final String regex;
    private final String name;

    Command(String name, String regex) {
        this.regex = regex;
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    public abstract Result executeCommand(TicTacToeManagement management, List<String> parameters);

    /**
     * Gets a command through the String representation
     *
     * @param commandName the command name
     * @return the found command
     * @throws SyntaxException if there is no command corresponding to the string
     */
    public static Command getCommand(String commandName) throws SyntaxException {
        for (Command command : Command.values()) {
            if (command.name.equals(commandName)) return command;
        }
        throw new SyntaxException(Errors.COMMAND_NOT_IMPLEMENTED);
    }

    /**
     * Gets regex of the command.
     *
     * @return the regex of the command
     */
    public String getRegexOfTheCommand() {
        return regex;
    }
}
