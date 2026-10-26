package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticuloOfertadoRepository extends JpaRepository<ArticuloOfertado, ArticuloOfertadoId> {

    List<ArticuloOfertado> findByOferta_Id(Integer ofertaId);
}
