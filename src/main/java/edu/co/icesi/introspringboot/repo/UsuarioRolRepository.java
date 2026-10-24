package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.UsuarioRol;
import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {

    List<UsuarioRol> findByUsuario_Id(Integer usuarioId);

    @Transactional
    void deleteByUsuario_Id(Integer usuarioId);

    @Transactional
    void deleteByRol_Id(Integer rolId);
}
