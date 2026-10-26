package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.RolPermiso;
import edu.co.icesi.introspringboot.entity.keys.RolPermisoId;

import java.util.List;
import java.util.Optional;

public interface RolPermisoService {

    List<RolPermiso> findAll();

    Optional<RolPermiso> findById(RolPermisoId id);

    RolPermiso save(RolPermiso rolPermiso);

    void deleteById(RolPermisoId id);
}
