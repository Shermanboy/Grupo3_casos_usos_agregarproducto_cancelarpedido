package com.aydsii.demo.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CarritoTest {

    private Producto producto(String nombre, String precio, int stock) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecioUnitario(new BigDecimal(precio));
        producto.setStockDisponible(stock);
        return producto;
    }

    @Test
    void agregaProductoNuevoYCalculaSubtotal() {
        Carrito carrito = new Carrito();
        Producto maceta = producto("Maceta", "1500.00", 10);

        assertTrue(carrito.agregarProducto(maceta, 2));
        assertEquals(1, carrito.getDetalles().size());
        assertEquals(2, carrito.getDetalles().get(0).getCantidad());
        assertEquals(new BigDecimal("1500.00"), carrito.getDetalles().get(0).getPrecioUnitario());
        assertEquals(new BigDecimal("3000.00"), carrito.getDetalles().get(0).getSubtotal());
        assertEquals(new BigDecimal("3000.00"), carrito.calcularSubtotal());
    }

    @Test
    void acumulaCantidadSiElProductoYaEstaEnElCarrito() {
        Carrito carrito = new Carrito();
        Producto maceta = producto("Maceta", "1500.00", 10);

        assertTrue(carrito.agregarProducto(maceta, 2));
        assertTrue(carrito.agregarProducto(maceta, 3));

        assertEquals(1, carrito.getDetalles().size());
        assertEquals(5, carrito.getDetalles().get(0).getCantidad());
        assertEquals(new BigDecimal("7500.00"), carrito.calcularSubtotal());
    }

    @Test
    void rechazaCantidadMayorAlStock() {
        Carrito carrito = new Carrito();
        Producto maceta = producto("Maceta", "1500.00", 2);

        assertFalse(carrito.agregarProducto(maceta, 3));
        assertTrue(carrito.getDetalles().isEmpty());
        assertEquals(0, carrito.cantidadDe(maceta));
    }

    @Test
    void rechazaCantidadInvalida() {
        Carrito carrito = new Carrito();
        Producto maceta = producto("Maceta", "1500.00", 10);

        assertFalse(carrito.agregarProducto(maceta, 0));
        assertFalse(carrito.agregarProducto(maceta, -1));
        assertTrue(carrito.getDetalles().isEmpty());
    }

    @Test
    void calculaSubtotalConVariosProductos() {
        Carrito carrito = new Carrito();
        Producto maceta = producto("Maceta", "1500.00", 10);
        Producto compostera = producto("Compostera", "2000.00", 10);

        assertTrue(carrito.agregarProducto(maceta, 2));
        assertTrue(carrito.agregarProducto(compostera, 1));

        assertEquals(2, carrito.getDetalles().size());
        assertEquals(new BigDecimal("5000.00"), carrito.calcularSubtotal());
    }
}
