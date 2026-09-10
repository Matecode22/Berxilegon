package edu.co.icesi.config;

import edu.co.icesi.repository.ArtistRepository;
import edu.co.icesi.repository.TrackRepository;
import edu.co.icesi.service.ArtistService;
import edu.co.icesi.service.TrackService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {

    //Repositories
    @Bean(initMethod = "initialize")
    public ArtistRepository artistRepository(){
        return new ArtistRepository();
    }

    @Bean(initMethod = "initialize")
    public TrackRepository trackRepository(ArtistRepository artistRepository){
        return new TrackRepository(artistRepository);
    }

    //Services
    @Bean
    public ArtistService artistService(
            ArtistRepository artistRepository,
            TrackRepository trackRepository
    ){
        return new ArtistService(artistRepository, trackRepository);
    }

    @Bean
    public TrackService trackService(
            TrackRepository trackRepository,
            ArtistRepository artistRepository
    ){
        return new TrackService(trackRepository, artistRepository);
    }
}
