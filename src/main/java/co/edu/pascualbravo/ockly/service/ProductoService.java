package co.edu.pascualbravo.ockly.service;

import co.edu.pascualbravo.ockly.dto.ProductoForm;
import co.edu.pascualbravo.ockly.model.Categoria;
import co.edu.pascualbravo.ockly.model.Producto;
import co.edu.pascualbravo.ockly.repository.CategoriaRepository;
import co.edu.pascualbravo.ockly.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productos;
    private final CategoriaRepository categorias;

    public ProductoService(ProductoRepository productos, CategoriaRepository categorias) {
        this.productos = productos;
        this.categorias = categorias;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() { return productos.findAllByOrderByNombreAsc(); }

    @Transactional(readOnly = true)
    public List<Producto> porCategoria(Long id) {
        return productos.findByCategoriaIdOrderByNombreAsc(id);
    }

    @Transactional(readOnly = true)
    public Producto buscar(Long id) {
        return productos.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @Transactional
    public Producto guardar(ProductoForm form) {
        Categoria categoria = categorias.findById(form.getCategoriaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoría inválida"));
        Producto producto = form.getId() == null ? new Producto() : buscar(form.getId());
        producto.setNombre(form.getNombre().trim());
        producto.setDescripcion(form.getDescripcion());
        producto.setPrecio(form.getPrecio());
        producto.setStock(form.getStock());
        producto.setCategoria(categoria);
        return productos.save(producto);
    }

    @Transactional
    public void eliminar(Long id) { productos.delete(buscar(id)); }
}
