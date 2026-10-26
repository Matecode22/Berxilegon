package edu.co.icesi.introspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permiso")
@Getter
@Setter
@NoArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permiso_id")
    private Integer id;

    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "permiso", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<RolPermiso> rolPermisos;
}
