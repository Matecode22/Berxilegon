package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    List<Categoria> findAll();

    Optional<Categoria> findById(Integer id);

    Categoria save(Categoria categoria);

    void deleteById(Integer id);
}
