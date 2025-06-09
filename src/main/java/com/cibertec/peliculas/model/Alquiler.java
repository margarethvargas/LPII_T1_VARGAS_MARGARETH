package com.cibertec.peliculas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "alquileres")
public class Alquiler {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Long idAlquiler;
    
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP) // Se almacena como timestamp en la base de datos
    private LocalDateTime fecha;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;
    
    @Min(value = 0, message = "El total no puede ser negativo")
    @Column(name = "total", nullable = false)
    private Double total;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoAlquiler estado;
    
    @OneToMany(mappedBy = "alquiler", cascade = CascadeType.ALL) // Se propaga la persistencia(persist, remove, merge, refresh, etc.) a los detalles
    private List<DetalleAlquiler> detalles;
    
    @PrePersist // Se ejecuta antes de persistir el alquiler
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        this.estado = EstadoAlquiler.ACTIVO;
    }
} 