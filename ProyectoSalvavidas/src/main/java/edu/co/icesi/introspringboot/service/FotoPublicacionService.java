package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.FotoPublicacion;

import java.util.List;
import java.util.Optional;

public interface FotoPublicacionService {

    List<FotoPublicacion> findAll();

    Optional<FotoPublicacion> findById(Integer id);

    FotoPublicacion save(FotoPublicacion foto);

    void deleteById(Integer id);

    List<FotoPublicacion> findByPublicacionId(Integer publicacionId);
}
