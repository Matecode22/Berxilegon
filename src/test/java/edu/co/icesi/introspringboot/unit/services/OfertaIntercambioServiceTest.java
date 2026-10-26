package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.entity.OfertaIntercambio;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.ArticuloOfertadoRepository;
import edu.co.icesi.introspringboot.repo.OfertaIntercambioRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.impl.OfertaIntercambioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfertaIntercambioServiceTest {

    @Mock
    private OfertaIntercambioRepository ofertaIntercambioRepository;
    @Mock
    private ArticuloOfertadoRepository articuloOfertadoRepository;
    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OfertaIntercambioServiceImpl ofertaService;

    private Usuario dueno;
    private Usuario oferente;
    private Publicacion objetivo;
    private Publicacion ofrecida;

    // el usuario 2 ofrece su publicacion 4 por la publicacion 3 (INTERCAMBIO) del usuario 1
    void scenario1() {
        dueno = new Usuario();
        dueno.setId(1);
        oferente = new Usuario();
        oferente.setId(2);
        objetivo = publicacion(3, dueno, "INTERCAMBIO");
        ofrecida = publicacion(4, oferente, "INTERCAMBIO");
    }

    private Publicacion publicacion(int id, Usuario usuario, String modoOferta) {
        Publicacion publicacion = new Publicacion();
        publicacion.setId(id);
        publicacion.setUsuario(usuario);
        publicacion.setModoOferta(modoOferta);
        publicacion.setEstado("ACTIVA");
        return publicacion;
    }

    private OfertaIntercambio oferta(int id, String estado) {
        OfertaIntercambio oferta = new OfertaIntercambio();
        oferta.setId(id);
        oferta.setPublicacion(objetivo);
        oferta.setEstado(estado);
        return oferta;
    }

    private void stubObjetivoYOferente() {
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(objetivo));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(oferente));
    }

    @Test
    void crearOferta_WhenNoArticulosAreOffered_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ofertaService.crearOferta(3, 2, List.of(), 0, null));

        verifyNoInteractions(ofertaIntercambioRepository, publicacionRepository);
    }

    @Test
    void crearOferta_WhenIsValid_SavesPendingOfertaAndItsArticulos() {
        scenario1();
        stubObjetivoYOferente();
        when(publicacionRepository.findById(4)).thenReturn(Optional.of(ofrecida));
        when(ofertaIntercambioRepository.save(any(OfertaIntercambio.class))).thenAnswer(inv -> {
            OfertaIntercambio o = inv.getArgument(0);
            o.setId(50);
            return o;
        });

        OfertaIntercambio saved = ofertaService.crearOferta(3, 2, List.of(4), null, "OFERENTE");

        assertEquals("PENDIENTE", saved.getEstado());
        assertEquals(0, saved.getDiferenciaDinero());
        assertNull(saved.getAFavorDe());
        ArgumentCaptor<ArticuloOfertado> captor = ArgumentCaptor.forClass(ArticuloOfertado.class);
        verify(articuloOfertadoRepository).save(captor.capture());
        assertEquals(50, captor.getValue().getId().getOfertaId());
        assertEquals(4, captor.getValue().getId().getPublicacionOfrecidaId());
    }

    @Test
    void crearOferta_WhenThereIsDiferenciaDinero_KeepsAFavorDe() {
        scenario1();
        stubObjetivoYOferente();
        when(publicacionRepository.findById(4)).thenReturn(Optional.of(ofrecida));
        when(ofertaIntercambioRepository.save(any(OfertaIntercambio.class))).thenAnswer(inv -> inv.getArgument(0));

        OfertaIntercambio saved = ofertaService.crearOferta(3, 2, List.of(4), 20000, "DUENO");

        assertEquals(20000, saved.getDiferenciaDinero());
        assertEquals("DUENO", saved.getAFavorDe());
    }

    @Test
    void crearOferta_WhenVentaIsNotElegibleIntercambio_ThrowsException() {
        scenario1();
        objetivo.setModoOferta("VENTA");
        DetalleVenta detalle = new DetalleVenta();
        detalle.setElegibleIntercambio(false);
        objetivo.setDetalleVenta(detalle);
        stubObjetivoYOferente();

        assertThrows(IllegalStateException.class, () -> ofertaService.crearOferta(3, 2, List.of(4), 0, null));

        verify(ofertaIntercambioRepository, never()).save(any(OfertaIntercambio.class));
    }

    @Test
    void crearOferta_WhenVentaIsElegibleIntercambio_SavesOferta() {
        scenario1();
        objetivo.setModoOferta("VENTA");
        DetalleVenta detalle = new DetalleVenta();
        detalle.setElegibleIntercambio(true);
        objetivo.setDetalleVenta(detalle);
        stubObjetivoYOferente();
        when(publicacionRepository.findById(4)).thenReturn(Optional.of(ofrecida));
        when(ofertaIntercambioRepository.save(any(OfertaIntercambio.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals("PENDIENTE", ofertaService.crearOferta(3, 2, List.of(4), 0, null).getEstado());
    }

    @Test
    void crearOferta_WhenOferenteIsTheOwner_ThrowsException() {
        scenario1();
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(objetivo));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(dueno));

        assertThrows(IllegalStateException.class, () -> ofertaService.crearOferta(3, 1, List.of(4), 0, null));
    }

    @Test
    void crearOferta_WhenOfferedPublicacionBelongsToAnotherUsuario_ThrowsException() {
        scenario1();
        ofrecida.setUsuario(dueno);
        stubObjetivoYOferente();
        when(publicacionRepository.findById(4)).thenReturn(Optional.of(ofrecida));
        when(ofertaIntercambioRepository.save(any(OfertaIntercambio.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThrows(IllegalStateException.class, () -> ofertaService.crearOferta(3, 2, List.of(4), 0, null));

        verify(articuloOfertadoRepository, never()).save(any(ArticuloOfertado.class));
    }

    @Test
    void aceptar_WhenOfertaIsPending_ExchangesPublicacionesAndRejectsOtherOfertas() {
        scenario1();
        OfertaIntercambio aceptada = oferta(50, "PENDIENTE");
        OfertaIntercambio otra = oferta(51, "PENDIENTE");
        ArticuloOfertado articulo = new ArticuloOfertado();
        articulo.setPublicacionOfrecida(ofrecida);
        when(ofertaIntercambioRepository.findById(50)).thenReturn(Optional.of(aceptada));
        when(articuloOfertadoRepository.findByOferta_Id(50)).thenReturn(List.of(articulo));
        when(ofertaIntercambioRepository.findByPublicacion_IdAndEstado(3, "PENDIENTE")).thenReturn(List.of(aceptada, otra));
        when(ofertaIntercambioRepository.save(any(OfertaIntercambio.class))).thenAnswer(inv -> inv.getArgument(0));

        ofertaService.aceptar(50);

        assertEquals("ACEPTADA", aceptada.getEstado());
        assertEquals("INTERCAMBIADA", objetivo.getEstado());
        assertEquals("INTERCAMBIADA", ofrecida.getEstado());
        assertEquals("RECHAZADA", otra.getEstado());
        assertNotNull(otra.getMotivoRechazo());
        verify(ofertaIntercambioRepository).save(otra);
    }

    @Test
    void aceptar_WhenObjetivoIsNoLongerActiva_ThrowsException() {
        scenario1();
        objetivo.setEstado("VENDIDA");
        when(ofertaIntercambioRepository.findById(50)).thenReturn(Optional.of(oferta(50, "PENDIENTE")));

        assertThrows(IllegalStateException.class, () -> ofertaService.aceptar(50));
    }

    @Test
    void aceptar_WhenOfertaIsNotPending_ThrowsException() {
        scenario1();
        when(ofertaIntercambioRepository.findById(50)).thenReturn(Optional.of(oferta(50, "RECHAZADA")));

        assertThrows(IllegalStateException.class, () -> ofertaService.aceptar(50));
    }

    @Test
    void rechazar_WhenMotivoIsBlank_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ofertaService.rechazar(50, ""));

        verifyNoInteractions(ofertaIntercambioRepository);
    }

    @Test
    void rechazar_WhenOfertaIsPending_RejectsItWithMotivo() {
        scenario1();
        OfertaIntercambio oferta = oferta(50, "PENDIENTE");
        when(ofertaIntercambioRepository.findById(50)).thenReturn(Optional.of(oferta));
        when(ofertaIntercambioRepository.save(oferta)).thenReturn(oferta);

        ofertaService.rechazar(50, "No me interesa");

        assertEquals("RECHAZADA", oferta.getEstado());
        assertEquals("No me interesa", oferta.getMotivoRechazo());
    }
}
