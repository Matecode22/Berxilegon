package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    boolean existsByNombre(String nombre);

    // Permiso -> rolPermisos -> rol -> usuarioRoles -> usuario -> correo
    List<Permiso> findDistinctByRolPermisos_Rol_UsuarioRoles_Usuario_Correo(String correo);
}
