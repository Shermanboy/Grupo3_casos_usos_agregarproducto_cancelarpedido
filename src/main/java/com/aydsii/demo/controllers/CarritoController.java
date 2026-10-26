package com.aydsii.demo.controllers;

import com.aydsii.demo.dtos.AgregarProductoRequest;
import com.aydsii.demo.dtos.CarritoResponse;
import com.aydsii.demo.dtos.ResultadoAgregarProducto;
import com.aydsii.demo.services.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes/{idCliente}/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/productos")
    public ResponseEntity<ResultadoAgregarProducto> agregarProducto(
            @PathVariable Long idCliente,
            @RequestBody AgregarProductoRequest request) {

        if (request == null || request.idProducto() == null || request.cantidad() == null) {
            return ResponseEntity.badRequest()
                    .body(ResultadoAgregarProducto.error("Los datos del producto son obligatorios."));
        }

        ResultadoAgregarProducto resultado =
                carritoService.agregarProducto(idCliente, request.idProducto(), request.cantidad());

        return ResponseEntity.status(resultado.codigo()).body(resultado);
    }

    @GetMapping
    public ResponseEntity<CarritoResponse> verCarrito(@PathVariable Long idCliente) {
        CarritoResponse carrito = carritoService.obtenerCarrito(idCliente);
        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carrito);
    }
}
