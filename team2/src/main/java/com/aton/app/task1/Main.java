package com.aton.app.task1;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Phaser;

public class Main {

    public static void main(String[] args) throws IOException {
        Scenario scenario = new Scenario(System.out::println, Path.of("team2/scenario.txt"), new Phaser());
        scenario.playScenario();
    }
}
