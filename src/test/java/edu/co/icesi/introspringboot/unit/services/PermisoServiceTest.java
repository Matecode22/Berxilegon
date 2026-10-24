package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolPermisoRepository;
import edu.co.icesi.introspringboot.service.impl.PermisoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermisoServiceTest {

    @Mock
    private PermisoRepository permisoRepository;
    @Mock
    private RolPermisoRepository rolPermisoRepository;

    @InjectMocks
    private PermisoServiceImpl permisoService;

    private Permiso permiso;

    void scenario1() {
        permiso = new Permiso();
        permiso.setNombre("PUJAR");
        permiso.setDescripcion("Participar en subastas");
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllPermisos() {
        when(permisoRepository.findAll()).thenReturn(Arrays.asList(new Permiso(), new Permiso()));

        assertEquals(2, permisoService.findAll().size());
    }

    @Test
    void create_WhenIsValid_ReturnPermiso() {
        scenario1();
        when(permisoRepository.existsByNombre("PUJAR")).thenReturn(false);
        when(permisoRepository.save(permiso)).thenReturn(permiso);

        assertNotNull(permisoService.create(permiso));
    }

    @Test
    void create_WhenAlreadyExists_ThrowsException() {
        scenario1();
        when(permisoRepository.existsByNombre("PUJAR")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> permisoService.create(permiso));

        verify(permisoRepository, never()).save(any(Permiso.class));
    }

    @Test
    void update_WhenExists_ChangesDescripcion() {
        scenario1();
        permiso.setId(11);
        Permiso cambios = new Permiso();
        cambios.setId(11);
        cambios.setNombre("PUJAR");
        cambios.setDescripcion("Nueva descripcion");
        when(permisoRepository.findById(11)).thenReturn(Optional.of(permiso));
        when(permisoRepository.save(permiso)).thenReturn(permiso);

        Permiso updated = permisoService.update(cambios);

        assertEquals("Nueva descripcion", updated.getDescripcion());
    }

    @Test
    void delete_WhenIsCalled_DeletesRolPermisosBeforePermiso() {
        permisoService.delete(11);

        InOrder order = inOrder(rolPermisoRepository, permisoRepository);
        order.verify(rolPermisoRepository).deleteByPermiso_Id(11);
        order.verify(permisoRepository).deleteById(11);
    }

    @Test
    void findByCorreo_ReturnsPermisosOfTheUsuario() {
        scenario1();
        when(permisoRepository.findDistinctByRolPermisos_Rol_UsuarioRoles_Usuario_Correo("maria.lopez@correo.com"))
                .thenReturn(List.of(permiso));

        assertEquals(1, permisoService.findByCorreo("maria.lopez@correo.com").size());
    }
}
