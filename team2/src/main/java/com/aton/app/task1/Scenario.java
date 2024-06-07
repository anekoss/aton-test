package com.aton.app.task1;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Phaser;

@AllArgsConstructor
public class Scenario {
    private final Printer printer;
    private final Path path;
    private final Phaser phaser;

    public void playScenario() throws IOException {
        List<Person> persons = FileManager.readScenario(path, printer, phaser);
        persons.forEach(person -> phaser.register());
        persons.forEach(Thread::start);
    }

}
