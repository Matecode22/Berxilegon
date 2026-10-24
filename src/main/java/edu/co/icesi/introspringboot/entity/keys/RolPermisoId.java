package edu.co.icesi.introspringboot.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class RolPermisoId implements Serializable {

    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "permiso_id")
    private Integer permisoId;
}
