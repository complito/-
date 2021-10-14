package ru.oop;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BotLogic botLogic = new BotLogic();
        BotIO botIO = new BotIO();
        botIO.printResponse(botLogic.startMessage());
        while (true) {
            String query = input.nextLine();
            botIO.printResponse(botIO.sendRequest(query));
        }
    }
}