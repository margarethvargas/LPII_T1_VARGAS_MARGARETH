package com.cibertec.peliculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "peliculas")
public class Pelicula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Long idPelicula;
    
    @NotBlank(message = "El título no puede estar vacío")
    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;
    
    @NotBlank(message = "El género no puede estar vacío")
    @Column(name = "genero", nullable = false, length = 50)
    private String genero;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Override
    public String toString() {
        return titulo + " (" + genero + ")";
    }
} 