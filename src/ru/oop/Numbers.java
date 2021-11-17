package ru.oop;

public class Numbers {
    public Boolean isNumber(char character) {
        return character - '0' >= 0 && character - '0' <= 9;
    }

    public Boolean is1or2(char character) {
        return character == '1' || character == '2';
    }
}