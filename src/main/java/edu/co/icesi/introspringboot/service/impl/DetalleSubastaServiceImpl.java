package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.repo.DetalleSubastaRepository;
import edu.co.icesi.introspringboot.service.DetalleSubastaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleSubastaServiceImpl implements DetalleSubastaService {

    @Autowired
    private DetalleSubastaRepository detalleSubastaRepository;

    @Override
    public List<DetalleSubasta> findAll() {
        return detalleSubastaRepository.findAll();
    }

    @Override
    public Optional<DetalleSubasta> findById(Integer id) {
        return detalleSubastaRepository.findById(id);
    }

    @Override
    @Transactional
    public DetalleSubasta save(DetalleSubasta detalleSubasta) {
        return detalleSubastaRepository.save(detalleSubasta);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        detalleSubastaRepository.deleteById(id);
    }
}
