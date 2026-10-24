package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    // preguntas de una publicacion (sin las respuestas)
    List<Comentario> findByPublicacion_IdAndPadreIsNullOrderByFechaAsc(Integer publicacionId);
}
