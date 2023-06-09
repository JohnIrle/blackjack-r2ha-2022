package com.r2ha.blackjack.domain;

import com.r2ha.blackjack.adapter.in.console.ConsoleCard;
import com.r2ha.blackjack.adapter.in.console.ConsoleGame;
import com.r2ha.blackjack.adapter.in.console.ConsoleHand;

import static org.fusesource.jansi.Ansi.ansi;

public class Game {

    private final Deck deck;

    private boolean playerDone;

    private final Hand dealerHand = new Hand();
    private final Hand playerHand = new Hand();

    public Game() {
        deck = new Deck();
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

    public String determineOutcome() {
        if (playerHand.isBusted()) {
            return "You Busted, so you lose.  💸";
        } else if (dealerHand.isBusted()) {
            return "Dealer went BUST, Player wins! Yay for you!! 💵";
        } else if (playerHand.beats(dealerHand)) {
            return "You beat the Dealer! 💵";
        } else if (playerHand.pushes(dealerHand)) {
            return "Push: Nobody wins, we'll call it even.";
        } else {
            return "You lost to the Dealer. 💸";
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

    public void displayGameState() {
        ConsoleGame.getPrintStream().print(ansi().eraseScreen().cursor(1, 1));
        ConsoleGame.getPrintStream().println("Dealer has: ");
        ConsoleGame.getPrintStream().println(ConsoleHand.displayFaceUpCard(dealerHand));

        // second card is the hole card, which is hidden, or "face down"
        ConsoleCard.displayBackOfCard();

        ConsoleGame.getPrintStream().println();
        ConsoleGame.getPrintStream().println("Player has: ");
        ConsoleGame.getPrintStream().println(ConsoleHand.cardsAsString(playerHand));
        ConsoleGame.getPrintStream().println(" (" + playerHand.value() + ")");
    }

    public void displayFinalGameState() {
        ConsoleGame.getPrintStream().print(ansi().eraseScreen().cursor(1, 1));
        ConsoleGame.getPrintStream().println("Dealer has: ");
        ConsoleGame.getPrintStream().println(ConsoleHand.cardsAsString(dealerHand));
        ConsoleGame.getPrintStream().println(" (" + dealerHand.value() + ")");

        ConsoleGame.getPrintStream().println();
        ConsoleGame.getPrintStream().println("Player has: ");
        ConsoleGame.getPrintStream().println(ConsoleHand.cardsAsString(playerHand));
        ConsoleGame.getPrintStream().println(" (" + playerHand.value() + ")");
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
