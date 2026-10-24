package edu.co.icesi.introspringboot.controller;

import edu.co.icesi.introspringboot.entity.Usuario;
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

    @GetMapping
    public String listUsuarios(
            Model model,
            @RequestParam(required = false) String status
    ) {
        if ("created".equals(status)) {
            model.addAttribute("message", "Usuario creado correctamente");
        }
        model.addAttribute("usuarios", usuarioService.findAll());
        return "usuario/index";
    }

    @GetMapping("/new")
    public String newUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/new";
    }

    @PostMapping
    public String createUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.create(usuario);
        return "redirect:/usuario?status=created";
    }

    @PostMapping("/{id}/delete")
    public String deleteUsuario(@PathVariable Integer id) {
        usuarioService.delete(id);
        return "redirect:/usuario";
    }

    @GetMapping("/{id}/roles")
    public String usuarioRoles(
            Model model,
            @PathVariable Integer id,
            @RequestParam(required = false) String status
    ) {
        if ("saved".equals(status)) {
            model.addAttribute("message", "Roles actualizados correctamente");
        }
        model.addAttribute("assignedRolIds", usuarioRolService.findRolIdsByUsuarioId(id));
        model.addAttribute("usuario", usuarioService.findById(id).orElseThrow());
        model.addAttribute("roles", rolService.findAll());
        return "usuario/roles";
    }

    // asocia los roles marcados y quita los desmarcados
    @PostMapping("/{id}/roles")
    public String assignRoles(
            @PathVariable Integer id,
            @RequestParam(required = false) List<Integer> rolIds
    ) {
        usuarioRolService.assignRoles(id, rolIds == null ? List.of() : rolIds);
        return "redirect:/usuario/" + id + "/roles?status=saved";
    }

    @GetMapping("/login")
    public String login(){
        return "usuario/login";
    }

    // Registro publico
    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {
        usuarioService.registrar(usuario);
        return "redirect:/usuario/login?registered";
    }
}
