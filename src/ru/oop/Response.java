package ru.oop;

import java.util.List;

public class Response {
    private String responseString;
    private List<Song> responseList;

    public String getResponseString() {
        return this.responseString;
    }

    public List<Song> getResponseList() {
        return this.responseList;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public void setResponseList(List<Song> responseList) {
        this.responseList = responseList;
    }

    public Response(String responseString){
        this.responseString = responseString;
    }

    public Response(List<Song> responseList){
        this.responseList = responseList;
    }

    public void songListToStr() {
        responseString = "Список найденных песен:\n";
        for (int i = 0; i < responseList.size(); i++) {
            responseString += i + ") " + responseList.get(i).getFullTitle() + "\n";
        }
        responseString += "Введите номер песни и через пробел 1 или 2\n1 - текст песни\n2 - информация о песни";
    }
}
