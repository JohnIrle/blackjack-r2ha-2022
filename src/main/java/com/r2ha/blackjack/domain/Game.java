package com.r2ha.blackjack.domain;

import com.r2ha.blackjack.adapter.in.console.ConsoleHand;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.PrintStream;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

    private static PrintStream systemOut = System.out;
    private final Deck deck;

    private boolean playerDone;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();
    private static Scanner scanner;

    public static void resetScreen() {
        getPrintStream().println(ansi().reset());
    }

    private static PrintStream getPrintStream() {
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

    public Game() {
        deck = new Deck();
    }

    public static void directOutputTo(PrintStream printStream) {
        systemOut = printStream;
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        // why: players first because this is the rule
        playerHand.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    public void determineOutcome() {
        if (playerHand.isBusted()) {
            getPrintStream().println("You Busted, so you lose.  💸");
        } else if (dealerHand.isBusted()) {
            getPrintStream().println("Dealer went BUST, Player wins! Yay for you!! 💵");
        } else if (playerHand.beats(dealerHand)) {
            getPrintStream().println("You beat the Dealer! 💵");
        } else if (playerHand.pushes(dealerHand)) {
            getPrintStream().println("Push: Nobody wins, we'll call it even.");
        } else {
            getPrintStream().println("You lost to the Dealer. 💸");
        }
    }

    public void dealerTurn() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16 must hit, =>17 must stand)
        if (!playerHand.isBusted()) {
            while (dealerHand.dealerMustDrawCard()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    public String inputFromPlayer() {
        getPrintStream().println("[H]it or [S]tand?");
        return scanner.nextLine();
    }

    public void displayGameState() {
        getPrintStream().print(ansi().eraseScreen().cursor(1, 1));
        getPrintStream().println("Dealer has: ");
        getPrintStream().println(ConsoleHand.displayFaceUpCard(dealerHand));

        // second card is the hole card, which is hidden, or "face down"
        displayBackOfCard();

        getPrintStream().println();
        getPrintStream().println("Player has: ");
        getPrintStream().println(ConsoleHand.cardsAsString(playerHand));
        getPrintStream().println(" (" + playerHand.value() + ")");
    }

    private void displayBackOfCard() {
        getPrintStream().print(
                ansi()
                        .cursorUp(7)
                        .cursorRight(12)
                        .a("┌─────────┐").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("│░ J I T ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E R ░│").cursorDown(1).cursorLeft(11)
                        .a("│░ T E D ░│").cursorDown(1).cursorLeft(11)
                        .a("│░░░░░░░░░│").cursorDown(1).cursorLeft(11)
                        .a("└─────────┘"));
    }

    public void displayFinalGameState() {
        getPrintStream().print(ansi().eraseScreen().cursor(1, 1));
        getPrintStream().println("Dealer has: ");
        getPrintStream().println(ConsoleHand.cardsAsString(dealerHand));
        getPrintStream().println(" (" + dealerHand.value() + ")");

        getPrintStream().println();
        getPrintStream().println("Player has: ");
        getPrintStream().println(ConsoleHand.cardsAsString(playerHand));
        getPrintStream().println(" (" + playerHand.value() + ")");
    }

    public void playerHits() {
        playerHand.drawFrom(deck);
       playerDone = playerHand.isBusted();
    }

    public void playerStands() {
        playerDone = true;
    }

    public boolean isPlayerDone() {
        return playerDone;
    }
}
