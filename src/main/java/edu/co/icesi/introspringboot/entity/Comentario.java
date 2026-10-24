package edu.co.icesi.introspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comentario")
@Getter
@Setter
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comentario_id")
    private Integer id;

    private String texto;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "publicacion_id")
    private Publicacion publicacion;

    // autor
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    // comentario al que responde (null si es una pregunta)
    @ManyToOne
    @JoinColumn(name = "comentario_padre_id")
    private Comentario padre;

    @OneToMany(mappedBy = "padre")
    @JsonIgnore
    private List<Comentario> respuestas;
}
