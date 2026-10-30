package com.aydsii.demo.repositories;

import com.aydsii.demo.entities.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.estado = :estado WHERE p.idPedido = :id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") String estado);
}