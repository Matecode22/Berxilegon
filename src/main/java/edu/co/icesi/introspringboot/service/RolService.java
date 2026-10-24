package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> findAll();

    Optional<Rol> findById(Integer id);

    Rol create(Rol rol);

    Rol update(Rol rol);

    void delete(Integer id);

    List<Integer> findPermisoIdsByRolId(Integer rolId);

    void updatePermisos(Integer rolId, List<Integer> permisoIds);
}
