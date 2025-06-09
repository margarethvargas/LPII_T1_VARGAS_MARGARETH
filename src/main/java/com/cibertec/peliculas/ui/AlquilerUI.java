package com.cibertec.peliculas.ui;

import com.cibertec.peliculas.model.*;
import com.cibertec.peliculas.util.JPAUtil;
import jakarta.persistence.EntityManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class AlquilerUI extends JFrame {
    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Pelicula> cmbPeliculas;
    private JSpinner spnCantidad;
    private JTable tblDetalles;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotal;
    private List<DetalleAlquiler> detalles;
    private double total;
    
    public AlquilerUI() {
        setTitle("Sistema de Alquiler de Películas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        detalles = new ArrayList<>();
        total = 0.0;
        
        initComponents();
        cargarDatos();
    }
    
    private void initComponents() {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel de selección
        JPanel panelSeleccion = new JPanel();
        panelSeleccion.setLayout(new BoxLayout(panelSeleccion, BoxLayout.Y_AXIS));
        panelSeleccion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel para Cliente
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCliente.add(new JLabel("Cliente:"));
        cmbClientes = new JComboBox<>();
        cmbClientes.setPreferredSize(new Dimension(200, 25));
        cmbClientes.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Limpiar detalles cuando se cambia el cliente
                    detalles.clear();
                    actualizarTabla();
                }
            }
        });
        panelCliente.add(cmbClientes);
        panelSeleccion.add(panelCliente);
        
        // Panel para Película
        JPanel panelPelicula = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPelicula.add(new JLabel("Película:"));
        cmbPeliculas = new JComboBox<>();
        cmbPeliculas.setPreferredSize(new Dimension(200, 25));
        panelPelicula.add(cmbPeliculas);
        panelSeleccion.add(panelPelicula);
        
        // Panel para Cantidad
        JPanel panelCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCantidad.add(new JLabel("Cantidad:"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spnCantidad.setPreferredSize(new Dimension(80, 25));
        panelCantidad.add(spnCantidad);
        panelSeleccion.add(panelCantidad);
        
        // Botón Agregar
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarDetalle()); //
        panelSeleccion.add(Box.createVerticalStrut(10)); // Espacio vertical
        panelSeleccion.add(btnAgregar);
        
        // Tabla de detalles
        String[] columnas = {"Película", "Cantidad", "Subtotal"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tblDetalles = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tblDetalles);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: S/. 0.00");
        panelInferior.add(lblTotal);
        
        JButton btnRegistrar = new JButton("Registrar Alquiler");
        btnRegistrar.addActionListener(e -> registrarAlquiler());
        panelInferior.add(btnRegistrar);
        
        // Agregar componentes al panel principal
        panel.add(panelSeleccion, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void cargarDatos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Cargar clientes
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            cmbClientes.removeAllItems();
            for (Cliente cliente : clientes) {
                cmbClientes.addItem(cliente);
            }
            
            // Cargar películas
            List<Pelicula> peliculas = em.createQuery("SELECT p FROM Pelicula p", Pelicula.class).getResultList();
            cmbPeliculas.removeAllItems();
            for (Pelicula pelicula : peliculas) {
                cmbPeliculas.addItem(pelicula);
            }
        } finally {
            em.close();
        }
    }
    
    private void agregarDetalle() {
        Pelicula pelicula = (Pelicula) cmbPeliculas.getSelectedItem();
        int cantidad = (Integer) spnCantidad.getValue();
        
        if (pelicula != null && cantidad > 0) {
            // Verificar si la película ya está en los detalles
            boolean yaExiste = detalles.stream()
                .anyMatch(item -> item.getPelicula().getIdPelicula().equals(pelicula.getIdPelicula())); //
            if (yaExiste) {
                JOptionPane.showMessageDialog(this, "Esta película ya fue agregada al detalle.");
                return;
            }
            EntityManager em = JPAUtil.getEntityManager();
            try {
                // Obtenemos la película gestionada por JPA
                Pelicula peliculaGestionada = em.find(Pelicula.class, pelicula.getIdPelicula());
                
                DetalleAlquiler detalle = new DetalleAlquiler();
                detalle.setPelicula(peliculaGestionada);
                detalle.setCantidad(cantidad);
                
                detalles.add(detalle);
                actualizarTabla();
            } finally {
                em.close();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una película y una cantidad válida.");
        }
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        total = 0.0;
        
        for (DetalleAlquiler detalle : detalles) {
            double subtotal = detalle.getCantidad() * 10.0; // Precio fijo de S/. 10 por película
            total += subtotal;
            
            Object[] fila = {
                detalle.getPelicula().getTitulo(),
                detalle.getCantidad(),
                String.format("S/. %.2f", subtotal)
            };
            modeloTabla.addRow(fila);
        }
        
        lblTotal.setText(String.format("Total: S/. %.2f", total));
    }
    
    private void registrarAlquiler() {
        if (detalles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un detalle al alquiler");
            return;
        }
        
        Cliente cliente = (Cliente) cmbClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente");
            return;
        }
        
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Obtenemos el cliente gestionado por JPA
            Cliente clienteGestionado = em.find(Cliente.class, cliente.getIdCliente());
            
            // Primero persistimos el alquiler
            Alquiler alquiler = new Alquiler();
            alquiler.setCliente(clienteGestionado);
            alquiler.setTotal(total);
            em.persist(alquiler);
            em.flush(); // Forzamos el flush para obtener el ID del alquiler
            
            // Luego persistimos los detalles
            for (DetalleAlquiler detalle : detalles) {
                // Obtenemos la película gestionada por JPA
                Pelicula peliculaGestionada = em.find(Pelicula.class, detalle.getPelicula().getIdPelicula());
                detalle.setPelicula(peliculaGestionada);
                detalle.setAlquiler(alquiler);
                em.persist(detalle);
            }
            
            em.getTransaction().commit();
            
            JOptionPane.showMessageDialog(this, "Alquiler registrado correctamente");
            limpiarFormulario();
            
        } catch (Exception e) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(this, "Error al registrar el alquiler: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    private void limpiarFormulario() {
        detalles.clear();
        modeloTabla.setRowCount(0);
        total = 0.0;
        lblTotal.setText("Total: S/. 0.00");
        spnCantidad.setValue(1);
    }

} 