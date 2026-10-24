package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.RolPermiso;
import edu.co.icesi.introspringboot.entity.keys.RolPermisoId;
import edu.co.icesi.introspringboot.repo.RolPermisoRepository;
import edu.co.icesi.introspringboot.service.RolPermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RolPermisoServiceImpl implements RolPermisoService {

    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @Override
    public List<RolPermiso> findAll() {
        return rolPermisoRepository.findAll();
    }

    @Override
    public Optional<RolPermiso> findById(RolPermisoId id) {
        return rolPermisoRepository.findById(id);
    }

    @Override
    @Transactional
    public RolPermiso save(RolPermiso rolPermiso) {
        return rolPermisoRepository.save(rolPermiso);
    }

    @Override
    @Transactional
    public void deleteById(RolPermisoId id) {
        rolPermisoRepository.deleteById(id);
    }
}
