package edu.co.icesi.introspringboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer id;

    private String nombre;

    // Con el correo se inicia sesion (hace las veces de username)
    @Column(unique = true)
    private String correo;

    // "contraseña" en el diagrama. Se guarda hasheada con BCrypt
    private String contrasena;

    private String foto;

    private String ciudad;

    private String contacto;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // ACTIVO / INACTIVO
    private String estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UsuarioRol> usuarioRoles;
}
