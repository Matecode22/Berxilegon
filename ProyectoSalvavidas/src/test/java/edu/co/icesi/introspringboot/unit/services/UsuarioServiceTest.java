package edu.co.icesi.introspringboot.unit.services;

import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioRolRepository usuarioRolRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    void scenario1() {
        usuario = new Usuario();
        usuario.setNombre("Maria Lopez");
        usuario.setCorreo("maria.lopez@correo.com");
        usuario.setContrasena("maria123");
    }

    void scenario2() {
        scenario1();
        usuario.setId(2);
        usuario.setContrasena("$2a$10$hashViejo");
        usuario.setEstado("ACTIVO");
    }

    @Test
    void findAll_WhenIsCalled_ReturnAllUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(new Usuario(), new Usuario()));

        List<Usuario> usuarios = usuarioService.findAll();

        assertEquals(2, usuarios.size());
    }

    @Test
    void findById_WhenExists_ReturnUsuario() {
        scenario2();
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));

        Optional<Usuario> found = usuarioService.findById(2);

        assertTrue(found.isPresent());
        assertEquals("maria.lopez@correo.com", found.get().getCorreo());
    }

    @Test
    void create_WhenIsValid_SavesHashedPassword() {
        //Arrange
        scenario1();
        when(usuarioRepository.existsByCorreo("maria.lopez@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("maria123")).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        //Act
        Usuario saved = usuarioService.create(usuario);
        //Assert: nunca queda la contrasena en texto plano
        assertEquals("$2a$10$hash", saved.getContrasena());
        assertNotEquals("maria123", saved.getContrasena());
        assertEquals("ACTIVO", saved.getEstado());
        assertNotNull(saved.getFechaRegistro());
    }

    @Test
    void create_WhenCorreoAlreadyExists_ThrowsException() {
        scenario1();
        when(usuarioRepository.existsByCorreo("maria.lopez@correo.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> usuarioService.create(usuario));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void create_WhenContrasenaIsEmpty_ThrowsException() {
        scenario1();
        usuario.setContrasena("");

        assertThrows(IllegalArgumentException.class, () -> usuarioService.create(usuario));

        verifyNoInteractions(usuarioRepository, passwordEncoder);
    }

    @Test
    void update_WhenNewContrasena_HashesIt() {
        scenario2();
        Usuario cambios = new Usuario();
        cambios.setId(2);
        cambios.setNombre("Maria L.");
        cambios.setCorreo("maria.lopez@correo.com");
        cambios.setContrasena("nueva123");
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva123")).thenReturn("$2a$10$hashNuevo");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario updated = usuarioService.update(cambios);

        assertEquals("Maria L.", updated.getNombre());
        assertEquals("$2a$10$hashNuevo", updated.getContrasena());
    }

    @Test
    void update_WhenContrasenaIsEmpty_KeepsTheOldHash() {
        scenario2();
        Usuario cambios = new Usuario();
        cambios.setId(2);
        cambios.setNombre("Maria L.");
        cambios.setCorreo("maria.lopez@correo.com");
        cambios.setContrasena("");
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario updated = usuarioService.update(cambios);

        assertEquals("$2a$10$hashViejo", updated.getContrasena());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void update_WhenUsuarioDoesNotExist_ThrowsException() {
        Usuario cambios = new Usuario();
        cambios.setId(99);
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.update(cambios));
    }

    @Test
    void delete_WhenIsCalled_DeletesRolesBeforeUsuario() {
        usuarioService.delete(2);

        InOrder order = inOrder(usuarioRolRepository, usuarioRepository);
        order.verify(usuarioRolRepository).deleteByUsuario_Id(2);
        order.verify(usuarioRepository).deleteById(2);
    }

    @Test
    void findByCorreo_WhenDoesNotExist_ThrowsException() {
        when(usuarioRepository.findByCorreo("nadie@correo.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.findByCorreo("nadie@correo.com"));
    }

    @Test
    void registrar_WhenIsValid_SavesUsuarioWithRolUsuario() {
        scenario1();
        Rol rolUsuario = new Rol();
        rolUsuario.setId(3);
        rolUsuario.setNombre("ROLE_USUARIO");
        when(rolRepository.findByNombre("ROLE_USUARIO")).thenReturn(Optional.of(rolUsuario));
        when(usuarioRepository.existsByCorreo("maria.lopez@correo.com")).thenReturn(false);
        when(passwordEncoder.encode("maria123")).thenReturn("$2a$10$hash");
        when(usuarioRepository.save(usuario)).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(10);
            return u;
        });

        Usuario saved = usuarioService.registrar(usuario);

        assertEquals(10, saved.getId());
        assertEquals("$2a$10$hash", saved.getContrasena());
        verify(usuarioRolRepository).save(any(UsuarioRol.class));
    }

    @Test
    void registrar_WhenRolUsuarioDoesNotExist_ThrowsException() {
        scenario1();
        when(rolRepository.findByNombre("ROLE_USUARIO")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.registrar(usuario));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}
