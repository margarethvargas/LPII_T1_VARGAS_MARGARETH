package com.cibertec.peliculas.util;
//Imports requeridos 
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "BD2_VargasSalas";
    private static EntityManagerFactory factory;

    public static EntityManager getEntityManager() {
        if (factory == null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return factory.createEntityManager();
    }

    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}