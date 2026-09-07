package edu.co.icesi.repository;

import edu.co.icesi.model.Artist;

import java.util.Collection;
import java.util.HashMap;

public class ArtistRepository {

    private HashMap<Integer, Artist> artists;

    public ArtistRepository() {
        artists = new HashMap<>();
    }

    public Collection<Artist> findAll() {
        return artists.values();
    }

    public void save(Artist artist) {
        artists.put(artist.getId(), artist);
    }

    public boolean existById(int artistId) {
        return artists.containsKey(artistId);
    }

    public Artist findById(int artistId) {
        return artists.get(artistId);
    }

    public Artist findByName(String name) {
        for (Artist artist : artists.values()) {
            if (artist.getName().equals(name)) {
                return artist;
            }
        }
        return null;
    }

    public void deleteById(int artistId) {
        artists.remove(artistId);
    }

    public void initialize() {
        for (int i = 1; i <= 10; i++) {
            Artist artist = new Artist();
            artist.setId(i);
            artist.setName("Artista " + i);
            artist.setNationality("Nacionalidad " + i);
            artists.put(artist.getId(), artist);
        }
    }
}
