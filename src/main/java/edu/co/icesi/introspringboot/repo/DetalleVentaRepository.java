package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// la llave es el id de la publicacion
@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
}
