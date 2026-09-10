package edu.co.icesi.service;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;
import edu.co.icesi.repository.ArtistRepository;
import edu.co.icesi.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private ArtistRepository artistRepository;

    public Collection<Track> getTracks() {
        return trackRepository.findAll();
    }

    public void addTrack(Track track) {
        if (trackRepository.existById(track.getId())) {
            System.out.println("Track already exists");
            return;
        }

        if (track.getArtistIds().size() == 0) {
            System.out.println("The track needs at least one artist");
            return;
        }

        for (Integer artistId : track.getArtistIds()) {
            if (!artistRepository.existById(artistId)) {
                System.out.println("Artist not found: " + artistId);
                return;
            }
        }

        trackRepository.save(track);
        for (Integer artistId : track.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId);
            artist.getTrackIds().add(track.getId());
        }
        System.out.println("Track saved");
    }

    public void deleteTrack(int trackId) {
        if (!trackRepository.existById(trackId)) {
            System.out.println("Track not found");
            return;
        }

        Track track = trackRepository.findById(trackId);
        for (Integer artistId : track.getArtistIds()) {
            Artist artist = artistRepository.findById(artistId);
            if (artist != null) {
                for (int i = artist.getTrackIds().size() - 1; i >= 0; i--) {
                    if (artist.getTrackIds().get(i) == trackId) {
                        artist.getTrackIds().remove(i);
                    }
                }
            }
        }
        trackRepository.deleteById(trackId);
        System.out.println("Track deleted");
    }

    public List<Artist> getArtistsByTrack(int trackId) {
        List<Artist> result = new ArrayList<>();
        Track track = trackRepository.findById(trackId);
        if (track != null) {
            for (Integer artistId : track.getArtistIds()) {
                Artist artist = artistRepository.findById(artistId);
                if (artist != null) {
                    result.add(artist);
                }
            }
        }
        return result;
    }
}
