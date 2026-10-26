package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Compra;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.CompraRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.impl.CompraServiceImpl;
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
public class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;
    @Mock
    private PublicacionRepository publicacionRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CompraServiceImpl compraService;

    private Publicacion venta;
    private Usuario vendedor;
    private Usuario comprador;

    // venta ACTIVA del usuario 1; compra el usuario 2
    void scenario1() {
        vendedor = new Usuario();
        vendedor.setId(1);
        comprador = new Usuario();
        comprador.setId(2);

        venta = new Publicacion();
        venta.setId(3);
        venta.setUsuario(vendedor);
        venta.setModoOferta("VENTA");
        venta.setEstado("ACTIVA");
    }

    private Compra compraPendiente(Publicacion publicacion) {
        Compra compra = new Compra();
        compra.setId(8);
        compra.setPublicacion(publicacion);
        compra.setEstado("PENDIENTE");
        return compra;
    }

    @Test
    void findByUsuarioId_ReturnsComprasOfTheUsuario() {
        List<Compra> compras = List.of(new Compra(), new Compra());
        when(compraRepository.findByUsuario_Id(2)).thenReturn(compras);

        assertEquals(compras, compraService.findByUsuarioId(2));
    }

    @Test
    void comprar_WhenIsValid_CreatesPendingCompraAndReservesPublicacion() {
        scenario1();
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(venta));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(comprador));
        when(compraRepository.save(any(Compra.class))).thenAnswer(inv -> inv.getArgument(0));

        Compra compra = compraService.comprar(3, 2);

        assertEquals("PENDIENTE", compra.getEstado());
        assertSame(comprador, compra.getUsuario());
        assertNotNull(compra.getFechaCompra());
        assertEquals("RESERVADA", venta.getEstado());
        verify(publicacionRepository).save(venta);
    }

    @Test
    void comprar_WhenPublicacionIsNotActivaVenta_ThrowsException() {
        scenario1();
        venta.setEstado("VENDIDA");
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(venta));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(comprador));

        assertThrows(IllegalStateException.class, () -> compraService.comprar(3, 2));

        verify(compraRepository, never()).save(any(Compra.class));
    }

    @Test
    void comprar_WhenBuyerIsTheOwner_ThrowsException() {
        scenario1();
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(venta));
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(vendedor));

        assertThrows(IllegalStateException.class, () -> compraService.comprar(3, 1));
    }

    @Test
    void comprar_WhenUsuarioDoesNotExist_ThrowsException() {
        scenario1();
        when(publicacionRepository.findById(3)).thenReturn(Optional.of(venta));
        when(usuarioRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> compraService.comprar(3, 2));
    }

    @Test
    void confirmar_WhenCompraIsPending_CompletesCompraAndSellsPublicacion() {
        scenario1();
        venta.setEstado("RESERVADA");
        Compra compra = compraPendiente(venta);
        when(compraRepository.findById(8)).thenReturn(Optional.of(compra));
        when(compraRepository.save(compra)).thenReturn(compra);

        compraService.confirmar(8);

        assertEquals("COMPLETADA", compra.getEstado());
        assertEquals("VENDIDA", venta.getEstado());
    }

    @Test
    void confirmar_WhenCompraIsNotPending_ThrowsException() {
        scenario1();
        Compra compra = compraPendiente(venta);
        compra.setEstado("CANCELADA");
        when(compraRepository.findById(8)).thenReturn(Optional.of(compra));

        assertThrows(IllegalStateException.class, () -> compraService.confirmar(8));
    }

    @Test
    void cancelar_WhenMotivoIsBlank_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> compraService.cancelar(8, " "));

        verifyNoInteractions(compraRepository);
    }

    @Test
    void cancelar_WhenPublicacionIsVenta_ReturnsItToActiva() {
        scenario1();
        venta.setEstado("RESERVADA");
        Compra compra = compraPendiente(venta);
        when(compraRepository.findById(8)).thenReturn(Optional.of(compra));
        when(compraRepository.save(compra)).thenReturn(compra);

        compraService.cancelar(8, "El comprador no respondio");

        assertEquals("CANCELADA", compra.getEstado());
        assertEquals("El comprador no respondio", compra.getMotivoCancelacion());
        assertEquals("ACTIVA", venta.getEstado());
    }

    @Test
    void cancelar_WhenPublicacionIsSubasta_FinishesIt() {
        scenario1();
        venta.setModoOferta("SUBASTA");
        Compra compra = compraPendiente(venta);
        when(compraRepository.findById(8)).thenReturn(Optional.of(compra));
        when(compraRepository.save(compra)).thenReturn(compra);

        compraService.cancelar(8, "No pago");

        assertEquals("FINALIZADA", venta.getEstado());
    }
}
