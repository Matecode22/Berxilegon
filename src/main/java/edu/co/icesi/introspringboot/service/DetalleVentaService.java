package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaService {

    List<DetalleVenta> findAll();

    Optional<DetalleVenta> findById(Integer id);

    DetalleVenta save(DetalleVenta detalleVenta);

    void deleteById(Integer id);
}
