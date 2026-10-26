package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Comentario;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.ComentarioRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.impl.ComentarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;
    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComentarioServiceImpl comentarioService;

    private Publicacion publicacion;
    private Usuario autor;

    void scenario1() {
        publicacion = new Publicacion();
        publicacion.setId(3);
        autor = new Usuario();
        autor.setId(2);

        when(publicacionRepository.findById(3)).thenReturn(Optional.of(publicacion));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(autor));
    }

    private Comentario comentarioDe(int id, Publicacion publicacion) {
        Comentario comentario = new Comentario();
        comentario.setId(id);
        comentario.setPublicacion(publicacion);
        return comentario;
    }

    @Test
    void findPreguntas_ReturnsOnlyRootComentarios() {
        List<Comentario> preguntas = List.of(new Comentario(), new Comentario());
        when(comentarioRepository.findByPublicacion_IdAndPadreIsNullOrderByFechaAsc(3)).thenReturn(preguntas);

        assertEquals(preguntas, comentarioService.findPreguntas(3));
    }

    @Test
    void comentar_WhenTextoIsBlank_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> comentarioService.comentar(3, 2, "  ", null));

        verifyNoInteractions(comentarioRepository, publicacionRepository, usuarioRepository);
    }

    @Test
    void comentar_WhenHasNoPadre_SavesPregunta() {
        scenario1();
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(inv -> inv.getArgument(0));

        Comentario saved = comentarioService.comentar(3, 2, "Sigue disponible?", null);

        assertEquals("Sigue disponible?", saved.getTexto());
        assertSame(publicacion, saved.getPublicacion());
        assertSame(autor, saved.getUsuario());
        assertNull(saved.getPadre());
        assertNotNull(saved.getFecha());
    }

    @Test
    void comentar_WhenHasPadre_SavesRespuesta() {
        scenario1();
        Comentario padre = comentarioDe(10, publicacion);
        when(comentarioRepository.findById(10)).thenReturn(Optional.of(padre));
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(inv -> inv.getArgument(0));

        Comentario saved = comentarioService.comentar(3, 2, "Si, aun esta", 10);

        assertSame(padre, saved.getPadre());
    }

    @Test
    void comentar_WhenPadreBelongsToAnotherPublicacion_ThrowsException() {
        scenario1();
        Publicacion otra = new Publicacion();
        otra.setId(9);
        when(comentarioRepository.findById(10)).thenReturn(Optional.of(comentarioDe(10, otra)));

        assertThrows(IllegalStateException.class, () -> comentarioService.comentar(3, 2, "Respuesta", 10));

        verify(comentarioRepository, never()).save(any(Comentario.class));
    }

    @Test
    void comentar_WhenPublicacionDoesNotExist_ThrowsException() {
        when(publicacionRepository.findById(3)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> comentarioService.comentar(3, 2, "Hola", null));
    }
}
