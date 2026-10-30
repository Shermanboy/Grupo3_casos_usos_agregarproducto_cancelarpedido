package com.aydsii.demo.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aydsii.demo.dtos.CarritoResponse;
import com.aydsii.demo.dtos.ResultadoAgregarProducto;
import com.aydsii.demo.entities.Carrito;
import com.aydsii.demo.entities.Cliente;
import com.aydsii.demo.entities.Producto;
import com.aydsii.demo.repositories.CarritoRepository;
import com.aydsii.demo.repositories.ClienteRepository;
import com.aydsii.demo.repositories.ProductoRepository;

@Service
public class CarritoService {

    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CarritoRepository carritoRepository;

    public CarritoService(ClienteRepository clienteRepository,
                          ProductoRepository productoRepository,
                          CarritoRepository carritoRepository) {
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.carritoRepository = carritoRepository;
    }

    @Transactional
    public ResultadoAgregarProducto agregarProducto(Long idCliente, Long idProducto, int cantidad) {
        if (idCliente == null || idProducto == null) {
            return ResultadoAgregarProducto.error("El cliente y el producto son obligatorios.");
        }

        if (cantidad <= 0) {
            return ResultadoAgregarProducto.error("La cantidad debe ser mayor a cero.");
        }

        Optional<Cliente> clienteOpt = clienteRepository.findById(idCliente);
        if (clienteOpt.isEmpty()) {
            return ResultadoAgregarProducto.noEncontrado("No existe el cliente con id " + idCliente + ".");
        }

        Optional<Producto> productoOpt = productoRepository.findById(idProducto);
        if (productoOpt.isEmpty()) {
            return ResultadoAgregarProducto.noEncontrado("No existe el producto con id " + idProducto + ".");
        }

        Producto producto = productoOpt.get();
        if (producto.getPrecioUnitario() == null
                || producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoAgregarProducto.conflicto("El producto no tiene un precio válido.");
        }

        Cliente cliente = clienteOpt.get();
        Carrito carrito = carritoRepository.findByCliente_IdCliente(idCliente)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    cliente.asignarCarrito(nuevo);
                    return nuevo;
                });

        if (!carrito.agregarProducto(producto, cantidad)) {
            int yaEnCarrito = carrito.cantidadDe(producto);
            int maximo = Math.max(0, producto.consultarDisponibilidad() - yaEnCarrito);
            return ResultadoAgregarProducto.conflicto(
                    "Stock insuficiente. Podés agregar hasta " + maximo + " unidad/es más."
            );
        }

        Carrito guardado = carritoRepository.save(carrito);
        return ResultadoAgregarProducto.ok(
                "Producto agregado al carrito.",
                CarritoResponse.construirRespuesta(guardado)
        );
    }

    @Transactional(readOnly = true)
    public CarritoResponse obtenerCarrito(Long idCliente) {
        return carritoRepository.findByCliente_IdCliente(idCliente)
                .map(CarritoResponse::construirRespuesta)
                .orElse(null);
    }
}
