package com.aydsii.demo.dtos;

import com.aydsii.demo.entities.Carrito;

import java.math.BigDecimal;
import java.util.List;

public record CarritoResponse(
        Long idCarrito,
        Long idCliente,
        List<DetalleCarritoResponse> detalles,
        BigDecimal subtotal
) {

    public static CarritoResponse from(Carrito carrito) {
        return new CarritoResponse(
                carrito.getIdCarrito(),
                carrito.getCliente() == null ? null : carrito.getCliente().getIdCliente(),
                carrito.getDetalles().stream()
                        .map(DetalleCarritoResponse::from)
                        .toList(),
                carrito.calcularSubtotal()
        );
    }
}
