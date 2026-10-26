package edu.co.icesi.introspringboot.service;

import edu.co.icesi.introspringboot.entity.Permiso;

import java.util.List;
import java.util.Optional;

public interface PermisoService {

    List<Permiso> findAll();

    Optional<Permiso> findById(Integer id);

    Permiso create(Permiso permiso);

    Permiso update(Permiso permiso);

    void delete(Integer id);

    List<Permiso> findByCorreo(String correo);
}
