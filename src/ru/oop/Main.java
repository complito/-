package ru.oop;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BotLogic bot = new BotLogic();
        Scanner input = new Scanner(System.in);
        System.out.println(bot.startMessage());
        while (true) {
            System.out.print("Введите запрос: ");
            String query = input.nextLine();
            ProcessingQuery.executeQuery(query);
        }
    }
}