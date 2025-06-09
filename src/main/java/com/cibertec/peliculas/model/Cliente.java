package com.cibertec.peliculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;
    
    @Override
    public String toString() {
        return nombre;
    }
} 