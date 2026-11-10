package co.edu.pascualbravo.ockly.repository;

import co.edu.pascualbravo.ockly.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByOrderByNombreAsc();
    List<Producto> findByCategoriaIdOrderByNombreAsc(Long categoriaId);
    long countByCategoriaId(Long categoriaId);
}
