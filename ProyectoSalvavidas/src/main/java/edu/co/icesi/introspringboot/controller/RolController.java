package edu.co.icesi.introspringboot.controller;

import edu.co.icesi.introspringboot.entity.Rol;
import edu.co.icesi.introspringboot.service.PermisoService;
import edu.co.icesi.introspringboot.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rol")
public class RolController {

    @Autowired
    private RolService rolService;
    @Autowired
    private PermisoService permisoService;

    @GetMapping
    public String listRoles(Model model, @RequestParam(required = false) String status) {
        if ("created".equals(status)) model.addAttribute("message", "Rol creado");
        if ("deleted".equals(status)) model.addAttribute("message", "Rol eliminado");
        if ("error".equals(status))   model.addAttribute("error", "Ese rol ya existe o el nombre está vacío");
        model.addAttribute("roles", rolService.findAll());
        return "rol/index";
    }

    // Crear rol: formulario
    @GetMapping("/new")
    public String newRol(Model model) {
        model.addAttribute("rol", new Rol());
        return "rol/new";
    }

    // Crear rol: guardar (el servicio le agrega el prefijo ROLE_)
    @PostMapping
    public String createRol(@ModelAttribute Rol rol) {
        try {
            rolService.create(rol);
        } catch (IllegalArgumentException e) {
            return "redirect:/rol?status=error";
        }
        return "redirect:/rol?status=created";
    }

    // Eliminar rol
    @PostMapping("/{id}/delete")
    public String deleteRol(@PathVariable Integer id) {
        rolService.delete(id);
        return "redirect:/rol?status=deleted";
    }

    // Asociar permisos: pantalla
    @GetMapping("/{id}/permisos")
    public String rolPermisos(Model model, @PathVariable Integer id,
                              @RequestParam(required = false) String status) {
        if ("saved".equals(status)) model.addAttribute("message", "Permisos actualizados");
        model.addAttribute("rol", rolService.findById(id).orElseThrow());
        model.addAttribute("permisos", permisoService.findAll());
        model.addAttribute("assignedPermisoIds", rolService.findPermisoIdsByRolId(id));
        return "rol/permisos";
    }

    // Asociar permisos: guardar los marcados
    @PostMapping("/{id}/permisos")
    public String updatePermisos(@PathVariable Integer id,
                                 @RequestParam(required = false) List<Integer> permisoIds) {
        rolService.updatePermisos(id, permisoIds == null ? List.of() : permisoIds);
        return "redirect:/rol/" + id + "/permisos?status=saved";
    }
}