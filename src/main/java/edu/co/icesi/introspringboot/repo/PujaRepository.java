package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Puja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PujaRepository extends JpaRepository<Puja, Integer> {

    List<Puja> findByPublicacion_IdOrderByValorDesc(Integer publicacionId);

    // la puja mas alta de una subasta
    Optional<Puja> findFirstByPublicacion_IdOrderByValorDesc(Integer publicacionId);
}
