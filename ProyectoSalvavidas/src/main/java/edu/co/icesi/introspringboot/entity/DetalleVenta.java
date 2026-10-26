package edu.co.icesi.introspringboot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Detalle de una publicacion en modo VENTA. Comparte la llave con la publicacion.
@Entity
@Table(name = "detalle_venta")
@Getter
@Setter
@NoArgsConstructor
public class DetalleVenta {

    @Id
    private Integer publicacionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    private Integer precio;

    @Column(name = "elegible_intercambio")
    private Boolean elegibleIntercambio;

    @Column(name = "descripcion_intercambio")
    private String descripcionIntercambio;
}
