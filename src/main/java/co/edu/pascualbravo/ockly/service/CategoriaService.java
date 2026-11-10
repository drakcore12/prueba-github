package co.edu.pascualbravo.ockly.service;

import co.edu.pascualbravo.ockly.model.Categoria;
import co.edu.pascualbravo.ockly.repository.CategoriaRepository;
import co.edu.pascualbravo.ockly.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categorias;
    private final ProductoRepository productos;

    public CategoriaService(CategoriaRepository categorias, ProductoRepository productos) {
        this.categorias = categorias;
        this.productos = productos;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listar() { return categorias.findAllByOrderByNombreAsc(); }

    @Transactional(readOnly = true)
    public Categoria buscar(Long id) {
        return categorias.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    }

    @Transactional
    public Categoria guardar(Categoria entrada) {
        Categoria categoria = entrada.getId() == null ? new Categoria() : buscar(entrada.getId());
        categoria.setNombre(entrada.getNombre().trim());
        categoria.setDescripcion(entrada.getDescripcion());
        return categorias.save(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscar(id);
        if (productos.countByCategoriaId(id) > 0) {
            throw new IllegalStateException("No puedes eliminar una categoría con productos asociados.");
        }
        categorias.delete(categoria);
    }
}
