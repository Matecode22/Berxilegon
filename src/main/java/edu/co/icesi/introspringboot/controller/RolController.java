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
    public String listRoles(
            Model model,
            @RequestParam(required = false) String status
    ) {
        if ("created".equals(status)) {
            model.addAttribute("message", "Rol creado correctamente");
        }
        if ("deleted".equals(status)) {
            model.addAttribute("message", "Rol eliminado correctamente");
        }
        model.addAttribute("roles", rolService.findAll());
        return "rol/index";
    }

    @GetMapping("/new")
    public String newRol(Model model) {
        model.addAttribute("rol", new Rol());
        return "rol/new";
    }

    @PostMapping
    public String createRol(@ModelAttribute Rol rol) {
        rolService.create(rol);
        return "redirect:/rol?status=created";
    }

    @PostMapping("/{id}/delete")
    public String deleteRol(@PathVariable Integer id) {
        rolService.delete(id);
        return "redirect:/rol?status=deleted";
    }

    @GetMapping("/{id}/permisos")
    public String rolPermisos(
            Model model,
            @PathVariable Integer id,
            @RequestParam(required = false) String status
    ) {
        if ("saved".equals(status)) {
            model.addAttribute("message", "Permisos actualizados correctamente");
        }
        model.addAttribute("assignedPermisoIds", rolService.findPermisoIdsByRolId(id));
        model.addAttribute("rol", rolService.findById(id).orElseThrow());
        model.addAttribute("permisos", permisoService.findAll());
        return "rol/edit";
    }

    @PostMapping("/{id}/permisos")
    public String updatePermisos(
            @PathVariable Integer id,
            @RequestParam(required = false) List<Integer> permisoIds
    ) {
        rolService.updatePermisos(id, permisoIds == null ? List.of() : permisoIds);
        return "redirect:/rol/" + id + "/permisos?status=saved";
    }
}
