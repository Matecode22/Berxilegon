package edu.co.icesi.introspringboot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "puja")
@Getter
@Setter
@NoArgsConstructor
public class Puja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "puja_id")
    private Integer id;

    private Integer valor;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    // quien puja
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
