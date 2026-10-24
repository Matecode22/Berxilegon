package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.FotoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FotoPublicacionRepository extends JpaRepository<FotoPublicacion, Integer> {

    List<FotoPublicacion> findByPublicacion_IdOrderByOrdenAsc(Integer publicacionId);
}
