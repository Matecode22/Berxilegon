package edu.co.icesi.introspringboot.service.impl;

import edu.co.icesi.introspringboot.api.dto.PublicacionDTO;
import edu.co.icesi.introspringboot.entity.Compra;
import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.entity.Puja;
import edu.co.icesi.introspringboot.repo.CategoriaRepository;
import edu.co.icesi.introspringboot.repo.CompraRepository;
import edu.co.icesi.introspringboot.repo.DetalleSubastaRepository;
import edu.co.icesi.introspringboot.repo.DetalleVentaRepository;
import edu.co.icesi.introspringboot.repo.PublicacionRepository;
import edu.co.icesi.introspringboot.repo.PujaRepository;
import edu.co.icesi.introspringboot.repo.UsuarioRepository;
import edu.co.icesi.introspringboot.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    @Autowired
    private PublicacionRepository publicacionRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;
    @Autowired
    private DetalleSubastaRepository detalleSubastaRepository;
    @Autowired
    private PujaRepository pujaRepository;
    @Autowired
    private CompraRepository compraRepository;

    @Override
    @Transactional
    public Publicacion createPublicacion(Publicacion publicacion) {
        if (publicacion.getTitulo() == null || publicacion.getTitulo().isBlank()) {
            throw new IllegalStateException("Publicacion titulo is null");
        }
        if (publicacion.getUsuario() == null
                || usuarioRepository.findById(publicacion.getUsuario().getId()).isEmpty()) {
            throw new IllegalStateException("Usuario of the publicacion does not exist");
        }
        if (publicacion.getCategoria() == null
                || categoriaRepository.findById(publicacion.getCategoria().getId()).isEmpty()) {
            throw new IllegalStateException("Categoria of the publicacion does not exist");
        }
        String modo = publicacion.getModoOferta();
        if (!"VENTA".equals(modo) && !"SUBASTA".equals(modo) && !"INTERCAMBIO".equals(modo)) {
            throw new IllegalArgumentException("Modo de oferta must be VENTA, SUBASTA or INTERCAMBIO");
        }

        // el detalle se guarda aparte: comparte la llave con la publicacion
        DetalleVenta detalleVenta = publicacion.getDetalleVenta();
        DetalleSubasta detalleSubasta = publicacion.getDetalleSubasta();
        if ("VENTA".equals(modo) && (detalleVenta == null || detalleVenta.getPrecio() == null
                || detalleVenta.getPrecio() <= 0)) {
            throw new IllegalArgumentException("A VENTA needs a precio greater than zero");
        }
        if ("SUBASTA".equals(modo) && (detalleSubasta == null || detalleSubasta.getPrecioBase() == null
                || detalleSubasta.getIncrementoMinimo() == null || detalleSubasta.getFechaCierre() == null
                || !detalleSubasta.getFechaCierre().isAfter(LocalDateTime.now()))) {
            throw new IllegalArgumentException("A SUBASTA needs precio base, incremento minimo and a future fecha de cierre");
        }
        publicacion.setDetalleVenta(null);
        publicacion.setDetalleSubasta(null);
        publicacion.setEstado("ACTIVA");
        publicacion.setFechaPublicacion(LocalDate.now());
        Publicacion saved = publicacionRepository.save(publicacion);

        if ("VENTA".equals(modo)) {
            if (detalleVenta.getElegibleIntercambio() == null) {
                detalleVenta.setElegibleIntercambio(false);
            }
            detalleVenta.setPublicacion(saved);
            saved.setDetalleVenta(detalleVentaRepository.save(detalleVenta));
        } else if ("SUBASTA".equals(modo)) {
            detalleSubasta.setPublicacion(saved);
            saved.setDetalleSubasta(detalleSubastaRepository.save(detalleSubasta));
        }
        return saved;
    }

    @Override
    @Transactional
    public void deletePublicacion(Integer id) {
        publicacionRepository.deleteById(id);
    }

    @Override
    public List<Publicacion> getAll() {
        return publicacionRepository.findAll();
    }

    @Override
    public Publicacion getById(Integer id) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(id);
        if (publicacion.isPresent()) {
            return publicacion.get();
        } else {
            throw new RuntimeException("Publicacion with id " + id + " does not exist");
        }
    }

    @Override
    public List<PublicacionDTO> getAllRest() {
        List<Publicacion> publicaciones = publicacionRepository.findAll();
        return publicaciones.stream().map(
                entity -> new PublicacionDTO(
                        entity.getId(),
                        entity.getTitulo(),
                        entity.getCategoria().getNombre(),
                        entity.getUsuario().getNombre(),
                        entity.getModoOferta(),
                        entity.getEstado()
                )
        ).toList();
    }

    @Override
    @Transactional
    public Publicacion darDeBaja(Integer id, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Motivo de baja is required");
        }
        Publicacion publicacion = getById(id);
        if (!"ACTIVA".equals(publicacion.getEstado())) {
            throw new IllegalStateException("Only an ACTIVA publicacion can be dada de baja");
        }
        publicacion.setEstado("DADA_DE_BAJA");
        publicacion.setMotivoBaja(motivo);
        return publicacionRepository.save(publicacion);
    }

    @Override
    @Transactional
    public Publicacion cerrarSubasta(Integer id) {
        Publicacion publicacion = getById(id);
        if (!"SUBASTA".equals(publicacion.getModoOferta()) || !"ACTIVA".equals(publicacion.getEstado())) {
            throw new IllegalStateException("Publicacion " + id + " is not an ACTIVA subasta");
        }
        if (LocalDateTime.now().isBefore(publicacion.getDetalleSubasta().getFechaCierre())) {
            throw new IllegalStateException("The subasta closes at " + publicacion.getDetalleSubasta().getFechaCierre());
        }
        Optional<Puja> ganadora = pujaRepository.findFirstByPublicacion_IdOrderByValorDesc(id);
        if (ganadora.isEmpty()) {
            // sin pujas termina sin venta
            publicacion.setEstado("FINALIZADA");
        } else {
            Compra compra = new Compra();
            compra.setPublicacion(publicacion);
            compra.setUsuario(ganadora.get().getUsuario());
            compra.setEstado("PENDIENTE");
            compra.setFechaCompra(LocalDateTime.now());
            compraRepository.save(compra);
            publicacion.setEstado("RESERVADA");
        }
        return publicacionRepository.save(publicacion);
    }
}
