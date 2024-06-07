package com.aton.app.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class FileManager {
    private static final int PHRASE_LIMIT = 2;

    private FileManager() {

    }

    public static List<Person> readScenario(Path path, Printer printer, Phaser phaser) throws IOException {
        Map<String, Person> persons = new HashMap<>();
        AtomicInteger order = new AtomicInteger(0);
        Files.readAllLines(path).forEach(
                phrase -> {
                    String[] personPhrase = phrase.split(":", PHRASE_LIMIT);
                    persons.computeIfAbsent(personPhrase[0], v -> new Person(new ArrayList<>(), personPhrase[0], phaser, printer))
                           .addPhrase(new Phrase(order.getAndIncrement(), personPhrase[1]));
                });
        return persons.values().stream().toList();
    }
}
