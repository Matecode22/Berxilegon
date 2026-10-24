package edu.co.icesi.introspringboot.repo;

import edu.co.icesi.introspringboot.entity.RolPermiso;
import edu.co.icesi.introspringboot.entity.keys.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    List<RolPermiso> findByRol_Id(Integer rolId);

    @Transactional
    void deleteByRol_Id(Integer rolId);

    @Transactional
    void deleteByPermiso_Id(Integer permisoId);
}
