package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import edu.co.icesi.introspringboot.repo.ArticuloOfertadoRepository;
import edu.co.icesi.introspringboot.service.ArticuloOfertadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloOfertadoServiceImpl implements ArticuloOfertadoService {

    @Autowired
    private ArticuloOfertadoRepository articuloOfertadoRepository;

    @Override
    public List<ArticuloOfertado> findAll() {
        return articuloOfertadoRepository.findAll();
    }

    @Override
    public Optional<ArticuloOfertado> findById(ArticuloOfertadoId id) {
        return articuloOfertadoRepository.findById(id);
    }

    @Override
    @Transactional
    public ArticuloOfertado save(ArticuloOfertado articuloOfertado) {
        return articuloOfertadoRepository.save(articuloOfertado);
    }

    @Override
    @Transactional
    public void deleteById(ArticuloOfertadoId id) {
        articuloOfertadoRepository.deleteById(id);
    }
}
