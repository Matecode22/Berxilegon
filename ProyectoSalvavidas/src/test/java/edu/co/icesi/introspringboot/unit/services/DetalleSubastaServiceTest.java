package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.repo.DetalleSubastaRepository;
import edu.co.icesi.introspringboot.service.impl.DetalleSubastaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleSubastaServiceTest {

    @Mock
    private DetalleSubastaRepository detalleSubastaRepository;

    @InjectMocks
    private DetalleSubastaServiceImpl detalleSubastaService;

    private DetalleSubasta detalle;

    void scenario1() {
        detalle = new DetalleSubasta();
        detalle.setPublicacionId(2);
        detalle.setPrecioBase(100000);
        detalle.setIncrementoMinimo(5000);
        detalle.setFechaCierre(LocalDateTime.now().plusDays(3));
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllDetalles() {
        when(detalleSubastaRepository.findAll()).thenReturn(Arrays.asList(new DetalleSubasta(), new DetalleSubasta()));

        assertEquals(2, detalleSubastaService.findAll().size());
    }

    @Test
    void findById_WhenExists_ReturnsDetalle() {
        scenario1();
        when(detalleSubastaRepository.findById(2)).thenReturn(Optional.of(detalle));

        assertEquals(100000, detalleSubastaService.findById(2).orElseThrow().getPrecioBase());
    }

    @Test
    void findById_WhenDoesNotExist_ReturnsEmpty() {
        when(detalleSubastaRepository.findById(99)).thenReturn(Optional.empty());

        assertTrue(detalleSubastaService.findById(99).isEmpty());
    }

    @Test
    void save_WhenIsCalled_ReturnsSavedDetalle() {
        scenario1();
        when(detalleSubastaRepository.save(detalle)).thenReturn(detalle);

        assertSame(detalle, detalleSubastaService.save(detalle));
    }

    @Test
    void deleteById_WhenIsCalled_DeletesDetalle() {
        detalleSubastaService.deleteById(2);

        verify(detalleSubastaRepository).deleteById(2);
    }
}
