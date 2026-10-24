package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.repo.DetalleVentaRepository;
import edu.co.icesi.introspringboot.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<DetalleVenta> findAll() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public Optional<DetalleVenta> findById(Integer id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    @Transactional
    public DetalleVenta save(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        detalleVentaRepository.deleteById(id);
    }
}
