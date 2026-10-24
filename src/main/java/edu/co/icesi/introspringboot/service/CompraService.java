package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Compra;

import java.util.List;
import java.util.Optional;

public interface CompraService {

    List<Compra> findAll();

    Optional<Compra> findById(Integer id);

    Compra save(Compra compra);

    void deleteById(Integer id);

    List<Compra> findByUsuarioId(Integer usuarioId);

    // compra directa de una publicacion en VENTA: queda PENDIENTE y la publicacion RESERVADA
    Compra comprar(Integer publicacionId, Integer usuarioId);

    Compra confirmar(Integer id);

    Compra cancelar(Integer id, String motivo);
}
