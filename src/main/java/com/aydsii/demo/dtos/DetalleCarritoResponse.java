package com.aydsii.demo.dtos;

import com.aydsii.demo.entities.DetalleCarrito;

import java.math.BigDecimal;

public record DetalleCarritoResponse(
        Long idProducto,
        String nombre,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {

    public static DetalleCarritoResponse construirRespuesta(DetalleCarrito detalle) {
        return new DetalleCarritoResponse(
                detalle.getProducto().getIdProducto(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                detalle.getSubtotal()
        );
    }
}
