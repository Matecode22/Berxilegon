package edu.co.icesi.introspringboot.api;


import edu.co.icesi.introspringboot.api.dto.PublicacionDTO;
import edu.co.icesi.introspringboot.service.PublicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publicaciones")
public class PublicacionRestController {

    @Autowired
    private PublicacionService publicacionService;

    @GetMapping
    public ResponseEntity<List<PublicacionDTO>> getPublicaciones() {
        var body = publicacionService.getAllRest();
        return ResponseEntity.status(200).body(body);
    }

}
