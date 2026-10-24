package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;
import edu.co.icesi.introspringboot.repo.RolRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRolRepository;
import edu.co.icesi.introspringboot.service.UsuarioRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioRolServiceImpl implements UsuarioRolService {

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<UsuarioRol> findAll() {
        return usuarioRolRepository.findAll();
    }

    @Override
    public Optional<UsuarioRol> findById(UsuarioRolId id) {
        return usuarioRolRepository.findById(id);
    }

    @Override
    @Transactional
    public UsuarioRol save(UsuarioRol usuarioRol) {
        return usuarioRolRepository.save(usuarioRol);
    }

    @Override
    @Transactional
    public void deleteById(UsuarioRolId id) {
        usuarioRolRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Integer> findRolIdsByUsuarioId(Integer usuarioId) {
        return usuarioRolRepository.findByUsuario_Id(usuarioId).stream()
                .map(usuarioRol -> usuarioRol.getRol().getId())
                .toList();
    }

    // Asocia los roles marcados y quita los que se desmarcaron
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignRoles(Integer usuarioId, List<Integer> rolIds) {
        List<UsuarioRol> actuales = usuarioRolRepository.findByUsuario_Id(usuarioId);

        //Quitar los roles que ya no estan seleccionados
        actuales.stream()
                .filter(usuarioRol -> !rolIds.contains(usuarioRol.getRol().getId()))
                .forEach(usuarioRol -> usuarioRolRepository.delete(usuarioRol));

        //Agregar los roles nuevos
        List<Integer> rolIdsActuales = actuales.stream()
                .map(usuarioRol -> usuarioRol.getRol().getId())
                .toList();
        rolIds.stream()
                .filter(rolId -> !rolIdsActuales.contains(rolId))
                .forEach(rolId -> {
                    UsuarioRolId id = new UsuarioRolId();
                    id.setUsuarioId(usuarioId);
                    id.setRolId(rolId);

                    UsuarioRol usuarioRol = new UsuarioRol();
                    usuarioRol.setId(id);
                    usuarioRol.setUsuario(usuarioRepository.getReferenceById(usuarioId));
                    usuarioRol.setRol(rolRepository.getReferenceById(rolId));
                    usuarioRolRepository.save(usuarioRol);
                });
    }
}
