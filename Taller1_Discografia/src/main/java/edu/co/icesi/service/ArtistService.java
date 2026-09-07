package edu.co.icesi.service;

import edu.co.icesi.model.Artist;
import edu.co.icesi.model.Track;
import edu.co.icesi.repository.ArtistRepository;
import edu.co.icesi.repository.TrackRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArtistService {

    private ArtistRepository artistRepository;
    private TrackRepository trackRepository;

    public ArtistService(ArtistRepository artistRepository, TrackRepository trackRepository) {
        this.artistRepository = artistRepository;
        this.trackRepository = trackRepository;
    }

    public Collection<Artist> getArtists() {
        return artistRepository.findAll();
    }

    public void addArtist(Artist artist) {
        if (!artistRepository.existById(artist.getId())) {
            artistRepository.save(artist);
            System.out.println("Artist saved");
        } else {
            System.out.println("Artist already exists");
        }
    }

    public Artist getArtistByName(String name) {
        return artistRepository.findByName(name);
    }

    public List<Track> getTracksByArtistName(String name) {
        List<Track> result = new ArrayList<>();
        Artist artist = artistRepository.findByName(name);

        if (artist != null) {
            for (Integer trackId : artist.getTrackIds()) {
                Track track = trackRepository.findById(trackId);
                if (track != null) {
                    result.add(track);
                }
            }
        }
        return result;
    }

    public void deleteArtist(int artistId) {
        if (artistRepository.existById(artistId)) {
            for (Track track : trackRepository.findAll()) {
                for (int i = track.getArtistIds().size() - 1; i >= 0; i--) {
                    if (track.getArtistIds().get(i) == artistId) {
                        track.getArtistIds().remove(i);
                    }
                }
            }
            artistRepository.deleteById(artistId);
            System.out.println("Artist deleted");
        } else {
            System.out.println("Artist not found");
        }
    }
}
