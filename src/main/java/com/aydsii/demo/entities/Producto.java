package com.aydsii.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    private String nombre;
    private Integer stockDisponible;

    public Producto() {}

    // --- Método de tu Modelo de Dominio ---
    public void sumarStock(int cantidad) {
        this.stockDisponible += cantidad;
    }

    // Getters y Setters
    public Long getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }
}