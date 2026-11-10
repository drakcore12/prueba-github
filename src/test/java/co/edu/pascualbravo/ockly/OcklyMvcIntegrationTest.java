package co.edu.pascualbravo.ockly;

import co.edu.pascualbravo.ockly.model.Categoria;
import co.edu.pascualbravo.ockly.model.Producto;
import co.edu.pascualbravo.ockly.repository.CategoriaRepository;
import co.edu.pascualbravo.ockly.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OcklyMvcIntegrationTest {
    @Autowired private MockMvc mvc;
    @Autowired private CategoriaRepository categorias;
    @Autowired private ProductoRepository productos;

    @BeforeEach
    void limpiar() {
        productos.deleteAll();
        categorias.deleteAll();
    }

    @Test
    void crearConsultarActualizarYEliminarAmbasEntidades() throws Exception {
        mvc.perform(post("/categorias/guardar")
                .param("nombre", "Cuadernos")
                .param("descripcion", "Útiles escolares"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/categorias"));
        assertThat(categorias.count()).isEqualTo(1);
        Categoria categoria = categorias.findAll().get(0);

        mvc.perform(get("/categorias/{id}", categoria.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("categorias/detalle"));

        mvc.perform(post("/productos/guardar")
                .param("nombre", "Cuaderno cuadriculado")
                .param("descripcion", "100 hojas")
                .param("precio", "12000.00")
                .param("stock", "15")
                .param("categoriaId", categoria.getId().toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/productos"));

        Producto producto = productos.findAll().get(0);
        assertThat(producto.getCategoria().getId()).isEqualTo(categoria.getId());
        mvc.perform(get("/productos/{id}", producto.getId()))
            .andExpect(status().isOk())
            .andExpect(view().name("productos/detalle"));

        mvc.perform(post("/productos/guardar")
                .param("id", producto.getId().toString())
                .param("nombre", "Cuaderno actualizado")
                .param("descripcion", "100 hojas")
                .param("precio", "14000.00")
                .param("stock", "8")
                .param("categoriaId", categoria.getId().toString()))
            .andExpect(status().is3xxRedirection());
        assertThat(productos.findById(producto.getId()).orElseThrow().getStock()).isEqualTo(8);

        mvc.perform(post("/categorias/guardar")
                .param("id", categoria.getId().toString())
                .param("nombre", "Papelería")
                .param("descripcion", "Nueva descripción"))
            .andExpect(status().is3xxRedirection());
        assertThat(categorias.findById(categoria.getId()).orElseThrow().getNombre()).isEqualTo("Papelería");

        mvc.perform(post("/productos/{id}/eliminar", producto.getId()))
            .andExpect(status().is3xxRedirection());
        assertThat(productos.count()).isZero();

        mvc.perform(post("/categorias/{id}/eliminar", categoria.getId()))
            .andExpect(status().is3xxRedirection());
        assertThat(categorias.count()).isZero();
    }

    @Test
    void impideEliminarCategoriaConProductos() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNombre("Bolígrafos");
        categoria = categorias.save(categoria);
        Producto producto = new Producto();
        producto.setNombre("Bolígrafo azul");
        producto.setPrecio(new BigDecimal("2500"));
        producto.setStock(10);
        producto.setCategoria(categoria);
        productos.save(producto);

        mvc.perform(post("/categorias/{id}/eliminar", categoria.getId()))
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attributeExists("error"));
        assertThat(categorias.count()).isEqualTo(1);
    }

    @Test
    void validaInformacionIncompletaYPrecioInvalido() throws Exception {
        mvc.perform(post("/categorias/guardar").param("nombre", "  "))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("categoria", "nombre"));

        mvc.perform(post("/productos/guardar")
                .param("nombre", "Producto")
                .param("precio", "-1")
                .param("stock", "-5"))
            .andExpect(status().isOk())
            .andExpect(model().attributeHasFieldErrors("producto", "precio", "stock", "categoriaId"));
        assertThat(productos.count()).isZero();
    }
}
