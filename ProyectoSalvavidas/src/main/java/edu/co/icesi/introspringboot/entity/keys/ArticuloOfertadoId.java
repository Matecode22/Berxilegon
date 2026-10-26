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
public class ArticuloOfertadoId implements Serializable {

    @Column(name = "oferta_id")
    private Integer ofertaId;

    @Column(name = "publicacion_ofrecida_id")
    private Integer publicacionOfrecidaId;
}
