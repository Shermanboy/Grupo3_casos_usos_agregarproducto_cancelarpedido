package com.aydsii.demo.services;

import com.aydsii.demo.dtos.ResultadoAgregarProducto;
import com.aydsii.demo.entities.Carrito;
import com.aydsii.demo.entities.Cliente;
import com.aydsii.demo.entities.Producto;
import com.aydsii.demo.repositories.CarritoRepository;
import com.aydsii.demo.repositories.ClienteRepository;
import com.aydsii.demo.repositories.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoService carritoService;

    private Cliente cliente() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Ana");
        cliente.setApellido("Pérez");
        cliente.setEmail("ana@test.com");
        return cliente;
    }

    private Producto producto(int stock) {
        Producto producto = new Producto();
        producto.setNombre("Maceta");
        producto.setPrecioUnitario(new BigDecimal("1500.00"));
        producto.setStockDisponible(stock);
        return producto;
    }

    @Test
    void agregaProductoACarritoExistente() {
        Cliente cliente = cliente();
        Producto producto = producto(10);
        Carrito carrito = new Carrito(cliente);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        when(carritoRepository.findByCliente_IdCliente(1L)).thenReturn(Optional.of(carrito));
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 2);

        assertTrue(resultado.exito());
        assertEquals(200, resultado.codigo());
        assertNotNull(resultado.carrito());
        assertEquals(1, resultado.carrito().detalles().size());
        assertEquals(new BigDecimal("3000.00"), resultado.carrito().subtotal());
        verify(carritoRepository).save(carrito);
    }

    @Test
    void creaCarritoSiElClienteNoTieneUno() {
        Cliente cliente = cliente();
        Producto producto = producto(10);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        when(carritoRepository.findByCliente_IdCliente(1L)).thenReturn(Optional.empty());
        when(carritoRepository.save(any(Carrito.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 1);

        assertTrue(resultado.exito());
        assertNotNull(cliente.getCarrito());
        assertEquals(1, resultado.carrito().detalles().size());
    }

    @Test
    void devuelveConflictoSiNoHayStock() {
        Cliente cliente = cliente();
        Producto producto = producto(1);
        Carrito carrito = new Carrito(cliente);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto));
        when(carritoRepository.findByCliente_IdCliente(1L)).thenReturn(Optional.of(carrito));

        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 2);

        assertFalse(resultado.exito());
        assertEquals(409, resultado.codigo());
        assertTrue(resultado.mensaje().contains("hasta 1"));
        assertNull(resultado.carrito());
        verify(carritoRepository, never()).save(any(Carrito.class));
    }

    @Test
    void devuelveNoEncontradoSiElClienteNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 1);

        assertFalse(resultado.exito());
        assertEquals(404, resultado.codigo());
        verifyNoInteractions(productoRepository);
    }

    @Test
    void devuelveNoEncontradoSiElProductoNoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente()));
        when(productoRepository.findById(2L)).thenReturn(Optional.empty());

        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 1);

        assertFalse(resultado.exito());
        assertEquals(404, resultado.codigo());
    }

    @Test
    void devuelveErrorSiLaCantidadEsInvalida() {
        ResultadoAgregarProducto resultado = carritoService.agregarProducto(1L, 2L, 0);

        assertFalse(resultado.exito());
        assertEquals(400, resultado.codigo());
        verifyNoInteractions(clienteRepository, productoRepository, carritoRepository);
    }
}
