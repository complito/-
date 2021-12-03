package ru.oop;

import java.util.List;

public class Response {
    private String responseString;
    private List<Song> songList;
    private List<Artist> artistList;

    public String getResponseString() {
        return this.responseString;
    }

    public List<Song> getSongList() {
        return this.songList;
    }

    public List<Artist> getArtistList() {
        return this.artistList;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public Response(String responseString){
        this.responseString = responseString;
    }

    public Response(List<Song> responseSongList) { this.songList = responseSongList; }

    public Response() {}

    public void songListToStr() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Список найденных песен:\nПесня (номер)\n");
        for (Song song : songList) {
            strBuilder.append(song.getFullTitle())
                    .append(" (")
                    .append(song.getApiPath().substring(7))
                    .append(")\n");
        }
        responseString = strBuilder.toString();
    }

    public void artistListToStr() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Список найденных артистов:\nАртист (номер)\n");
        for (Artist artist : artistList) {
            strBuilder.append(artist.getArtistName())
                    .append(" (")
                    .append(artist.getArtistId())
                    .append(")\n");
        }
        responseString = strBuilder.toString();
    }
}
