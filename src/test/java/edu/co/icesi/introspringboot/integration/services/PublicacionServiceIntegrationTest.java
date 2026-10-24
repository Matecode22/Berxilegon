package edu.co.icesi.introspringboot.integration.services;

import edu.co.icesi.introspringboot.entity.Categoria;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.CategoriaRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.PublicacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PublicacionServiceIntegrationTest {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private Publicacion publicacion;
    private Categoria categoria;
    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setNombre("Test Usuario");
        usuario.setCorreo("test@correo.com");
        usuario.setContrasena("$2a$10$hash");
        usuario.setEstado("ACTIVO");
        usuario.setFechaRegistro(LocalDateTime.now());
        usuarioRepository.save(usuario);

        categoria = new Categoria();
        categoria.setNombre("Test Categoria");
        categoriaRepository.save(categoria);

        publicacion = new Publicacion();
        publicacion.setTitulo("Test Publicacion");
        publicacion.setEstadoConservacion("BUENO");
        publicacion.setModoOferta("INTERCAMBIO");
        publicacion.setUsuario(usuario);
        publicacion.setCategoria(categoria);
    }

    @Test
    void createPublicacion_WhenValid_ReturnsSavedPublicacion(){
        //Act
        Publicacion outputPublicacion = publicacionService.createPublicacion(publicacion);
        int publicacionSize = publicacionRepository.findAll().size();
        //Assert
        assertNotNull(outputPublicacion);
        assertNotNull(outputPublicacion.getId());
        assertEquals("ACTIVA", outputPublicacion.getEstado());
        assertEquals(1, publicacionSize);
    }

    @Test
    void createPublicacion_WhenTituloIsNull_ThrowsException(){
        //Arrange
        Publicacion publicacionSinTitulo = new Publicacion();
        publicacionSinTitulo.setModoOferta("INTERCAMBIO");
        publicacionSinTitulo.setUsuario(usuario);
        publicacionSinTitulo.setCategoria(categoria);
        //Act & Assert
        assertThrows(IllegalStateException.class, ()->{
            publicacionService.createPublicacion(publicacionSinTitulo);
        } );
    }

    @AfterEach
    void tearDown(){
        publicacionRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

}
