package edu.co.icesi.introspringboot.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO {
    private Integer id;
    private String titulo;
    private String categoriaNombre;
    private String usuarioNombre;
    private String modoOferta;
    private String estado;
}
