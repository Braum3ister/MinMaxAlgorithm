package game.ui;


import game.logic.TicTacToeManagement;
import game.ressources.Errors;
import game.ressources.SyntaxException;
import utitlity.Input;
import utitlity.Output;
import utitlity.Pair;

import java.util.List;


/**
 * This class describes a session for command execution.
 *
 * @author Johannes Stephan
 * @version 1.0
 */
public class Session {
    private boolean isCodeRunning;
    private final TicTacToeManagement management;
    private final Output output;
    private final Output errOutput;
    private final Input input;
    private final CommandParser parser;


    /**
     * Instantiates a new Session.
     *
     * @param output    the output consumer
     * @param errOutput the error output consumer
     * @param input     the input supplier
     * @param parser    the parser used to decode the input strings
     */
    public Session(Output output, Output errOutput, Input input, CommandParser parser) {
        this.management = new TicTacToeManagement();
        this.parser = parser;
        this.input = input;
        this.output = output;
        this.errOutput = errOutput;
    }

    /**
     * Method which starts the FireBreaker - Game
     */
    public void interactive() {
        isCodeRunning = true;
        while (isCodeRunning) {
            processSingleCommand();
        }
    }


    private void processSingleCommand() {
        String inputUser = input.read(); //Scanner.readline()
        Pair<String, List<String>> parsedArguments; //command , Parameter

        try {
            parsedArguments = parser.parseCommand(inputUser);
        } catch (SyntaxException e) {
            errOutput.output(e.getMessage());
            return;
        }
        String command = parsedArguments.getFirstElement();
        List<String> parameters = parsedArguments.getSecondElement();

        executeSingleCommand(command, parameters);
    }

    private void executeSingleCommand(String commandName, List<String> parameters) {
        Result result;

        try {
            result = Command.getCommand(commandName).executeCommand(management, parameters);
        } catch (SyntaxException e) {
            result = new Result(Result.ResultType.FAILURE, e.getMessage());
        }
        switch (result.getType()) {
            case SUCCESS:
                if (result.getMessage() != null) {
                    output.output(result.getMessage());
                } else {
                    isCodeRunning = false;
                }
                break;
            case FAILURE:
                if (result.getMessage() != null) {
                    errOutput.output(result.getMessage());
                } else {
                    errOutput.output(Errors.COMMAND_ENDED_ERROR);
                }
                break;
            default:
                throw new IllegalStateException(Errors.NOT_IMPLEMENTED);
        }
    }
}
