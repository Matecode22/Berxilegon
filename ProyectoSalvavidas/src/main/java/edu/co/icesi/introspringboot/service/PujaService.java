package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Puja;

import java.util.List;
import java.util.Optional;

public interface PujaService {

    List<Puja> findAll();

    Optional<Puja> findById(Integer id);

    Puja save(Puja puja);

    void deleteById(Integer id);

    List<Puja> findByPublicacionId(Integer publicacionId);

    // valida las reglas de la subasta y registra la puja
    Puja pujar(Integer publicacionId, Integer usuarioId, Integer valor);
}
