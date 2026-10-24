package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Comentario;

import java.util.List;
import java.util.Optional;

public interface ComentarioService {

    List<Comentario> findAll();

    Optional<Comentario> findById(Integer id);

    Comentario save(Comentario comentario);

    void deleteById(Integer id);

    // preguntas de la publicacion; cada una trae sus respuestas
    List<Comentario> findPreguntas(Integer publicacionId);

    Comentario comentar(Integer publicacionId, Integer usuarioId, String texto, Integer padreId);
}
