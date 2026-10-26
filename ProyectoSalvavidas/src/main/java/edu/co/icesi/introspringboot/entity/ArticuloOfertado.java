package edu.co.icesi.introspringboot.entity;

import edu.co.icesi.introspringboot.entity.keys.ArticuloOfertadoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Publicaciones que el oferente entrega en una oferta de intercambio
@Entity
@Table(name = "articulo_ofertado")
@Getter
@Setter
@NoArgsConstructor
public class ArticuloOfertado {

    @EmbeddedId
    private ArticuloOfertadoId id;

    @ManyToOne
    @MapsId("ofertaId")
    @JoinColumn(name = "oferta_id")
    private OfertaIntercambio oferta;

    @ManyToOne
    @MapsId("publicacionOfrecidaId")
    @JoinColumn(name = "publicacion_ofrecida_id")
    private Publicacion publicacionOfrecida;
}
