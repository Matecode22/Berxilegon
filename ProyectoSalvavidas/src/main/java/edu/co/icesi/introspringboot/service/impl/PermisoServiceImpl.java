package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolPermisoRepository;
import edu.co.icesi.introspringboot.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermisoServiceImpl implements PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @Override
    public List<Permiso> findAll() {
        return permisoRepository.findAll();
    }

    @Override
    public Optional<Permiso> findById(Integer id) {
        return permisoRepository.findById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Permiso create(Permiso permiso) {
        if (permiso.getNombre() == null || permiso.getNombre().isBlank()) {
            throw new IllegalArgumentException("Permiso nombre is required");
        }
        if (permisoRepository.existsByNombre(permiso.getNombre())) {
            throw new IllegalArgumentException("Permiso " + permiso.getNombre() + " already exists");
        }
        return permisoRepository.save(permiso);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Permiso update(Permiso permiso) {
        Permiso existente = permisoRepository.findById(permiso.getId())
                .orElseThrow(() -> new RuntimeException("Permiso with id " + permiso.getId() + " not found"));
        existente.setNombre(permiso.getNombre());
        existente.setDescripcion(permiso.getDescripcion());
        return permisoRepository.save(existente);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        rolPermisoRepository.deleteByPermiso_Id(id);
        permisoRepository.deleteById(id);
    }

    @Override
    public List<Permiso> findByCorreo(String correo) {
        return permisoRepository.findDistinctByRolPermisos_Rol_UsuarioRoles_Usuario_Correo(correo);
    }
}
