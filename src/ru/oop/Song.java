package ru.oop;

public class Song {
    private String apiPath;
    private String fullTitle;
    private String primaryArtistApiPath;
    private String lyricsPath;

    public String getApiPath() {
        return this.apiPath;
    }

    public String getFullTitle() {
        return this.fullTitle;
    }

    public String getPrimaryArtistApiPath() {
        return this.primaryArtistApiPath;
    }

    public String getLyricsPath() {
        return this.lyricsPath;
    }

    public void setLyricsPath(String lyricsPath) {
        this.lyricsPath = lyricsPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public void setPrimaryArtistApiPath(String primaryArtistApiPath) {
        this.primaryArtistApiPath = primaryArtistApiPath;
    }

    public Song(String apiPath, String fullTitle, String primaryArtistApiPath, String lyricsPath) {
        this.apiPath = apiPath;
        this.fullTitle = fullTitle;
        this.primaryArtistApiPath = primaryArtistApiPath;
        this.lyricsPath = lyricsPath;
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
                && primaryArtistApiPath.equals(song.primaryArtistApiPath)
                && lyricsPath.equals(song.lyricsPath);
    }
}
