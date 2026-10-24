package edu.co.icesi.introspringboot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

// Detalle de una publicacion en modo SUBASTA. Comparte la llave con la publicacion.
@Entity
@Table(name = "detalle_subasta")
@Getter
@Setter
@NoArgsConstructor
public class DetalleSubasta {

    @Id
    private Integer publicacionId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    @Column(name = "precio_base")
    private Integer precioBase;

    @Column(name = "incremento_minimo")
    private Integer incrementoMinimo;

    // formato del input datetime-local del formulario (2026-10-30T18:00)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
}
