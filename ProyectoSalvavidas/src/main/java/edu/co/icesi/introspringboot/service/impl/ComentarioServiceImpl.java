package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Comentario;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.ComentarioRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Comentario> findAll() {
        return comentarioRepository.findAll();
    }

    @Override
    public Optional<Comentario> findById(Integer id) {
        return comentarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Comentario save(Comentario comentario) {
        return comentarioRepository.save(comentario);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        comentarioRepository.deleteById(id);
    }

    @Override
    public List<Comentario> findPreguntas(Integer publicacionId) {
        return comentarioRepository.findByPublicacion_IdAndPadreIsNullOrderByFechaAsc(publicacionId);
    }

    @Override
    @Transactional
    public Comentario comentar(Integer publicacionId, Integer usuarioId, String texto, Integer padreId) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Comentario texto is required");
        }
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion with id " + publicacionId + " not found"));
        Usuario autor = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario with id " + usuarioId + " not found"));

        Comentario comentario = new Comentario();
        comentario.setPublicacion(publicacion);
        comentario.setUsuario(autor);
        comentario.setTexto(texto);
        comentario.setFecha(LocalDateTime.now());
        if (padreId != null) {
            Comentario padre = comentarioRepository.findById(padreId)
                    .orElseThrow(() -> new RuntimeException("Comentario with id " + padreId + " not found"));
            if (!padre.getPublicacion().getId().equals(publicacionId)) {
                throw new IllegalStateException("The comentario " + padreId + " belongs to another publicacion");
            }
            comentario.setPadre(padre);
        }
        return comentarioRepository.save(comentario);
    }
}
