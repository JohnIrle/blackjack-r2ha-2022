package com.r2ha.blackjack;

public class ConsoleHand {
    static String displayFaceUpCard(Hand hand) {
        return ConsoleCard.display(hand.faceUpCard());
    }
}
