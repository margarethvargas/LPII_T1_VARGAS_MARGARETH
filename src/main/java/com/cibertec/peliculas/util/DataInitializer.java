package com.cibertec.peliculas.util;

import com.cibertec.peliculas.model.Cliente;
import com.cibertec.peliculas.model.Pelicula;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DataInitializer {
    
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD2_VargasSalas");
        EntityManager em = emf.createEntityManager();
        
        try {
            // Verificar si ya existen datos usando find()
            Cliente cliente = em.find(Cliente.class, 1);
            Pelicula pelicula = em.find(Pelicula.class, 1);
            
            // Solo insertar si no hay datos
            if (cliente == null && pelicula == null) {
                System.out.println("Insertando datos de ejemplo...");
                em.getTransaction().begin();
                
                // Insertar clientes
                Cliente cliente1 = new Cliente();
                cliente1.setNombre("Margareth Vargas");
                cliente1.setEmail("maki@email.com");
                em.persist(cliente1);
                
                Cliente cliente2 = new Cliente();
                cliente2.setNombre("Sabina Vallejo");
                cliente2.setEmail("sabi@email.com");
                em.persist(cliente2);
                
                Cliente cliente3 = new Cliente();
                cliente3.setNombre("Martina Vallejo");
                cliente3.setEmail("tini@email.com");
                em.persist(cliente3);
                
                Cliente cliente4 = new Cliente();
                cliente4.setNombre("Gambino Vallejo");
                cliente4.setEmail("gambi@email.com");
                em.persist(cliente4);
                
                // Insertar películas
                Pelicula pelicula1 = new Pelicula();
                pelicula1.setTitulo("Star Wars");
                pelicula1.setGenero("Ciencia Ficción");
                pelicula1.setStock(3);
                em.persist(pelicula1);
                
                Pelicula pelicula2 = new Pelicula();
                pelicula2.setTitulo("Back to the Future");
                pelicula2.setGenero("Ciencia Ficción");
                pelicula2.setStock(6);
                em.persist(pelicula2);
                
                Pelicula pelicula3 = new Pelicula();
                pelicula3.setTitulo("Interstellar");
                pelicula3.setGenero("Ciencia Ficción");
                pelicula3.setStock(5);
                em.persist(pelicula3);
                
                Pelicula pelicula4 = new Pelicula();
                pelicula4.setTitulo("The Matrix");
                pelicula4.setGenero("Ciencia Ficción");
                pelicula4.setStock(4);
                em.persist(pelicula4);
                
                em.getTransaction().commit();
                System.out.println("Datos de ejemplo insertados correctamente");
            } else {
                System.out.println("La base de datos ya contiene datos. No se insertarán datos de ejemplo.");
            }
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
} 