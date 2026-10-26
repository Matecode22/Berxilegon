package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.impl.UsuarioRolServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioRolServiceTest {

    @Mock
    private UsuarioRolRepository usuarioRolRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UsuarioRolServiceImpl usuarioRolService;

    private UsuarioRol usuarioRol(int rolId) {
        Rol rol = new Rol();
        rol.setId(rolId);
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setUsuario(new Usuario());
        usuarioRol.setRol(rol);
        return usuarioRol;
    }

    @Test
    void findRolIdsByUsuarioId_ReturnsTheIdsOfItsRoles() {
        when(usuarioRolRepository.findByUsuario_Id(3)).thenReturn(List.of(usuarioRol(2), usuarioRol(3)));

        assertEquals(List.of(2, 3), usuarioRolService.findRolIdsByUsuarioId(3));
    }

    @Test
    void assignRoles_RemovesUncheckedAndAddsNewOnes() {
        UsuarioRol quitar = usuarioRol(2);
        UsuarioRol mantener = usuarioRol(3);
        when(usuarioRolRepository.findByUsuario_Id(3)).thenReturn(List.of(quitar, mantener));
        when(usuarioRepository.getReferenceById(3)).thenReturn(new Usuario());
        when(rolRepository.getReferenceById(1)).thenReturn(new Rol());

        usuarioRolService.assignRoles(3, List.of(3, 1));

        verify(usuarioRolRepository).delete(quitar);
        verify(usuarioRolRepository, never()).delete(mantener);
        verify(usuarioRolRepository, times(1)).save(any(UsuarioRol.class));
    }

    @Test
    void assignRoles_WhenNothingIsChecked_RemovesAllRoles() {
        UsuarioRol unico = usuarioRol(3);
        when(usuarioRolRepository.findByUsuario_Id(5)).thenReturn(List.of(unico));

        usuarioRolService.assignRoles(5, List.of());

        verify(usuarioRolRepository).delete(unico);
        verify(usuarioRolRepository, never()).save(any(UsuarioRol.class));
    }
}
