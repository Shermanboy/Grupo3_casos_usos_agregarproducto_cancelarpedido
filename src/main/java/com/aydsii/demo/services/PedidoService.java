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

    @Transactional // Garantiza que si algo falla, no se guarde nada a medias
    public boolean cancelarPedido(Long idPedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);
        
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            
            // Acá le preguntamos a la entidad si es válido cancelarla (tu modelo de dominio)
            if (pedido.puedeCancelarse()) {
                pedido.cancelar(); // Cambia el estado a "Cancelado"
                
                // Recorremos los detalles para devolver el stock al inventario
                for (DetallePedido detalle : pedido.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    producto.sumarStock(detalle.getCantidad());
                    productoRepository.save(producto); // Actualizamos el stock en la DB
                }
                
                pedidoRepository.save(pedido); // Actualizamos el estado del pedido en la DB
                return true;
            }
        }
        return false; // Retorna falso si no se puede cancelar o si no existe
    }
}