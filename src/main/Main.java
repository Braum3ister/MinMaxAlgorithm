package main;

import game.ui.Parser;
import game.ui.Session;
import utitlity.Input;
import utitlity.Output;

import java.util.Scanner;

public final class Main {
    private static final Output OUTPUT = System.out::println;
    private static final Output ERROR_OUTPUT = string -> System.out.println("Error, " + string);
    private static final Input INPUT = () -> {
        var scanner = new Scanner(System.in);
        return scanner.nextLine();
    };


    private Main() {
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        var session = new Session(OUTPUT, ERROR_OUTPUT, INPUT, new Parser());
        session.interactive();
    }
}
