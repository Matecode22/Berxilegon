package edu.co.icesi.introspringboot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "compra")
@Getter
@Setter
@NoArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id")
    private Integer id;

    @Column(name = "fecha_compra")
    private LocalDateTime fechaCompra;

    // PENDIENTE / COMPLETADA / CANCELADA
    private String estado;

    @Column(name = "motivo_cancelacion")
    private String motivoCancelacion;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    // comprador
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
