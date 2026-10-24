package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.api.dto.PublicacionDTO;
import edu.co.icesi.introspringboot.entity.Publicacion;

import java.util.List;

public interface PublicacionService {

    Publicacion createPublicacion(Publicacion publicacion);

    void deletePublicacion(Integer id);

    List<Publicacion> getAll();

    Publicacion getById(Integer id);

    List<PublicacionDTO> getAllRest();

    Publicacion darDeBaja(Integer id, String motivo);

    // cierra una subasta vencida: la puja mas alta queda como compra PENDIENTE
    Publicacion cerrarSubasta(Integer id);
}
