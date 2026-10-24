package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Categoria;
import edu.co.icesi.introspringboot.repo.CategoriaRepository;
import edu.co.icesi.introspringboot.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> findById(Integer id) {
        return categoriaRepository.findById(id);
    }

    @Override
    @Transactional
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        categoriaRepository.deleteById(id);
    }
}
