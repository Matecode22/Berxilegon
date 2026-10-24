package edu.co.icesi.introspringboot.controller;


import edu.co.icesi.introspringboot.entity.Categoria;
import edu.co.icesi.introspringboot.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/new")
    public String newCategoria(Model model,
                               @RequestParam(required = false) String status) {
        if(status != null) {
            if(status.equals("success")) {
                model.addAttribute("message",
                        "Categoria guardada correctamente");
            }
        }
        model.addAttribute(
                "categoria",
                new Categoria()
        );
        return "categoria/new";
    }

    @PostMapping("/save")
    public String createCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.save(categoria);
        return "redirect:/categoria/new?status=success";
    }

}
