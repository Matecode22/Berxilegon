package edu.co.icesi.introspringboot.unit.services;


import edu.co.icesi.introspringboot.entity.Categoria;
import edu.co.icesi.introspringboot.entity.Compra;
import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Puja;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.CategoriaRepository;
import edu.co.icesi.introspringboot.repo.CompraRepository;
import edu.co.icesi.introspringboot.repo.DetalleSubastaRepository;
import edu.co.icesi.introspringboot.repo.DetalleVentaRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.PujaRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.impl.PublicacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublicacionServiceTest {

    @Mock
    private PublicacionRepository publicacionRepository;
    // Este objeto lo controla el ingeniero de testing
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @Mock
    private DetalleSubastaRepository detalleSubastaRepository;
    @Mock
    private PujaRepository pujaRepository;
    @Mock
    private CompraRepository compraRepository;

    @InjectMocks
    private PublicacionServiceImpl publicacionService;


    private Publicacion publicacion;
    private Usuario usuario;
    private Categoria categoria;

    void scenario1(){
        usuario = new Usuario();
        usuario.setId(2);
        usuario.setNombre("Maria Lopez");

        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Tecnologia");

        publicacion = new Publicacion();
        publicacion.setId(1);
        publicacion.setUsuario(usuario);
        publicacion.setCategoria(categoria);
        publicacion.setTitulo("Consola Nintendo Switch");
        publicacion.setEstadoConservacion("BUENO");
        publicacion.setModoOferta("INTERCAMBIO");
    }

    void scenarioSubastaVencida(){
        scenario1();
        publicacion.setModoOferta("SUBASTA");
        publicacion.setEstado("ACTIVA");
        DetalleSubasta detalle = new DetalleSubasta();
        detalle.setPrecioBase(300000);
        detalle.setIncrementoMinimo(20000);
        detalle.setFechaCierre(LocalDateTime.now().minusDays(1));
        publicacion.setDetalleSubasta(detalle);
        when(publicacionRepository.findById(1)).thenReturn(Optional.of(publicacion));
        when(publicacionRepository.save(publicacion)).thenReturn(publicacion);
    }


    @Test
    void createPublicacion_WhenIsValid_ReturnPublicacionActiva() {
        //Arrange
        scenario1();
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(publicacionRepository.save(publicacion)).thenReturn(publicacion);

        //Act
        Publicacion saved = publicacionService.createPublicacion(publicacion);
        //Assert
        assertNotNull(saved);
        assertEquals("ACTIVA", saved.getEstado());
        assertNotNull(saved.getFechaPublicacion());
        verifyNoInteractions(detalleVentaRepository, detalleSubastaRepository);
    }

    @Test
    void createPublicacion_WhenIsVenta_SavesDetalleVenta() {
        scenario1();
        publicacion.setModoOferta("VENTA");
        DetalleVenta detalle = new DetalleVenta();
        detalle.setPrecio(1800000);
        publicacion.setDetalleVenta(detalle);
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(publicacionRepository.save(publicacion)).thenReturn(publicacion);
        when(detalleVentaRepository.save(detalle)).thenReturn(detalle);

        Publicacion saved = publicacionService.createPublicacion(publicacion);

        assertEquals(1800000, saved.getDetalleVenta().getPrecio());
        assertFalse(saved.getDetalleVenta().getElegibleIntercambio());
        assertSame(saved, detalle.getPublicacion());
    }

    @Test
    void createPublicacion_WhenVentaHasNoPrecio_ThrowsException() {
        scenario1();
        publicacion.setModoOferta("VENTA");
        publicacion.setDetalleVenta(new DetalleVenta());
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        assertThrows(IllegalArgumentException.class, () -> publicacionService.createPublicacion(publicacion));

        verify(publicacionRepository, never()).save(any(Publicacion.class));
    }

    @Test
    void createPublicacion_WhenTituloIsNull_ThrowsException() {
        scenario1();
        publicacion.setTitulo(null);

        assertThrows(IllegalStateException.class, () -> publicacionService.createPublicacion(publicacion));

        verifyNoInteractions(publicacionRepository);
    }

    @Test
    void createPublicacion_WhenCategoriaDoesNotExist_ThrowsException() {
        scenario1();
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> publicacionService.createPublicacion(publicacion));
    }

    @Test
    void getAll_WhenIsCalled_ReturnAllPublicaciones() {
        //Arrange: simular el comportamiento del mock
        when(publicacionRepository.findAll())
                .thenReturn(Arrays.asList(
                        new Publicacion(),
                        new Publicacion(),
                        new Publicacion()
                ));

        //Act
        List<Publicacion> publicaciones = publicacionService.getAll();
        //Assert
        assertEquals(3, publicaciones.size());
    }

    @Test
    void getById_WhenDoesNotExist_ThrowsException() {
        when(publicacionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> publicacionService.getById(99));
    }

    @Test
    void darDeBaja_WhenActiva_ChangesEstadoAndSavesMotivo() {
        scenario1();
        publicacion.setEstado("ACTIVA");
        when(publicacionRepository.findById(1)).thenReturn(Optional.of(publicacion));
        when(publicacionRepository.save(publicacion)).thenReturn(publicacion);

        Publicacion baja = publicacionService.darDeBaja(1, "Ya no esta disponible");

        assertEquals("DADA_DE_BAJA", baja.getEstado());
        assertEquals("Ya no esta disponible", baja.getMotivoBaja());
    }

    @Test
    void cerrarSubasta_WithPujas_CreatesCompraPendiente() {
        scenarioSubastaVencida();
        Usuario ganador = new Usuario();
        ganador.setId(5);
        Puja puja = new Puja();
        puja.setUsuario(ganador);
        puja.setValor(340000);
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(1)).thenReturn(Optional.of(puja));

        Publicacion cerrada = publicacionService.cerrarSubasta(1);

        assertEquals("RESERVADA", cerrada.getEstado());
        verify(compraRepository).save(any(Compra.class));
    }

    @Test
    void cerrarSubasta_WithoutPujas_EndsFinalizada() {
        scenarioSubastaVencida();
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(1)).thenReturn(Optional.empty());

        Publicacion cerrada = publicacionService.cerrarSubasta(1);

        assertEquals("FINALIZADA", cerrada.getEstado());
        verifyNoInteractions(compraRepository);
    }
}
