package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Puja;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.PujaRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.PujaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PujaServiceImpl implements PujaService {

    @Autowired
    private PujaRepository pujaRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Puja> findAll() {
        return pujaRepository.findAll();
    }

    @Override
    public Optional<Puja> findById(Integer id) {
        return pujaRepository.findById(id);
    }

    @Override
    @Transactional
    public Puja save(Puja puja) {
        return pujaRepository.save(puja);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        pujaRepository.deleteById(id);
    }

    @Override
    public List<Puja> findByPublicacionId(Integer publicacionId) {
        return pujaRepository.findByPublicacion_IdOrderByValorDesc(publicacionId);
    }

    @Override
    @Transactional
    public Puja pujar(Integer publicacionId, Integer usuarioId, Integer valor) {
        if (valor == null || valor <= 0) {
            throw new IllegalArgumentException("Valor must be greater than zero");
        }
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion with id " + publicacionId + " not found"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario with id " + usuarioId + " not found"));

        if (!"SUBASTA".equals(publicacion.getModoOferta()) || !"ACTIVA".equals(publicacion.getEstado())) {
            throw new IllegalStateException("Publicacion " + publicacionId + " is not an ACTIVA subasta");
        }
        if (!LocalDateTime.now().isBefore(publicacion.getDetalleSubasta().getFechaCierre())) {
            throw new IllegalStateException("The subasta is closed");
        }
        if (publicacion.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalStateException("The owner cannot bid on his own subasta");
        }

        // la primera puja cubre el precio base; las demas superan la mas alta + incremento minimo
        Optional<Puja> masAlta = pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(publicacionId);
        int minimo = masAlta.isEmpty()
                ? publicacion.getDetalleSubasta().getPrecioBase()
                : masAlta.get().getValor() + publicacion.getDetalleSubasta().getIncrementoMinimo();
        if (valor < minimo) {
            throw new IllegalArgumentException("The bid must be at least " + minimo);
        }

        Puja puja = new Puja();
        puja.setPublicacion(publicacion);
        puja.setUsuario(usuario);
        puja.setValor(valor);
        puja.setFechaHora(LocalDateTime.now());
        return pujaRepository.save(puja);
    }
}
