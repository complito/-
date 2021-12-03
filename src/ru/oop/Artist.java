package ru.oop;

public class Artist {
    private String artistName;
    private String artistId;

    public String getArtistName() { return artistName; }

    public String getArtistId() { return artistId; }

    public void setArtistName(String artistName) { this.artistName = artistName; }

    public void setArtistId(String artistId) { this.artistId = artistId; }

    public Artist(String artistName, String artistId) {
        this.artistName = artistName;
        this.artistId = artistId;
    }

    public Artist() {}

    @Override
    public boolean equals(Object obj){
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Artist artist = (Artist) obj;
        return artistName.equals(artist.artistName)
                && artistId.equals(artist.artistId);
    }
}
