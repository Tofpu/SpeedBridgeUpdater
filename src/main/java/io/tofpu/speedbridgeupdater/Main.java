package io.tofpu.speedbridgeupdater;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit && scanner.hasNext()) {
            final String input = scanner.nextLine();
            switch (input) {
                case "exit":
                    exit = true;
                    break;
            }
        }
    }
}
