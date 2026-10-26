package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.security.CustomUserDetailService;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {

    private static final String CORREO = "carlos.ramirez@correo.com";

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    private Usuario usuario;

    void scenario1() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreo(CORREO);
        usuario.setContrasena("$2a$10$hash");
        usuario.setEstado("ACTIVO");

        Rol admin = new Rol();
        admin.setNombre("ROLE_ADMIN");
        Permiso createUser = new Permiso();
        createUser.setNombre("CREATE_USER");

        when(usuarioService.findByCorreo(CORREO)).thenReturn(usuario);
        when(rolRepository.findByUsuarioRoles_Usuario_Correo(CORREO)).thenReturn(List.of(admin));
        when(permisoRepository.findDistinctByRolPermisos_Rol_UsuarioRoles_Usuario_Correo(CORREO))
                .thenReturn(List.of(createUser));
    }

    @Test
    void loadUserByUsername_WhenExists_ReturnsRolesAndPermisosAsAuthorities() {
        scenario1();

        UserDetails userDetails = customUserDetailService.loadUserByUsername(CORREO);

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();
        assertEquals(CORREO, userDetails.getUsername());
        assertEquals("$2a$10$hash", userDetails.getPassword());
        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("CREATE_USER"));
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_WhenUsuarioIsInactivo_ReturnsDisabledUser() {
        scenario1();
        usuario.setEstado("INACTIVO");

        UserDetails userDetails = customUserDetailService.loadUserByUsername(CORREO);

        assertFalse(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_WhenDoesNotExist_ThrowsUsernameNotFound() {
        when(usuarioService.findByCorreo("nadie@correo.com")).thenThrow(new RuntimeException("Usuario not found!"));

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailService.loadUserByUsername("nadie@correo.com"));
    }
}
