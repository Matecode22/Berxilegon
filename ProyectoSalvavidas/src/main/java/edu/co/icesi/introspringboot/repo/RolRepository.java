package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    List<Rol> findByNombreContainingIgnoreCase(String nombre);

    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    // Rol -> usuarioRoles -> usuario -> correo
    List<Rol> findByUsuarioRoles_Usuario_Correo(String correo);
}
