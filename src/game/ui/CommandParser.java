package game.ui;

import game.ressources.SyntaxException;
import utitlity.Pair;

import java.util.List;

public interface CommandParser {

    Pair<String, List<String>> parseCommand(String input) throws SyntaxException;
}
