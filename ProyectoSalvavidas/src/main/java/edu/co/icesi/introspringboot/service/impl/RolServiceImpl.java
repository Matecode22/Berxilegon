package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.entity.RolPermiso;
import edu.co.icesi.introspringboot.entity.keys.RolPermisoId;
import edu.co.icesi.introspringboot.repo.PermisoRepository;
import edu.co.icesi.introspringboot.repo.RolPermisoRepository;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private RolPermisoRepository rolPermisoRepository;
    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> findById(Integer id) {
        return rolRepository.findById(id);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Rol create(Rol rol) {
        if (rol.getNombre() == null || rol.getNombre().isBlank()) {
            throw new IllegalArgumentException("Rol nombre is required");
        }
        // los roles se guardan con el prefijo ROLE_ para poder usar hasRole(...)
        String nombre = rol.getNombre().trim().toUpperCase();
        if (!nombre.startsWith("ROLE_")) {
            nombre = "ROLE_" + nombre;
        }
        if (rolRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Rol " + nombre + " already exists");
        }
        rol.setNombre(nombre);
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public Rol update(Rol rol) {
        Rol existente = rolRepository.findById(rol.getId())
                .orElseThrow(() -> new RuntimeException("Rol with id " + rol.getId() + " not found"));
        existente.setNombre(rol.getNombre());
        return rolRepository.save(existente);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        usuarioRolRepository.deleteByRol_Id(id);
        rolPermisoRepository.deleteByRol_Id(id);
        rolRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Integer> findPermisoIdsByRolId(Integer rolId) {
        return rolPermisoRepository.findByRol_Id(rolId).stream()
                .map(rolPermiso -> rolPermiso.getPermiso().getId())
                .toList();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void updatePermisos(Integer rolId, List<Integer> permisoIds) {
        List<RolPermiso> actuales = rolPermisoRepository.findByRol_Id(rolId);

        //Quitar los permisos que ya no estan seleccionados
        actuales.stream()
                .filter(rolPermiso -> !permisoIds.contains(rolPermiso.getPermiso().getId()))
                .forEach(rolPermiso -> rolPermisoRepository.delete(rolPermiso));

        //Agregar los permisos nuevos
        List<Integer> permisoIdsActuales = actuales.stream()
                .map(rolPermiso -> rolPermiso.getPermiso().getId())
                .toList();
        permisoIds.stream()
                .filter(permisoId -> !permisoIdsActuales.contains(permisoId))
                .forEach(permisoId -> {
                    RolPermisoId id = new RolPermisoId();
                    id.setRolId(rolId);
                    id.setPermisoId(permisoId);

                    RolPermiso rolPermiso = new RolPermiso();
                    rolPermiso.setId(id);
                    rolPermiso.setRol(rolRepository.getReferenceById(rolId));
                    rolPermiso.setPermiso(permisoRepository.getReferenceById(permisoId));
                    rolPermisoRepository.save(rolPermiso);
                });
    }
}
