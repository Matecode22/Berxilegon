package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import java.util.List;
import java.util.Optional;

public interface ArticuloOfertadoService {

    List<ArticuloOfertado> findAll();

    Optional<ArticuloOfertado> findById(ArticuloOfertadoId id);

    ArticuloOfertado save(ArticuloOfertado articuloOfertado);

    void deleteById(ArticuloOfertadoId id);
}
