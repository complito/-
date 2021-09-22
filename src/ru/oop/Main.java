package ru.oop;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println(BotLogic.startMessage());
        while (true) {
            System.out.print("Введите запрос: ");
            String query = input.nextLine();
            ProcessingQuery.executeQuery(query);
        }
    }
}