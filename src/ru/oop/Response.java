package ru.oop;

import java.util.ArrayList;
import java.util.List;

public class Response {
    public String responseString;
    public List<Song> responseList;

    public Response(String responseString){
        this.responseString = responseString;
    }

    public Response(List<Song> responseList){
        this.responseList = responseList;
    }

    public void SongListToStr() {
        responseString = "";
        responseString += "Список найденных песен:\n";
        for (Song song : responseList) {
            responseString += song.fullTitle + "\n";
        }
    }
}
