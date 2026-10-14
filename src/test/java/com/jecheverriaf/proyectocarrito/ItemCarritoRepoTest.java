package com.jecheverriaf.proyectocarrito;


import com.jecheverriaf.proyectocarrito.entity.ItemCarrito;
import com.jecheverriaf.proyectocarrito.entity.Producto;
import com.jecheverriaf.proyectocarrito.repository.ItemCarritoRepository;
import com.jecheverriaf.proyectocarrito.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;



@DataJpaTest //esto sirve para probar los datos
@TestPropertySource(properties = {
        "spring.sql.init.mode=never",//esto sirva para no ejecutarr los scripts sql mientras se hacen las  ppruebas
        "spring.jpa.hibernate.ddl-auto=create-drop"// es una instruccion para crear  lass tabas a partir de la capa Entity
})
public class ItemCarritoRepoTest {

    @Autowired//esta anotacion sirve para traer objets a construidos
    private ItemCarritoRepository itemCarritoRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Test
    //es un escenario dondel el usuario carga productos al carrito
    public void itemsConProductoCargados(){
        Producto producto = productoRepository.save(new Producto(null, "FinePix Pro2 3D Camera","3DcAm01", "images/product/camera.jpg", 671.21));
        itemCarritoRepository.save(new ItemCarrito(null, producto, "usuario",2));

        List<ItemCarrito> resultado = itemCarritoRepository.findAll();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodUsuario()).isEqualTo("usuario");
        assertThat(resultado.get(0).getProducto().getNombre()).isEqualTo("FinePix Pro2 3D Camera");

    }
}
