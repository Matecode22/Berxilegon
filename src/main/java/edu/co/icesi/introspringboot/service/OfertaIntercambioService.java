package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.OfertaIntercambio;

import java.util.List;
import java.util.Optional;

public interface OfertaIntercambioService {

    List<OfertaIntercambio> findAll();

    Optional<OfertaIntercambio> findById(Integer id);

    OfertaIntercambio save(OfertaIntercambio oferta);

    void deleteById(Integer id);

    List<OfertaIntercambio> findByPublicacionId(Integer publicacionId);

    OfertaIntercambio crearOferta(Integer publicacionId, Integer usuarioId, List<Integer> publicacionesOfrecidasIds,
                                  Integer diferenciaDinero, String aFavorDe);

    OfertaIntercambio aceptar(Integer id);

    OfertaIntercambio rechazar(Integer id, String motivo);
}
