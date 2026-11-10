package co.edu.pascualbravo.ockly.controller;

import co.edu.pascualbravo.ockly.repository.CategoriaRepository;
import co.edu.pascualbravo.ockly.repository.ProductoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;

    public InicioController(CategoriaRepository categorias, ProductoRepository productos) {
        this.categorias = categorias;
        this.productos = productos;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("categoriasTotal", categorias.count());
        model.addAttribute("productosTotal", productos.count());
        return "inicio";
    }
}
