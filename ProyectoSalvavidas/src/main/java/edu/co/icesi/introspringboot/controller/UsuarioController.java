package edu.co.icesi.introspringboot.controller;

import edu.co.icesi.introspringboot.entity.keys.UsuarioRolId;
import edu.co.icesi.introspringboot.service.RolService;
import edu.co.icesi.introspringboot.service.UsuarioRolService;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RolService rolService;
    @Autowired
    private UsuarioRolService usuarioRolService;

    // Listar usuarios
    @GetMapping
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/index";
    }

    // Pantalla de roles de un usuario
    @GetMapping("/{id}/roles")
    public String usuarioRoles(Model model, @PathVariable Integer id,
                               @RequestParam(required = false) String status) {
        if ("saved".equals(status))   model.addAttribute("message", "Roles actualizados");
        if ("removed".equals(status)) model.addAttribute("message", "Rol quitado");
        model.addAttribute("usuario", usuarioService.findById(id).orElseThrow());
        model.addAttribute("roles", rolService.findAll());
        model.addAttribute("assignedRolIds", usuarioRolService.findRolIdsByUsuarioId(id));
        return "usuario/roles";
    }

    // Asociar roles: guarda los roles marcados
    @PostMapping("/{id}/roles")
    public String assignRoles(@PathVariable Integer id,
                              @RequestParam(required = false) List<Integer> rolIds) {
        usuarioRolService.assignRoles(id, rolIds == null ? List.of() : rolIds);
        return "redirect:/usuario/" + id + "/roles?status=saved";
    }

    // Quitar un rol a un usuario
    @PostMapping("/{id}/roles/{rolId}/delete")
    public String quitarRol(@PathVariable Integer id, @PathVariable Integer rolId) {
        UsuarioRolId usuarioRolId = new UsuarioRolId();
        usuarioRolId.setUsuarioId(id);
        usuarioRolId.setRolId(rolId);
        usuarioRolService.deleteById(usuarioRolId);
        return "redirect:/usuario/" + id + "/roles?status=removed";
    }
}