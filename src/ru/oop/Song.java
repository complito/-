package ru.oop;

public class Song {
    public String apiPath;
    public String fullTitle;
    public String primaryArtistApiPath;
    public Song(String apiPath, String fullTitle, String primaryArtistApiPath) {
        this.apiPath = apiPath;
        this.fullTitle = fullTitle;
        this.primaryArtistApiPath = primaryArtistApiPath;
    }
    Song() {

    }
    @Override
    public boolean equals(Object obj){
        if (obj == this){
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Song song = (Song) obj;
        return apiPath.equals(song.apiPath)
                && fullTitle.equals(song.fullTitle)
                && primaryArtistApiPath.equals(song.primaryArtistApiPath);
    }
}
