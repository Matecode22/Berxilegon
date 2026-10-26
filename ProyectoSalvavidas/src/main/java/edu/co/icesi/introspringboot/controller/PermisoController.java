package edu.co.icesi.introspringboot.controller;

import edu.co.icesi.introspringboot.entity.Permiso;
import edu.co.icesi.introspringboot.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/permiso")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public String listPermisos(Model model, @RequestParam(required = false) String status) {
        if ("created".equals(status)) model.addAttribute("message", "Permiso creado");
        model.addAttribute("permisos", permisoService.findAll());
        return "permiso/index";
    }

    @GetMapping("/new")
    public String newPermiso(Model model) {
        model.addAttribute("permiso", new Permiso());
        return "permiso/new";
    }

    @PostMapping
    public String createPermiso(@ModelAttribute Permiso permiso) {
        permisoService.create(permiso);
        return "redirect:/permiso?status=created";
    }
}