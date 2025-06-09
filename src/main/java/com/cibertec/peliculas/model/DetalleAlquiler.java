    package com.cibertec.peliculas.model;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Min;
    import lombok.Data;

    @Data 
    @Entity
    @Table(name = "detalle_alquiler")
    public class DetalleAlquiler {
        
        @EmbeddedId // Se utiliza para definir una clave compuesta
        private DetalleAlquilerId id = new DetalleAlquilerId(); // Se inicializa el id para evitar nullpointers(objetos nulos)
        
        @ManyToOne 
        @MapsId("idAlquiler") // Se utiliza para mapear la clave compuesta con la entidad Alquiler 
        @JoinColumn(name = "id_alquiler")
        private Alquiler alquiler;
        
        @ManyToOne
        @MapsId("idPelicula")
        @JoinColumn(name = "id_pelicula")
        private Pelicula pelicula;
        
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        @Column(name = "cantidad", nullable = false)
        private Integer cantidad;
        
        @PrePersist // Se ejecuta antes de persistir el detalle alquiler
        public void prePersist() { // Se utiliza para inicializar el id de la clave compuesta
            if (alquiler != null) { // Se utiliza para inicializar el id de la clave compuesta
                id.setIdAlquiler(alquiler.getIdAlquiler());
            }
            if (pelicula != null) { // Se utiliza para inicializar el id de la clave compuesta
                id.setIdPelicula(pelicula.getIdPelicula());
            }
        }
    }

    @Embeddable // Se utiliza para definir una clave compuesta
    @Data // Se utiliza para generar los getters y setters
    class DetalleAlquilerId implements java.io.Serializable { // Se implementa la interfaz Serializable para que la clave compuesta se pueda serializar(enviar a otro lado) y deserializar(recibir de otro lado) y que se pueda usar en otras clases (se usa en la clase DetalleAlquiler)
        
        @Column(name = "id_alquiler") // Se utiliza para definir el nombre de la columna en la base de datos
        private Long idAlquiler;
        
        @Column(name = "id_pelicula") // Se utiliza para definir el nombre de la columna en la base de datos
        private Long idPelicula;
    } 