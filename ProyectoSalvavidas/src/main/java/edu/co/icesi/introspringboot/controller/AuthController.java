package edu.co.icesi.introspringboot.controller;

import edu.co.icesi.introspringboot.entity.Usuario;
import edu.co.icesi.introspringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    // GET: muestra el formulario. El POST /usuario/login lo procesa Spring Security
    @GetMapping("/login")
    public String login() {
        return "usuario/login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute Usuario usuario) {
        try {
            // el servicio hashea la contrasena y le asigna ROLE_USUARIO
            usuarioService.registrar(usuario);
        } catch (IllegalArgumentException e) {
            return "redirect:/usuario/registro?error";   // p. ej. correo repetido
        }
        return "redirect:/usuario/login?registered";
    }
}
