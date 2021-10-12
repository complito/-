package ru.oop;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BotLogic botLogic = new BotLogic();
        BotIO botIO = new BotIO();

        botIO.Response(botLogic.startMessage());
        while (true) {
            botIO.Response("Введите запрос: ");
            String query = input.nextLine();
            for (String request : botIO.Request(query))
                botIO.Response(request);
        }
    }
}