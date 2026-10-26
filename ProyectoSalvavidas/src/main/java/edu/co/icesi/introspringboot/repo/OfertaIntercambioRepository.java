package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.OfertaIntercambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaIntercambioRepository extends JpaRepository<OfertaIntercambio, Integer> {

    List<OfertaIntercambio> findByPublicacion_Id(Integer publicacionId);

    List<OfertaIntercambio> findByUsuario_Id(Integer usuarioId);

    List<OfertaIntercambio> findByPublicacion_IdAndEstado(Integer publicacionId, String estado);
}
