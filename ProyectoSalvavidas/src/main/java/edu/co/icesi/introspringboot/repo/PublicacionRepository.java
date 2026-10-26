package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    List<Publicacion> findByEstado(String estado);

    List<Publicacion> findByModoOferta(String modoOferta);

    List<Publicacion> findByCategoria_Nombre(String categoriaNombre);

    List<Publicacion> findByUsuario_Correo(String correo);

    List<Publicacion> findByTituloContainingIgnoreCase(String titulo);
}
