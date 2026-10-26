package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Puja;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.PujaRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.impl.PujaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PujaServiceTest {

    @Mock
    private PujaRepository pujaRepository;
    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PujaServiceImpl pujaService;

    private Publicacion subasta;
    private Usuario dueno;
    private Usuario postor;

    // subasta ACTIVA del usuario 1 con precio base 100 e incremento minimo 10; puja el usuario 2
    void scenario1() {
        dueno = new Usuario();
        dueno.setId(1);
        postor = new Usuario();
        postor.setId(2);

        DetalleSubasta detalle = new DetalleSubasta();
        detalle.setPrecioBase(100);
        detalle.setIncrementoMinimo(10);
        detalle.setFechaCierre(LocalDateTime.now().plusDays(1));

        subasta = new Publicacion();
        subasta.setId(5);
        subasta.setUsuario(dueno);
        subasta.setModoOferta("SUBASTA");
        subasta.setEstado("ACTIVA");
        subasta.setDetalleSubasta(detalle);

        when(publicacionRepository.findById(5)).thenReturn(Optional.of(subasta));
        // lenient: el caso del dueno puja con el usuario 1 y no usa este stub
        lenient().when(usuarioRepository.findById(2)).thenReturn(Optional.of(postor));
    }

    private Puja puja(int valor) {
        Puja puja = new Puja();
        puja.setValor(valor);
        return puja;
    }

    @Test
    void findByPublicacionId_ReturnsPujasOrderedByValor() {
        List<Puja> pujas = List.of(puja(150), puja(120));
        when(pujaRepository.findByPublicacion_IdOrderByValorDesc(5)).thenReturn(pujas);

        assertEquals(pujas, pujaService.findByPublicacionId(5));
    }

    @Test
    void pujar_WhenValorIsNotPositive_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> pujaService.pujar(5, 2, 0));

        verifyNoInteractions(pujaRepository, publicacionRepository, usuarioRepository);
    }

    @Test
    void pujar_WhenPublicacionDoesNotExist_ThrowsException() {
        when(publicacionRepository.findById(5)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pujaService.pujar(5, 2, 100));
    }

    @Test
    void pujar_WhenFirstBidCoversPrecioBase_SavesPuja() {
        scenario1();
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(5)).thenReturn(Optional.empty());
        when(pujaRepository.save(any(Puja.class))).thenAnswer(inv -> inv.getArgument(0));

        Puja saved = pujaService.pujar(5, 2, 100);

        assertEquals(100, saved.getValor());
        assertSame(postor, saved.getUsuario());
        assertSame(subasta, saved.getPublicacion());
        assertNotNull(saved.getFechaHora());
    }

    @Test
    void pujar_WhenFirstBidIsBelowPrecioBase_ThrowsException() {
        scenario1();
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(5)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pujaService.pujar(5, 2, 99));

        verify(pujaRepository, never()).save(any(Puja.class));
    }

    @Test
    void pujar_WhenBidDoesNotReachHighestPlusIncremento_ThrowsException() {
        scenario1();
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(5)).thenReturn(Optional.of(puja(150)));

        assertThrows(IllegalArgumentException.class, () -> pujaService.pujar(5, 2, 159));

        verify(pujaRepository, never()).save(any(Puja.class));
    }

    @Test
    void pujar_WhenBidReachesHighestPlusIncremento_SavesPuja() {
        scenario1();
        when(pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(5)).thenReturn(Optional.of(puja(150)));
        when(pujaRepository.save(any(Puja.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals(160, pujaService.pujar(5, 2, 160).getValor());
    }

    @Test
    void pujar_WhenPublicacionIsNotSubasta_ThrowsException() {
        scenario1();
        subasta.setModoOferta("VENTA");

        assertThrows(IllegalStateException.class, () -> pujaService.pujar(5, 2, 100));
    }

    @Test
    void pujar_WhenSubastaIsClosed_ThrowsException() {
        scenario1();
        subasta.getDetalleSubasta().setFechaCierre(LocalDateTime.now().minusMinutes(1));

        assertThrows(IllegalStateException.class, () -> pujaService.pujar(5, 2, 100));
    }

    @Test
    void pujar_WhenOwnerBidsOnOwnSubasta_ThrowsException() {
        scenario1();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(dueno));

        assertThrows(IllegalStateException.class, () -> pujaService.pujar(5, 1, 100));

        verify(pujaRepository, never()).save(any(Puja.class));
    }
}
