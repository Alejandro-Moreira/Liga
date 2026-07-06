package com.appleboxestudios.futbol.ui;

import com.appleboxestudios.futbol.model.Jugador;
import com.appleboxestudios.futbol.model.Posicion;
import com.appleboxestudios.futbol.service.IJugadorService;
import com.appleboxestudios.futbol.util.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal de la aplicación.
 * Contiene el formulario CRUD de jugadores y la tabla de visualización.
 */
public class VentanaPrincipal extends JFrame {

    // Servicio de lógica de negocio
    private final IJugadorService jugadorService;

    // Componentes del formulario
    private JTextField txtNombre;
    private JComboBox<Posicion> cbPosicion;
    private JTextField txtEdad;
    private JTextField txtEquipo;

    // Botones de acción
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    // Tabla de jugadores
    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;

    // ID del jugador seleccionado para edición (-1 = ninguno)
    private int idSeleccionado = -1;

    // Columnas de la tabla
    private static final String[] COLUMNAS = {"ID", "Nombre", "Posición", "Edad", "Equipo"};

    public VentanaPrincipal(IJugadorService jugadorService) {
        this.jugadorService = jugadorService;
        initUI();
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     */
    private void initUI() {
        setTitle("Sistema de Gestión de Jugadores de Fútbol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 550);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 450));

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- Panel superior: Formulario ---
        JPanel panelFormulario = crearPanelFormulario();
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        // --- Panel central: Tabla ---
        JPanel panelTabla = crearPanelTabla();
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
    }

    /**
     * Crea el panel con los campos del formulario y los botones de acción.
     */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Jugador"));

        // Campos del formulario en grid
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nombre
        gbc.gridx = 0; gbc.gridy = 0;
        camposPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        camposPanel.add(txtNombre, gbc);

        // Posición
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Posición:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbPosicion = new JComboBox<>(Posicion.values());
        camposPanel.add(cbPosicion, gbc);

        // Edad
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEdad = new JTextField(10);
        camposPanel.add(txtEdad, gbc);

        // Equipo
        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Equipo:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEquipo = new JTextField(20);
        camposPanel.add(txtEquipo, gbc);

        panel.add(camposPanel, BorderLayout.CENTER);

        // Botones de acción
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        // Estado inicial: Modificar y Eliminar deshabilitados hasta seleccionar un jugador
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);

        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnModificar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnLimpiar);

        panel.add(botonesPanel, BorderLayout.SOUTH);

        // Registrar listeners
        registrarListeners();

        return panel;
    }

    /**
     * Crea el panel con la tabla de jugadores.
     */
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Jugadores Registrados"));

        // Modelo de tabla no editable
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaJugadores = new JTable(modeloTabla);
        tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaJugadores.getTableHeader().setReorderingAllowed(false);

        // Ajustar ancho de columna ID
        tablaJugadores.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaJugadores.getColumnModel().getColumn(0).setMaxWidth(60);

        // Listener para selección en la tabla
        tablaJugadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarJugadorSeleccionado();
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaJugadores);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Registra los listeners de los botones de acción.
     */
    private void registrarListeners() {
        btnAgregar.addActionListener(e -> agregarJugador());
        btnModificar.addActionListener(e -> modificarJugador());
        btnEliminar.addActionListener(e -> eliminarJugador());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    /**
     * Agrega un nuevo jugador con los datos del formulario.
     */
    private void agregarJugador() {
        // Validar campos
        String error = validarCampos();
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear jugador con los datos del formulario
        Jugador jugador = new Jugador(
                txtNombre.getText().trim(),
                (Posicion) cbPosicion.getSelectedItem(),
                Integer.parseInt(txtEdad.getText().trim()),
                txtEquipo.getText().trim()
        );

        jugadorService.add(jugador);
        actualizarTabla();
        limpiarFormulario();

        JOptionPane.showMessageDialog(this,
                "Jugador registrado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Modifica los datos del jugador seleccionado.
     */
    private void modificarJugador() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un jugador de la tabla para modificar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar campos
        String error = validarCampos();
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el jugador existente y actualizar sus datos
        Jugador jugador = jugadorService.getById(idSeleccionado);
        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                    "El jugador seleccionado ya no existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            actualizarTabla();
            return;
        }

        jugador.setNombre(txtNombre.getText().trim());
        jugador.setPosicion((Posicion) cbPosicion.getSelectedItem());
        jugador.setEdad(Integer.parseInt(txtEdad.getText().trim()));
        jugador.setEquipo(txtEquipo.getText().trim());

        jugadorService.update(jugador);
        actualizarTabla();
        limpiarFormulario();

        JOptionPane.showMessageDialog(this,
                "Jugador modificado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Elimina el jugador seleccionado, previa confirmación del usuario.
     */
    private void eliminarJugador() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un jugador de la tabla para eliminar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jugador jugador = jugadorService.getById(idSeleccionado);
        if (jugador == null) {
            limpiarFormulario();
            actualizarTabla();
            return;
        }

        // Confirmación antes de eliminar
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar al jugador \"" + jugador.getNombre() + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            jugadorService.delete(idSeleccionado);
            actualizarTabla();
            limpiarFormulario();

            JOptionPane.showMessageDialog(this,
                    "Jugador eliminado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Valida los campos del formulario.
     * @return mensaje de error si hay problemas, null si todo es válido
     */
    private String validarCampos() {
        if (!Validador.esTextoValido(txtNombre.getText())) {
            return "El campo 'Nombre' es obligatorio.";
        }
        if (!Validador.esEnteroPositivo(txtEdad.getText())) {
            return "El campo 'Edad' debe ser un número entero positivo.";
        }
        if (!Validador.esTextoValido(txtEquipo.getText())) {
            return "El campo 'Equipo' es obligatorio.";
        }
        return null;
    }

    /**
     * Carga los datos del jugador seleccionado en la tabla al formulario.
     */
    private void cargarJugadorSeleccionado() {
        int filaSeleccionada = tablaJugadores.getSelectedRow();
        if (filaSeleccionada == -1) {
            return;
        }

        // Obtener el ID de la primera columna
        idSeleccionado = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        Jugador jugador = jugadorService.getById(idSeleccionado);

        if (jugador != null) {
            txtNombre.setText(jugador.getNombre());
            cbPosicion.setSelectedItem(jugador.getPosicion());
            txtEdad.setText(String.valueOf(jugador.getEdad()));
            txtEquipo.setText(jugador.getEquipo());

            // Habilitar botones de edición/eliminación
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnAgregar.setEnabled(false);
        }
    }

    /**
     * Limpia todos los campos del formulario y resetea el estado de selección.
     */
    private void limpiarFormulario() {
        txtNombre.setText("");
        cbPosicion.setSelectedIndex(0);
        txtEdad.setText("");
        txtEquipo.setText("");
        idSeleccionado = -1;

        tablaJugadores.clearSelection();

        // Restaurar estado de botones
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    /**
     * Actualiza la tabla con la lista completa de jugadores del servicio.
     */
    public void actualizarTabla() {
        modeloTabla.setRowCount(0); // Limpiar filas actuales
        List<Jugador> jugadores = jugadorService.getAll();
        for (Jugador j : jugadores) {
            modeloTabla.addRow(new Object[]{
                    j.getId(),
                    j.getNombre(),
                    j.getPosicion().getDescripcion(),
                    j.getEdad(),
                    j.getEquipo()
            });
        }
    }
}
