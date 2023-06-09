package com.r2ha.blackjack.adapter.in.console;

import com.r2ha.blackjack.domain.Game;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ConsoleGame {

    private static PrintStream systemOut = System.out;
    public static Scanner scanner;
    private final Game game;

    public ConsoleGame(Game game) {
        this.game = game;
    }

    public static void resetScreen() {
        getPrintStream().println(ansi().reset());
    }

    public static PrintStream getPrintStream() {
        return systemOut;
    }

    public static void waitForEnterFromUser() {
        getPrintStream().println(ansi()
                                   .cursor(3, 1)
                                   .fgBrightBlack().a("Hit [ENTER] to start..."));

        scanner.nextLine();
    }

    public static void displayWelcomeScreen() {
        scanner = new Scanner(System.in);
        AnsiConsole.systemInstall();
        getPrintStream().println(ansi()
                                   .bgBright(Ansi.Color.WHITE)
                                   .eraseScreen()
                                   .cursor(1, 1)
                                   .fgGreen().a("Welcome to")
                                   .fgRed().a(" JitterTed's")
                                   .fgBlack().a(" Blackjack game"));
    }

    public static void directOutputTo(PrintStream printStream) {
        systemOut = printStream;
    }

    public void start() {
        displayWelcomeScreen();
        waitForEnterFromUser();

        game.initialDeal();

        playerPlays();

        game.dealerTurn();

        game.displayFinalGameState();

        game.determineOutcome();

        resetScreen();
    }

    public void playerPlays() {
        while (!game.isPlayerDone()) {
            game.displayGameState();
            String command = game.inputFromPlayer();
            handle(command);
        }
    }

    public void handle(String command) {
        if (command.toLowerCase().startsWith("h")) {
            game.playerHits();
        } else if (command.toLowerCase().startsWith("s")) {
            game.playerStands();
        }
    }

}