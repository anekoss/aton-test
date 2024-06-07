package com.aton.app.task1;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.concurrent.Phaser;

@AllArgsConstructor
public class Person extends Thread {
    private final List<Phrase> phrases;
    private final String name;
    private final Phaser phaser;
    private final Printer printer;

    @Override
    public void run() {
        for (var phrase : phrases) {
            awaitTurn(phrase);
            printer.print(name + ":" + phrase.phrase());
            phaser.arriveAndAwaitAdvance();
        }
        phaser.arriveAndDeregister();
    }

    public void awaitTurn(Phrase phrase) {
        while (!isMyTurn(phrase)) {
            phaser.arriveAndAwaitAdvance();
        }
    }

    public boolean isMyTurn(Phrase phrase) {
        return phrase.turn() == phaser.getPhase();
    }

    public void addPhrase(Phrase phrase) {
        phrases.add(phrase);
    }


}
