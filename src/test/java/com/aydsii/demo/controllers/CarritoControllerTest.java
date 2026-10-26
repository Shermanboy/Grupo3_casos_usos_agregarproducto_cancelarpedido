package com.aydsii.demo.controllers;

import com.aydsii.demo.dtos.CarritoResponse;
import com.aydsii.demo.dtos.ResultadoAgregarProducto;
import com.aydsii.demo.services.CarritoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarritoController.class)
class CarritoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoService carritoService;

    private CarritoResponse carritoResponse() {
        return new CarritoResponse(1L, 1L, List.of(), new BigDecimal("3000.00"));
    }

    @Test
    void agregaProductoYDevuelve200() throws Exception {
        when(carritoService.agregarProducto(1L, 2L, 2))
                .thenReturn(ResultadoAgregarProducto.ok("Producto agregado al carrito.", carritoResponse()));

        mockMvc.perform(post("/api/clientes/1/carrito/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idProducto":2,"cantidad":2}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.carrito.subtotal").value(3000.0));
    }

    @Test
    void devuelve409SiNoHayStock() throws Exception {
        when(carritoService.agregarProducto(1L, 2L, 5))
                .thenReturn(ResultadoAgregarProducto.conflicto(
                        "Stock insuficiente. Podés agregar hasta 1 unidad/es más."));

        mockMvc.perform(post("/api/clientes/1/carrito/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idProducto":2,"cantidad":5}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.exito").value(false))
                .andExpect(jsonPath("$.mensaje")
                        .value("Stock insuficiente. Podés agregar hasta 1 unidad/es más."));
    }

    @Test
    void devuelve400SiFaltanDatos() throws Exception {
        mockMvc.perform(post("/api/clientes/1/carrito/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"idProducto":2}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.exito").value(false));
    }

    @Test
    void devuelve404SiElCarritoNoExiste() throws Exception {
        when(carritoService.obtenerCarrito(1L)).thenReturn(null);

        mockMvc.perform(get("/api/clientes/1/carrito"))
                .andExpect(status().isNotFound());
    }
}
