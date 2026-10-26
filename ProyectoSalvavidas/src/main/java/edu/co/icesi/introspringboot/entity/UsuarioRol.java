package edu.co.icesi.introspringboot.entity;

import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_rol")
@Getter
@Setter
@NoArgsConstructor
public class UsuarioRol {

    @EmbeddedId
    private UsuarioRolId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @MapsId("rolId")
    @JoinColumn(name = "rol_id")
    private Rol rol;
}
