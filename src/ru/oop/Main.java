package ru.oop;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println(BotLogic.startMessage());
        while (true) {
            System.out.print("Введите запрос: ");
            String query = input.nextLine();
            if (query.equals("")) System.out.println("Ошибка: запрос пустой");
            else if (query.equals("\\help")) System.out.println(BotLogic.helpMessage());
            else {
                List<Song> songs = BotLogic.findSongs(query);
                if (songs.isEmpty()) System.out.println("По введённому запросу не было найдено песен");
                else {
                    System.out.println("Список найденных песен:");
                    for (Song song : songs) System.out.println(song.fullTitle);
                }
            }
        }
    }
}