package com.aydsii.demo.controllers;

import com.aydsii.demo.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // Método que agarra la petición delete para cancelar un pedido
    @DeleteMapping("/{idPedido}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long idPedido) {
        boolean fueCancelado = pedidoService.cancelarPedido(idPedido);
        
        if (fueCancelado) {
            return ResponseEntity.ok("Pedido cancelado exitosamente y stock devuelto.");
        } else {
            return ResponseEntity.badRequest().body("Error: El pedido no se encuentra en un estado válido para cancelación o no existe.");
        }
    }
}