package com.aydsii.demo.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    private String estado; // "Pendiente", "Aceptado", "Cancelado"
    private Date fechaCreacion;
    private Double montoTotal;

    // Relación de composición: Un pedido tiene muchos detalles
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;

    // Constructor vacío exigido por JPA
    public Pedido() {}

    // --- Métodos de tu Modelo de Dominio ---

    public boolean puedeCancelarse() {
        return this.estado.equals("Pendiente") || this.estado.equals("Aceptado");
    }

    public void cancelar() {
        if (puedeCancelarse()) {
            this.estado = "Cancelado";
        }
    }

    // Getters y Setters
    public Long getIdPedido() { return idPedido; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetallePedido> getDetalles() { return detalles; }
}