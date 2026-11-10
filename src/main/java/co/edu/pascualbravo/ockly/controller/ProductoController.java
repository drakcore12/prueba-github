package co.edu.pascualbravo.ockly.controller;

import co.edu.pascualbravo.ockly.dto.ProductoForm;
import co.edu.pascualbravo.ockly.service.CategoriaService;
import co.edu.pascualbravo.ockly.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService productos;
    private final CategoriaService categorias;

    public ProductoController(ProductoService productos, CategoriaService categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productos.listar());
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new ProductoForm());
        model.addAttribute("categorias", categorias.listar());
        return "productos/form";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productos.buscar(id));
        return "productos/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", ProductoForm.from(productos.buscar(id)));
        model.addAttribute("categorias", categorias.listar());
        return "productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoForm producto,
                          BindingResult errores, Model model, RedirectAttributes mensajes) {
        if (errores.hasErrors()) {
            model.addAttribute("categorias", categorias.listar());
            return "productos/form";
        }
        productos.guardar(producto);
        mensajes.addFlashAttribute("exito", "Producto guardado correctamente.");
        return "redirect:/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes mensajes) {
        productos.eliminar(id);
        mensajes.addFlashAttribute("exito", "Producto eliminado.");
        return "redirect:/productos";
    }
}
