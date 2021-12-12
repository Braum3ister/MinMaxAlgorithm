package game.ui;

import game.ressources.Errors;
import game.ressources.SyntaxException;
import utitlity.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Parser implements CommandParser {
    private static final String SPACE = " ";

    @Override
    public Pair<String, List<String>> parseCommand(String inputUser) throws SyntaxException {
        checkBasicRegex(inputUser);
        String commandValue = inputUser.split(SPACE)[0];

        return new Pair<>(checkCommand(commandValue, inputUser), createParameters(inputUser, commandValue));

    }

    private List<String> createParameters(String inputUser, String commandValue) {
        var modifiedInput = inputUser.substring(commandValue.length());
        if (modifiedInput.equals("")) {
            return new LinkedList<>();
        }

        String[] outputAsStringArray = modifiedInput.substring(1).split(",");
        return new LinkedList<>(Arrays.asList(outputAsStringArray));
    }

    private void checkBasicRegex(String inputUser) throws SyntaxException {
        if (inputUser.isEmpty()) throw new SyntaxException(Errors.SYNTAX_ERROR);
        if (inputUser.charAt(0) == ' ') {
            throw new SyntaxException(Errors.SYNTAX_ERROR);
        }
    }


    private String checkCommand(String command, String userInput) throws SyntaxException {
        if (userInput.matches(Command.getCommand(command).getRegexOfTheCommand())) {
            return command;
        }
        throw new SyntaxException(Errors.SYNTAX_ERROR);
    }
}
