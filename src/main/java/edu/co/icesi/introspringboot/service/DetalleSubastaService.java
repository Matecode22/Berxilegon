package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.DetalleSubasta;

import java.util.List;
import java.util.Optional;

public interface DetalleSubastaService {

    List<DetalleSubasta> findAll();

    Optional<DetalleSubasta> findById(Integer id);

    DetalleSubasta save(DetalleSubasta detalleSubasta);

    void deleteById(Integer id);
}
