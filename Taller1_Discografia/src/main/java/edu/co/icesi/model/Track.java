package edu.co.icesi.model;

import java.util.ArrayList;
import java.util.List;

public class Track {

    private int id;
    private String title;
    private String genre;
    private int duration;
    private String albumTitle;
    private List<Integer> artistIds = new ArrayList<>();

    public Track() {
    }

    public Track(int id, String title, String genre, int duration, String albumTitle) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.albumTitle = albumTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public List<Integer> getArtistIds() {
        return artistIds;
    }

    @Override
    public String toString() {
        return title + " (" + id + ") - " + genre + " - " + duration + " segundos - " + albumTitle;
    }
}
