package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.repo.DetalleVentaRepository;
import edu.co.icesi.introspringboot.service.impl.DetalleVentaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleVentaServiceTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    private DetalleVenta detalle;

    void scenario1() {
        detalle = new DetalleVenta();
        detalle.setPublicacionId(1);
        detalle.setPrecio(250000);
        detalle.setElegibleIntercambio(true);
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllDetalles() {
        when(detalleVentaRepository.findAll()).thenReturn(Arrays.asList(new DetalleVenta(), new DetalleVenta()));

        assertEquals(2, detalleVentaService.findAll().size());
    }

    @Test
    void findById_WhenExists_ReturnsDetalle() {
        scenario1();
        when(detalleVentaRepository.findById(1)).thenReturn(Optional.of(detalle));

        assertEquals(250000, detalleVentaService.findById(1).orElseThrow().getPrecio());
    }

    @Test
    void findById_WhenDoesNotExist_ReturnsEmpty() {
        when(detalleVentaRepository.findById(99)).thenReturn(Optional.empty());

        assertTrue(detalleVentaService.findById(99).isEmpty());
    }

    @Test
    void save_WhenIsCalled_ReturnsSavedDetalle() {
        scenario1();
        when(detalleVentaRepository.save(detalle)).thenReturn(detalle);

        assertSame(detalle, detalleVentaService.save(detalle));
    }

    @Test
    void deleteById_WhenIsCalled_DeletesDetalle() {
        detalleVentaService.deleteById(1);

        verify(detalleVentaRepository).deleteById(1);
    }
}
