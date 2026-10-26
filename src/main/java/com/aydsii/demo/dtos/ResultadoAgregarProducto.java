package com.aydsii.demo.dtos;

public record ResultadoAgregarProducto(
        boolean exito,
        int codigo,
        String mensaje,
        CarritoResponse carrito
) {

    public static ResultadoAgregarProducto ok(String mensaje, CarritoResponse carrito) {
        return new ResultadoAgregarProducto(true, 200, mensaje, carrito);
    }

    public static ResultadoAgregarProducto error(String mensaje) {
        return new ResultadoAgregarProducto(false, 400, mensaje, null);
    }

    public static ResultadoAgregarProducto noEncontrado(String mensaje) {
        return new ResultadoAgregarProducto(false, 404, mensaje, null);
    }

    public static ResultadoAgregarProducto conflicto(String mensaje) {
        return new ResultadoAgregarProducto(false, 409, mensaje, null);
    }
}
