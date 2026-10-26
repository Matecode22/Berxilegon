package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.FotoPublicacion;
import edu.co.icesi.introspringboot.repo.FotoPublicacionRepository;
import edu.co.icesi.introspringboot.service.impl.FotoPublicacionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FotoPublicacionServiceTest {

    @Mock
    private FotoPublicacionRepository fotoPublicacionRepository;

    @InjectMocks
    private FotoPublicacionServiceImpl fotoPublicacionService;

    private FotoPublicacion foto(int id, int orden) {
        FotoPublicacion foto = new FotoPublicacion();
        foto.setId(id);
        foto.setUrl("https://img/" + id + ".jpg");
        foto.setOrden(orden);
        return foto;
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllFotos() {
        when(fotoPublicacionRepository.findAll()).thenReturn(Arrays.asList(foto(1, 1), foto(2, 2), foto(3, 3)));

        assertEquals(3, fotoPublicacionService.findAll().size());
    }

    @Test
    void findById_WhenExists_ReturnsFoto() {
        when(fotoPublicacionRepository.findById(1)).thenReturn(Optional.of(foto(1, 1)));

        assertTrue(fotoPublicacionService.findById(1).isPresent());
    }

    @Test
    void save_WhenIsCalled_ReturnsSavedFoto() {
        FotoPublicacion nueva = foto(4, 1);
        when(fotoPublicacionRepository.save(nueva)).thenReturn(nueva);

        assertSame(nueva, fotoPublicacionService.save(nueva));
    }

    @Test
    void deleteById_WhenIsCalled_DeletesFoto() {
        fotoPublicacionService.deleteById(1);

        verify(fotoPublicacionRepository).deleteById(1);
    }

    @Test
    void findByPublicacionId_ReturnsFotosOrderedByOrden() {
        List<FotoPublicacion> ordenadas = List.of(foto(5, 1), foto(6, 2));
        when(fotoPublicacionRepository.findByPublicacion_IdOrderByOrdenAsc(10)).thenReturn(ordenadas);

        assertEquals(ordenadas, fotoPublicacionService.findByPublicacionId(10));
    }
}
