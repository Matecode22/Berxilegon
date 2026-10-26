package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Categoria;
import edu.co.icesi.introspringboot.repo.CategoriaRepository;
import edu.co.icesi.introspringboot.service.impl.CategoriaServiceImpl;
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
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

    private Categoria categoria;

    void scenario1() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Tecnologia");
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllCategorias() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(new Categoria(), new Categoria()));

        assertEquals(2, categoriaService.findAll().size());
    }

    @Test
    void findById_WhenExists_ReturnsCategoria() {
        scenario1();
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        assertEquals("Tecnologia", categoriaService.findById(1).orElseThrow().getNombre());
    }

    @Test
    void findById_WhenDoesNotExist_ReturnsEmpty() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertTrue(categoriaService.findById(99).isEmpty());
    }

    @Test
    void save_WhenIsCalled_ReturnsSavedCategoria() {
        scenario1();
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        assertSame(categoria, categoriaService.save(categoria));
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteById_WhenIsCalled_DeletesCategoria() {
        categoriaService.deleteById(1);

        verify(categoriaRepository).deleteById(1);
    }
}
