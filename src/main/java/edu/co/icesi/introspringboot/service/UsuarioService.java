package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Integer id);

    Usuario create(Usuario usuario);

    Usuario update(Usuario usuario);

    void delete(Integer id);

    Usuario findByCorreo(String correo);

    // Registro publico: crea el usuario con el rol ROLE_USUARIO
    Usuario registrar(Usuario usuario);
}
