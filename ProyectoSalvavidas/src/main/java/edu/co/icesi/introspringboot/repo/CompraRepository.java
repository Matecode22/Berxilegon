package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {

    List<Compra> findByUsuario_Id(Integer usuarioId);

    List<Compra> findByPublicacion_Id(Integer publicacionId);
}
