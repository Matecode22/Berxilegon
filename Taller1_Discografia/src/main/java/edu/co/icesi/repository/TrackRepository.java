package edu.co.icesi.repository;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class TrackRepository {

    private HashMap<Integer, Track> tracks;

    @Autowired
    private ArtistRepository artistRepository;

    public TrackRepository() {
        tracks = new HashMap<>();
    }

    public Collection<Track> findAll() {
        return tracks.values();
    }

    public void save(Track track) {
        tracks.put(track.getId(), track);
    }

    public boolean existById(int trackId) {
        return tracks.containsKey(trackId);
    }

    public Track findById(int trackId) {
        return tracks.get(trackId);
    }

    public void deleteById(int trackId) {
        tracks.remove(trackId);
    }

    @PostConstruct
    public void initialize() {
        int trackId = 1;
        for (int artistId = 1; artistId <= 10; artistId++) {
            for (int j = 1; j <= 5; j++) {
                Track track = new Track();
                track.setId(trackId);
                track.setTitle("Track " + trackId);
                track.setGenre("Genero " + j);
                track.setDuration(180 + trackId);
                track.setAlbumTitle("Album " + artistId);
                track.getArtistIds().add(artistId);
                tracks.put(track.getId(), track);

                Artist artist = artistRepository.findById(artistId);
                if (artist != null) {
                    artist.getTrackIds().add(trackId);
                }
                trackId++;
            }
        }
    }
}
