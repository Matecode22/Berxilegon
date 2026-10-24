package edu.co.icesi.introspringboot.controller;


import edu.co.icesi.introspringboot.entity.DetalleSubasta;
import edu.co.icesi.introspringboot.entity.DetalleVenta;
import edu.co.icesi.introspringboot.entity.Publicacion;
import edu.co.icesi.introspringboot.service.CategoriaService;
import edu.co.icesi.introspringboot.service.FotoPublicacionService;
import edu.co.icesi.introspringboot.service.PublicacionService;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Thymeleaf
@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FotoPublicacionService fotoPublicacionService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute(
                "title",
                "Publicaciones");
        List<Publicacion> publicacionList = publicacionService.getAll();
        model.addAttribute("publicacionList", publicacionList);
        return "publicacion/index";
    }

    // http://localhost:8080/WebApp6/publicaciones/detail/1
    @GetMapping("/detail/{id}")
    public String getPublicacionById(Model model, @PathVariable Integer id) {
        model.addAttribute(
                "publicacion",
                publicacionService.getById(id)
        );
        model.addAttribute(
                "fotos",
                fotoPublicacionService.findByPublicacionId(id)
        );
        return "publicacion/detail";
    }

    //1. GET del formulario
    @GetMapping("/new")
    public String createPublicacion(Model model) {
        Publicacion publicacion = new Publicacion();
        publicacion.setDetalleVenta(new DetalleVenta());
        publicacion.setDetalleSubasta(new DetalleSubasta());
        model.addAttribute(
                "publicacion",
                publicacion);
        model.addAttribute(
                "categorias",
                categoriaService.findAll()
        );
        return "publicacion/new";
    }

    //2. POST: la publicacion queda a nombre del usuario que inicio sesion
    @PostMapping("/save")
    public String savePublicacion(@ModelAttribute Publicacion publicacion, Authentication authentication) {
        publicacion.setUsuario(usuarioService.findByCorreo(authentication.getName()));
        publicacionService.createPublicacion(publicacion);
        return "redirect:/publicaciones";
    }

}
