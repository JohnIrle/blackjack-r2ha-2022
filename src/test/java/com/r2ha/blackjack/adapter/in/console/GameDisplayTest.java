package com.r2ha.blackjack.adapter.in.console;

import com.r2ha.blackjack.Blackjack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GameDisplayTest {
    private final InputStream originalSystemIn = System.in;

    private void provideInput(String input) {
        byte[] inputBytes = input.getBytes();
        ByteArrayInputStream testIn = new ByteArrayInputStream(inputBytes);
        System.setIn(testIn);
    }

    @AfterEach
    public void restoreSystemInput() {
        System.setIn(originalSystemIn);
    }

    @Test
    void gamePlays() {
        provideInput("\nS\n");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        ConsoleGame.directOutputTo(printStream);
        // Starts the game with an empty String array for the arguments
        Blackjack.main(new String[0]);

        List<String> cleanedOutput = baos.toString()
                                         .replaceAll("\u001B\\[[\\d;]*[^\\d;]", "\n")
                                         .lines()
                                         .map(String::strip)
                                         .toList();

        assertThat(cleanedOutput)
                .contains(
                        "Welcome to",
                        "JitterTed's",
                        "Blackjack game",
                        "Hit [ENTER] to start...",
                        "Dealer has:",
                        "Player has:",
                        "[H]it or [S]tand?"
                );

        // count card tops, 1 for each card displayed
        long cardTops = cleanedOutput.stream()
                                     .filter(s -> s.equals("┌─────────┐")).count();
        assertThat(cardTops)
                .describedAs("At least 8 cards should have been displayed, could be more depending on Dealer's hand")
                .isGreaterThanOrEqualTo(8);

        // Count card bottoms, must match the number of card tops
        long cardBottoms = cleanedOutput.stream()
                                        .filter(s -> s.equals("└─────────┘")).count();
        assertThat(cardBottoms)
                .describedAs("Number of card bottoms must match number of card tops")
                .isEqualTo(cardTops);

        // Count card middles
        long cardMiddles = cleanedOutput.stream()
                                        .filter(s -> s.equals("│         │")
                                                // Back of dealer card
                                                || s.equals("│░░░░░░░░░│"))
                                        .count();
        assertThat(cardMiddles)
                .describedAs("Number of card middles must match number of card tops * 2")
                .isEqualTo(cardTops * 2);

        assertThat(cleanedOutput)
                .containsAnyOf("You Busted, so you lose.  💸",
                               "Dealer went BUST, Player wins! Yay for you!! 💵",
                               "You beat the Dealer! 💵",
                               "Push: Nobody wins, we'll call it even.",
                               "You lost to the Dealer. 💸");
    }
}
