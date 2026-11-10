package co.edu.pascualbravo.ockly.controller;

import co.edu.pascualbravo.ockly.model.Categoria;
import co.edu.pascualbravo.ockly.service.CategoriaService;
import co.edu.pascualbravo.ockly.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categorias;
    private final ProductoService productos;

    public CategoriaController(CategoriaService categorias, ProductoService productos) {
        this.categorias = categorias;
        this.productos = productos;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categorias.listar());
        return "categorias/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "categorias/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categorias.buscar(id));
        model.addAttribute("productos", productos.porCategoria(id));
        return "categorias/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categorias.buscar(id));
        return "categorias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult errores, RedirectAttributes mensajes) {
        if (errores.hasErrors()) return "categorias/form";
        categorias.guardar(categoria);
        mensajes.addFlashAttribute("exito", "Categoría guardada correctamente.");
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes mensajes) {
        try {
            categorias.eliminar(id);
            mensajes.addFlashAttribute("exito", "Categoría eliminada.");
        } catch (IllegalStateException ex) {
            mensajes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/categorias";
    }
}
