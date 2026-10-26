package edu.co.icesi.introspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "oferta_intercambio")
@Getter
@Setter
@NoArgsConstructor
public class OfertaIntercambio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oferta_id")
    private Integer id;

    @Column(name = "diferencia_dinero")
    private Integer diferenciaDinero;

    // OFERENTE / PUBLICADOR: quien recibe la diferencia de dinero
    @Column(name = "a_favor_de")
    private String aFavorDe;

    // PENDIENTE / ACEPTADA / RECHAZADA
    private String estado;

    @Column(name = "motivo_rechazo")
    private String motivoRechazo;

    @Column(name = "fecha_oferta")
    private LocalDateTime fechaOferta;

    // publicacion por la que se oferta
    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    // oferente
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "oferta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ArticuloOfertado> articulos;
}
