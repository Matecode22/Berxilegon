package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.FotoPublicacion;
import edu.co.icesi.introspringboot.repo.FotoPublicacionRepository;
import edu.co.icesi.introspringboot.service.FotoPublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FotoPublicacionServiceImpl implements FotoPublicacionService {

    @Autowired
    private FotoPublicacionRepository fotoPublicacionRepository;

    @Override
    public List<FotoPublicacion> findAll() {
        return fotoPublicacionRepository.findAll();
    }

    @Override
    public Optional<FotoPublicacion> findById(Integer id) {
        return fotoPublicacionRepository.findById(id);
    }

    @Override
    @Transactional
    public FotoPublicacion save(FotoPublicacion foto) {
        return fotoPublicacionRepository.save(foto);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        fotoPublicacionRepository.deleteById(id);
    }

    @Override
    public List<FotoPublicacion> findByPublicacionId(Integer publicacionId) {
        return fotoPublicacionRepository.findByPublicacion_IdOrderByOrdenAsc(publicacionId);
    }
}
