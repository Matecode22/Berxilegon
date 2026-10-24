package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;

import java.util.List;
import java.util.Optional;

public interface UsuarioRolService {

    List<UsuarioRol> findAll();

    Optional<UsuarioRol> findById(UsuarioRolId id);

    UsuarioRol save(UsuarioRol usuarioRol);

    void deleteById(UsuarioRolId id);

    List<Integer> findRolIdsByUsuarioId(Integer usuarioId);

    void assignRoles(Integer usuarioId, List<Integer> rolIds);
}
