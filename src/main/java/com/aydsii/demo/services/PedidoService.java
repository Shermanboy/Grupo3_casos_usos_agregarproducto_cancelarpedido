package com.aydsii.demo.services;

import com.aydsii.demo.entities.DetallePedido;
import com.aydsii.demo.entities.Pedido;
import com.aydsii.demo.entities.Producto;
import com.aydsii.demo.repositories.PedidoRepository;
import com.aydsii.demo.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    @Transactional 
    public boolean cancelarPedido(Long idPedido) {
    Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            
            
            if (pedido.puedeCancelarse()) {
                pedido.cancelar(); // Cambia el estado a "Cancelado"
                
           
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    producto.sumarStock(detalle.getCantidad());
                    productoRepository.save(producto); // Actualizamos el stock en la DB
                }
                
              pedidoRepository.actualizarEstado(pedido.getIdPedido(), pedido.getEstado());
                return true;
            }
        }
        return false; 
    }
}