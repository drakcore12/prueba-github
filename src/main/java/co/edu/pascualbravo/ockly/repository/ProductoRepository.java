package co.edu.pascualbravo.ockly.repository;

import co.edu.pascualbravo.ockly.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // La vista MVC necesita la categoria aun despues de cerrar la transaccion.
    @EntityGraph(attributePaths = "categoria")
    List<Producto> findAllByOrderByNombreAsc();

    @EntityGraph(attributePaths = "categoria")
    List<Producto> findByCategoriaIdOrderByNombreAsc(Long categoriaId);

    @Override
    @EntityGraph(attributePaths = "categoria")
    Optional<Producto> findById(Long id);

    long countByCategoriaId(Long categoriaId);
}
