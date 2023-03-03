package com.r2ha.blackjack.adapter.in.console;

import com.r2ha.blackjack.domain.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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
        Game.directOutputTo(printStream);
        // Starts the game with an empty String array for the arguments
        Game.main(new String[0]);

        String output = baos.toString();
        assertThat(output).containsIgnoringWhitespaces(
                "Hit [ENTER]",
                "[H]it or [S]tand?");
    }
}
