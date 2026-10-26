package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    // rol que recibe quien se registra por su cuenta
    public static final String ROL_POR_DEFECTO = "ROLE_USUARIO";

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public Usuario create(Usuario usuario) {
        return guardarNuevo(usuario);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public Usuario update(Usuario usuario) {
        Usuario existente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new RuntimeException("Usuario with id " + usuario.getId() + " not found"));
        if (usuario.getCorreo() != null && !usuario.getCorreo().equals(existente.getCorreo())
                && usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Usuario with correo " + usuario.getCorreo() + " already exists");
        }
        existente.setNombre(usuario.getNombre());
        existente.setCorreo(usuario.getCorreo());
        existente.setFoto(usuario.getFoto());
        existente.setCiudad(usuario.getCiudad());
        existente.setContacto(usuario.getContacto());
        if (usuario.getEstado() != null) {
            existente.setEstado(usuario.getEstado());
        }
        // solo se cambia la contrasena si se envio una nueva, y siempre hasheada
        if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
            existente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        return usuarioRepository.save(existente);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public void delete(Integer id) {
        usuarioRolRepository.deleteByUsuario_Id(id);
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        Optional<Usuario> foundUsuario = usuarioRepository.findByCorreo(correo);
        return foundUsuario.orElseThrow(() -> new RuntimeException("Usuario not found!"));
    }

    // Publico (sin @PreAuthorize): lo usa la pagina de registro
    @Override
    @Transactional
    public Usuario registrar(Usuario usuario) {
        Rol rolPorDefecto = rolRepository.findByNombre(ROL_POR_DEFECTO)
                .orElseThrow(() -> new RuntimeException("Rol " + ROL_POR_DEFECTO + " not found"));
        Usuario guardado = guardarNuevo(usuario);

        UsuarioRolId id = new UsuarioRolId();
        id.setUsuarioId(guardado.getId());
        id.setRolId(rolPorDefecto.getId());
        UsuarioRol usuarioRol = new UsuarioRol();
        usuarioRol.setId(id);
        usuarioRol.setUsuario(guardado);
        usuarioRol.setRol(rolPorDefecto);
        usuarioRolRepository.save(usuarioRol);
        return guardado;
    }

    private Usuario guardarNuevo(Usuario usuario) {
        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new IllegalArgumentException("Usuario correo is required");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().isBlank()) {
            throw new IllegalArgumentException("Usuario contrasena is required");
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Usuario with correo " + usuario.getCorreo() + " already exists");
        }
        // nunca se guarda la contrasena en texto plano
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuario.setFechaRegistro(LocalDateTime.now());
        if (usuario.getEstado() == null) {
            usuario.setEstado("ACTIVO");
        }
        return usuarioRepository.save(usuario);
    }
}
