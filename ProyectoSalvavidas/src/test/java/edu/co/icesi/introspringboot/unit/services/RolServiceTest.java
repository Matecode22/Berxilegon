package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.RolPermiso;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolPermisoRepository;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.impl.RolServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class RolServiceTest {

    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioRolRepository usuarioRolRepository;
    @Mock
    private RolPermisoRepository rolPermisoRepository;
    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rol;

    void scenario1() {
        rol = new Rol();
        rol.setId(2);
        rol.setNombre("ROLE_MODERADOR");
    }

    private RolPermiso rolPermiso(Rol rol, int permisoId) {
        Permiso permiso = new Permiso();
        permiso.setId(permisoId);
        RolPermiso rolPermiso = new RolPermiso();
        rolPermiso.setRol(rol);
        rolPermiso.setPermiso(permiso);
        return rolPermiso;
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllRoles() {
        when(rolRepository.findAll()).thenReturn(Arrays.asList(new Rol(), new Rol(), new Rol()));

        assertEquals(3, rolService.findAll().size());
    }

    @Test
    void create_WhenNameHasNoPrefix_AddsRolePrefix() {
        Rol nuevo = new Rol();
        nuevo.setNombre("vendedor");
        when(rolRepository.existsByNombre("ROLE_VENDEDOR")).thenReturn(false);
        when(rolRepository.save(nuevo)).thenReturn(nuevo);

        Rol saved = rolService.create(nuevo);

        assertEquals("ROLE_VENDEDOR", saved.getNombre());
    }

    @Test
    void create_WhenRolAlreadyExists_ThrowsException() {
        Rol nuevo = new Rol();
        nuevo.setNombre("ROLE_ADMIN");
        when(rolRepository.existsByNombre("ROLE_ADMIN")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> rolService.create(nuevo));

        verify(rolRepository, never()).save(any(Rol.class));
    }

    @Test
    void create_WhenNameIsEmpty_ThrowsException() {
        Rol nuevo = new Rol();

        assertThrows(IllegalArgumentException.class, () -> rolService.create(nuevo));

        verifyNoInteractions(rolRepository);
    }

    @Test
    void update_WhenRolDoesNotExist_ThrowsException() {
        scenario1();
        when(rolRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> rolService.update(rol));
    }

    @Test
    void delete_WhenIsCalled_DeletesAssociationsAndRol() {
        rolService.delete(2);

        verify(usuarioRolRepository).deleteByRol_Id(2);
        verify(rolPermisoRepository).deleteByRol_Id(2);
        verify(rolRepository).deleteById(2);
    }

    @Test
    void findPermisoIdsByRolId_ReturnsTheIdsOfItsPermisos() {
        scenario1();
        when(rolPermisoRepository.findByRol_Id(2)).thenReturn(List.of(rolPermiso(rol, 6), rolPermiso(rol, 9)));

        assertEquals(List.of(6, 9), rolService.findPermisoIdsByRolId(2));
    }

    @Test
    void updatePermisos_RemovesUncheckedAndAddsNewOnes() {
        scenario1();
        RolPermiso quitar = rolPermiso(rol, 6);
        RolPermiso mantener = rolPermiso(rol, 9);
        when(rolPermisoRepository.findByRol_Id(2)).thenReturn(List.of(quitar, mantener));
        when(rolRepository.getReferenceById(2)).thenReturn(rol);
        when(permisoRepository.getReferenceById(14)).thenReturn(new Permiso());

        rolService.updatePermisos(2, List.of(9, 14));

        verify(rolPermisoRepository).delete(quitar);
        verify(rolPermisoRepository, never()).delete(mantener);
        verify(rolPermisoRepository, times(1)).save(any(RolPermiso.class));
    }
}
