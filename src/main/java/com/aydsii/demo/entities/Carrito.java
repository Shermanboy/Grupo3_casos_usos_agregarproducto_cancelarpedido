package com.aydsii.demo.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;

    @OneToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCarrito> detalles = new ArrayList<>();

    public Carrito() {
    }

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Agrega un producto al carrito. Si el producto ya existe, acumula la cantidad.
     *
     * @return true si se agregó; false si la cantidad es inválida o no hay stock.
     */
    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return false;
        }

        DetalleCarrito detalle = buscarDetalle(producto);
        int cantidadTotal = cantidad + (detalle == null ? 0 : detalle.getCantidad());

        if (producto.consultarDisponibilidad() < cantidadTotal) {
            return false;
        }

        if (detalle == null) {
            detalle = new DetalleCarrito(this, producto, cantidad);
            detalles.add(detalle);
        } else {
            detalle.incrementarCantidad(cantidad);
            detalle.recalcularSubtotal();
        }

        return true;
    }

    public int cantidadDe(Producto producto) {
        DetalleCarrito detalle = buscarDetalle(producto);
        return detalle == null ? 0 : detalle.getCantidad();
    }

    public BigDecimal calcularSubtotal() {
        return detalles.stream()
                .map(DetalleCarrito::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private DetalleCarrito buscarDetalle(Producto producto) {
        return detalles.stream()
                .filter(detalle -> mismoProducto(detalle.getProducto(), producto))
                .findFirst()
                .orElse(null);
    }

    private boolean mismoProducto(Producto primero, Producto segundo) {
        if (primero == null || segundo == null) {
            return false;
        }
        if (primero.getIdProducto() != null && segundo.getIdProducto() != null) {
            return primero.getIdProducto().equals(segundo.getIdProducto());
        }
        return primero == segundo;
    }

    public Long getIdCarrito() {
        return idCarrito;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleCarrito> getDetalles() {
        return detalles;
    }
}
