package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import edu.co.icesi.introspringboot.repo.ArticuloOfertadoRepository;
import edu.co.icesi.introspringboot.service.impl.ArticuloOfertadoServiceImpl;
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
public class ArticuloOfertadoServiceTest {

    @Mock
    private ArticuloOfertadoRepository articuloOfertadoRepository;

    @InjectMocks
    private ArticuloOfertadoServiceImpl articuloOfertadoService;

    private ArticuloOfertadoId id;
    private ArticuloOfertado articulo;

    void scenario1() {
        id = new ArticuloOfertadoId();
        id.setOfertaId(1);
        id.setPublicacionOfrecidaId(7);
        articulo = new ArticuloOfertado();
        articulo.setId(id);
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllArticulos() {
        when(articuloOfertadoRepository.findAll()).thenReturn(Arrays.asList(new ArticuloOfertado(), new ArticuloOfertado()));

        assertEquals(2, articuloOfertadoService.findAll().size());
    }

    @Test
    void findById_WhenExists_ReturnsArticulo() {
        scenario1();
        when(articuloOfertadoRepository.findById(id)).thenReturn(Optional.of(articulo));

        assertSame(articulo, articuloOfertadoService.findById(id).orElseThrow());
    }

    @Test
    void save_WhenIsCalled_ReturnsSavedArticulo() {
        scenario1();
        when(articuloOfertadoRepository.save(articulo)).thenReturn(articulo);

        assertSame(articulo, articuloOfertadoService.save(articulo));
    }

    @Test
    void deleteById_WhenIsCalled_DeletesArticuloByCompositeKey() {
        scenario1();

        articuloOfertadoService.deleteById(id);

        verify(articuloOfertadoRepository).deleteById(id);
    }
}
