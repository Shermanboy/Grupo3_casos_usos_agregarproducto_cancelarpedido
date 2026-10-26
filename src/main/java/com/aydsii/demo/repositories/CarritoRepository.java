package com.aydsii.demo.repositories;

import com.aydsii.demo.entities.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByCliente_IdCliente(Long idCliente);
}
