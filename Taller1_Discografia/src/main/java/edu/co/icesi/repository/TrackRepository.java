package edu.co.icesi.repository;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;

import java.util.Collection;
import java.util.HashMap;

public class TrackRepository {

    private HashMap<Integer, Track> tracks;

    private ArtistRepository artistRepository;

    public TrackRepository() {
        tracks = new HashMap<>();
    }

    public TrackRepository(ArtistRepository artistRepository) {
        tracks = new HashMap<>();
        this.artistRepository = artistRepository;
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
