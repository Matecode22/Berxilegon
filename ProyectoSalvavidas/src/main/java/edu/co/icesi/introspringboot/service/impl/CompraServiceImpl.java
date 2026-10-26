package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.Compra;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.repo.CompraRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Compra> findAll() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<Compra> findById(Integer id) {
        return compraRepository.findById(id);
    }

    @Override
    @Transactional
    public Compra save(Compra compra) {
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        compraRepository.deleteById(id);
    }

    @Override
    public List<Compra> findByUsuarioId(Integer usuarioId) {
        return compraRepository.findByUsuario_Id(usuarioId);
    }

    @Override
    @Transactional
    public Compra comprar(Integer publicacionId, Integer usuarioId) {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion with id " + publicacionId + " not found"));
        Usuario comprador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario with id " + usuarioId + " not found"));
        if (!"VENTA".equals(publicacion.getModoOferta()) || !"ACTIVA".equals(publicacion.getEstado())) {
            throw new IllegalStateException("Publicacion " + publicacionId + " is not an ACTIVA venta");
        }
        if (publicacion.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalStateException("A usuario cannot buy his own publicacion");
        }

        Compra compra = new Compra();
        compra.setPublicacion(publicacion);
        compra.setUsuario(comprador);
        compra.setEstado("PENDIENTE");
        compra.setFechaCompra(LocalDateTime.now());
        publicacion.setEstado("RESERVADA");
        publicacionRepository.save(publicacion);
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public Compra confirmar(Integer id) {
        Compra compra = pendiente(id);
        compra.setEstado("COMPLETADA");
        compra.getPublicacion().setEstado("VENDIDA");
        publicacionRepository.save(compra.getPublicacion());
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public Compra cancelar(Integer id, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo de cancelacion is required");
        }
        Compra compra = pendiente(id);
        compra.setEstado("CANCELADA");
        compra.setMotivoCancelacion(motivo);
        // una venta vuelve a estar disponible; una subasta ya cerro y termina sin venta
        Publicacion publicacion = compra.getPublicacion();
        publicacion.setEstado("SUBASTA".equals(publicacion.getModoOferta()) ? "FINALIZADA" : "ACTIVA");
        publicacionRepository.save(publicacion);
        return compraRepository.save(compra);
    }

    private Compra pendiente(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra with id " + id + " not found"));
        if (!"PENDIENTE".equals(compra.getEstado())) {
            throw new IllegalStateException("Compra " + id + " is not PENDIENTE");
        }
        return compra;
    }
}
