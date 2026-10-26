package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// la llave es el id de la publicacion
@Repository
public interface DetalleSubastaRepository extends JpaRepository<DetalleSubasta, Integer> {
}
