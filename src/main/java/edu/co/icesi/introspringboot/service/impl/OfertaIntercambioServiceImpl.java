package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.entity.ArticuloOfertado;
import edu.co.icesi.introspringboot.entity.OfertaIntercambio;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import edu.co.icesi.introspringboot.repo.ArticuloOfertadoRepository;
import edu.co.icesi.introspringboot.repo.OfertaIntercambioRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.OfertaIntercambioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfertaIntercambioServiceImpl implements OfertaIntercambioService {

    @Autowired
    private OfertaIntercambioRepository ofertaIntercambioRepository;
    @Autowired
    private ArticuloOfertadoRepository articuloOfertadoRepository;
    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<OfertaIntercambio> findAll() {
        return ofertaIntercambioRepository.findAll();
    }

    @Override
    public Optional<OfertaIntercambio> findById(Integer id) {
        return ofertaIntercambioRepository.findById(id);
    }

    @Override
    @Transactional
    public OfertaIntercambio save(OfertaIntercambio oferta) {
        return ofertaIntercambioRepository.save(oferta);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        ofertaIntercambioRepository.deleteById(id);
    }

    @Override
    public List<OfertaIntercambio> findByPublicacionId(Integer publicacionId) {
        return ofertaIntercambioRepository.findByPublicacion_Id(publicacionId);
    }

    @Override
    @Transactional
    public OfertaIntercambio crearOferta(Integer publicacionId, Integer usuarioId,
                                         List<Integer> publicacionesOfrecidasIds,
                                         Integer diferenciaDinero, String aFavorDe) {
        if (publicacionesOfrecidasIds == null || publicacionesOfrecidasIds.isEmpty()) {
            throw new IllegalArgumentException("An oferta needs at least one articulo ofrecido");
        }
        Publicacion objetivo = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion with id " + publicacionId + " not found"));
        Usuario oferente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario with id " + usuarioId + " not found"));

        // reciben ofertas las publicaciones INTERCAMBIO y las VENTA marcadas como elegibles
        boolean aceptaIntercambio = "INTERCAMBIO".equals(objetivo.getModoOferta())
                || ("VENTA".equals(objetivo.getModoOferta()) && objetivo.getDetalleVenta() != null
                && Boolean.TRUE.equals(objetivo.getDetalleVenta().getElegibleIntercambio()));
        if (!"ACTIVA".equals(objetivo.getEstado()) || !aceptaIntercambio) {
            throw new IllegalStateException("Publicacion " + publicacionId + " does not accept ofertas de intercambio");
        }
        if (objetivo.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalStateException("A usuario cannot make an oferta for his own publicacion");
        }

        OfertaIntercambio oferta = new OfertaIntercambio();
        oferta.setPublicacion(objetivo);
        oferta.setUsuario(oferente);
        oferta.setDiferenciaDinero(diferenciaDinero == null ? 0 : diferenciaDinero);
        oferta.setAFavorDe(oferta.getDiferenciaDinero() > 0 ? aFavorDe : null);
        oferta.setEstado("PENDIENTE");
        oferta.setFechaOferta(LocalDateTime.now());
        OfertaIntercambio saved = ofertaIntercambioRepository.save(oferta);

        for (Integer ofrecidaId : publicacionesOfrecidasIds) {
            Publicacion ofrecida = publicacionRepository.findById(ofrecidaId)
                    .orElseThrow(() -> new RuntimeException("Publicacion with id " + ofrecidaId + " not found"));
            if (!ofrecida.getUsuario().getId().equals(usuarioId) || !"ACTIVA".equals(ofrecida.getEstado())) {
                throw new IllegalStateException("Publicacion " + ofrecidaId + " cannot be offered by this usuario");
            }
            ArticuloOfertadoId id = new ArticuloOfertadoId();
            id.setOfertaId(saved.getId());
            id.setPublicacionOfrecidaId(ofrecidaId);
            ArticuloOfertado articulo = new ArticuloOfertado();
            articulo.setId(id);
            articulo.setOferta(saved);
            articulo.setPublicacionOfrecida(ofrecida);
            articuloOfertadoRepository.save(articulo);
        }
        return saved;
    }

    @Override
    @Transactional
    public OfertaIntercambio aceptar(Integer id) {
        OfertaIntercambio oferta = pendiente(id);
        Publicacion objetivo = oferta.getPublicacion();
        if (!"ACTIVA".equals(objetivo.getEstado())) {
            throw new IllegalStateException("Publicacion " + objetivo.getId() + " is no longer available");
        }
        oferta.setEstado("ACEPTADA");
        objetivo.setEstado("INTERCAMBIADA");
        publicacionRepository.save(objetivo);
        for (ArticuloOfertado articulo : articuloOfertadoRepository.findByOferta_Id(id)) {
            articulo.getPublicacionOfrecida().setEstado("INTERCAMBIADA");
            publicacionRepository.save(articulo.getPublicacionOfrecida());
        }
        // las demas ofertas pendientes por la misma publicacion se rechazan
        for (OfertaIntercambio otra : ofertaIntercambioRepository.findByPublicacion_IdAndEstado(objetivo.getId(), "PENDIENTE")) {
            if (!otra.getId().equals(id)) {
                otra.setEstado("RECHAZADA");
                otra.setMotivoRechazo("La publicacion ya fue intercambiada");
                ofertaIntercambioRepository.save(otra);
            }
        }
        return ofertaIntercambioRepository.save(oferta);
    }

    @Override
    @Transactional
    public OfertaIntercambio rechazar(Integer id, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo de rechazo is required");
        }
        OfertaIntercambio oferta = pendiente(id);
        oferta.setEstado("RECHAZADA");
        oferta.setMotivoRechazo(motivo);
        return ofertaIntercambioRepository.save(oferta);
    }

    private OfertaIntercambio pendiente(Integer id) {
        OfertaIntercambio oferta = ofertaIntercambioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta with id " + id + " not found"));
        if (!"PENDIENTE".equals(oferta.getEstado())) {
            throw new IllegalStateException("Oferta " + id + " is not PENDIENTE");
        }
        return oferta;
    }
}
