package edu.co.icesi.introspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "publicacion")
@Getter
@Setter
@NoArgsConstructor
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publicacion_id")
    private Integer id;

    // quien publica
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private String titulo;

    private String descripcion;

    // NUEVO / COMO_NUEVO / BUENO / ACEPTABLE
    @Column(name = "estado_conservacion")
    private String estadoConservacion;

    // VENTA / SUBASTA / INTERCAMBIO
    @Column(name = "modo_oferta")
    private String modoOferta;

    // ACTIVA / RESERVADA / VENDIDA / INTERCAMBIADA / FINALIZADA / DADA_DE_BAJA
    private String estado;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "motivo_baja")
    private String motivoBaja;

    @OneToMany(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FotoPublicacion> fotos;

    @OneToOne(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private DetalleVenta detalleVenta;

    @OneToOne(mappedBy = "publicacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private DetalleSubasta detalleSubasta;

    @OneToMany(mappedBy = "publicacion")
    @JsonIgnore
    private List<Puja> pujas;

    @OneToMany(mappedBy = "publicacion")
    @JsonIgnore
    private List<Comentario> comentarios;
}
