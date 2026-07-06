package com.appleboxestudios.futbol.ui;

import com.appleboxestudios.futbol.model.Estadistica;
import com.appleboxestudios.futbol.model.Jugador;
import com.appleboxestudios.futbol.model.Posicion;
import com.appleboxestudios.futbol.service.IJugadorService;
import com.appleboxestudios.futbol.util.Validador;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana principal de la aplicación.
 * Contiene el formulario CRUD de jugadores y la tabla de visualización.
 * Incluye validación visual con bordes rojos en campos inválidos.
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
    private JButton btnEstadisticas;

    // Componentes de búsqueda
    private JTextField txtBusqueda;
    private JButton btnLimpiarBusqueda;

    // Tabla de jugadores
    private JTable tablaJugadores;
    private DefaultTableModel modeloTabla;

    // Tabla de ranking
    private JTable tablaRanking;
    private DefaultTableModel modeloTablaRanking;

    // ID del jugador seleccionado para edición (-1 = ninguno)
    private int idSeleccionado = -1;

    // Columnas de la tabla
    private static final String[] COLUMNAS = {
            "ID", "Nombre", "Posición", "Edad", "Equipo", "Goles", "Asistencias", "Partidos"
    };

    // Bordes para retroalimentación visual de validación
    private static final Border BORDE_NORMAL = UIManager.getBorder("TextField.border");
    private static final Border BORDE_ERROR = BorderFactory.createLineBorder(Color.RED, 2);

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
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 550));

        JTabbedPane tabbedPane = new JTabbedPane();

        // --- Pestaña 1: Gestión de Jugadores ---
        JPanel panelGestion = new JPanel(new BorderLayout(10, 10));
        panelGestion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        JPanel panelFormulario = crearPanelFormulario();
        panelSuperior.add(panelFormulario);

        JPanel panelBusqueda = crearPanelBusqueda();
        panelSuperior.add(panelBusqueda);

        panelGestion.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelTabla = crearPanelTabla();
        panelGestion.add(panelTabla, BorderLayout.CENTER);

        tabbedPane.addTab("Gestión de Jugadores", panelGestion);

        // --- Pestaña 2: Ranking y Rendimiento ---
        JPanel panelRanking = crearPanelRanking();
        tabbedPane.addTab("Ranking de Rendimiento", panelRanking);

        // Listener para actualizar el ranking al cambiar de pestaña
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) {
                actualizarRanking();
            }
        });

        setContentPane(tabbedPane);
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
        btnEstadisticas = new JButton("Estadísticas");
        btnLimpiar = new JButton("Limpiar");

        // Estado inicial: Modificar, Eliminar y Estadísticas deshabilitados
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnEstadisticas.setEnabled(false);

        botonesPanel.add(btnAgregar);
        botonesPanel.add(btnModificar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnEstadisticas);
        botonesPanel.add(btnLimpiar);

        panel.add(botonesPanel, BorderLayout.SOUTH);

        // Registrar listeners
        registrarListeners();

        return panel;
    }

    /**
     * Crea el panel de búsqueda con campo de texto y botón para limpiar filtro.
     * El filtrado se realiza en tiempo real mientras el usuario escribe.
     */
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Jugador"));

        // Campo de búsqueda con icono de lupa textual
        JPanel campoBusqueda = new JPanel(new BorderLayout(5, 0));
        campoBusqueda.add(new JLabel("  \uD83D\uDD0D Nombre:"), BorderLayout.WEST);
        txtBusqueda = new JTextField(20);
        campoBusqueda.add(txtBusqueda, BorderLayout.CENTER);

        // Botón para limpiar búsqueda
        btnLimpiarBusqueda = new JButton("Limpiar Filtro");
        campoBusqueda.add(btnLimpiarBusqueda, BorderLayout.EAST);

        panel.add(campoBusqueda, BorderLayout.CENTER);

        // Filtrado en tiempo real con DocumentListener
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrarTabla();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrarTabla();
            }
        });

        // Listener para limpiar búsqueda
        btnLimpiarBusqueda.addActionListener(e -> {
            txtBusqueda.setText("");
            actualizarTabla();
        });

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
        btnEstadisticas.addActionListener(e -> abrirDialogoEstadisticas());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    /**
     * Agrega un nuevo jugador con los datos del formulario.
     * Valida todos los campos y muestra errores acumulados si los hay.
     */
    private void agregarJugador() {
        // Limpiar bordes previos antes de validar
        resetearBordes();

        // Validar campos con el nuevo sistema que acumula errores
        List<String> errores = Validador.validarJugador(
                txtNombre.getText(),
                txtEdad.getText(),
                txtEquipo.getText()
        );

        if (!errores.isEmpty()) {
            marcarCamposInvalidos();
            String mensajeError = Validador.formatearErrores(errores);
            JOptionPane.showMessageDialog(this, mensajeError, "Error de validación",
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
     * Valida todos los campos antes de aplicar cambios.
     */
    private void modificarJugador() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un jugador de la tabla para modificar.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Limpiar bordes previos antes de validar
        resetearBordes();

        // Validar campos
        List<String> errores = Validador.validarJugador(
                txtNombre.getText(),
                txtEdad.getText(),
                txtEquipo.getText()
        );

        if (!errores.isEmpty()) {
            marcarCamposInvalidos();
            String mensajeError = Validador.formatearErrores(errores);
            JOptionPane.showMessageDialog(this, mensajeError, "Error de validación",
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
     * Abre un diálogo modal para editar las estadísticas del jugador seleccionado.
     * La edición de estadísticas es independiente de los datos personales.
     */
    private void abrirDialogoEstadisticas() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un jugador de la tabla para editar sus estadísticas.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Jugador jugador = jugadorService.getById(idSeleccionado);
        if (jugador == null) {
            JOptionPane.showMessageDialog(this,
                    "El jugador seleccionado ya no existe.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            limpiarFormulario();
            actualizarTabla();
            return;
        }

        Estadistica est = jugador.getEstadistica();

        // Crear diálogo modal
        JDialog dialogo = new JDialog(this, "Estadísticas de " + jugador.getNombre(), true);
        dialogo.setSize(400, 280);
        dialogo.setLocationRelativeTo(this);
        dialogo.setResizable(false);

        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel de campos
        JPanel camposPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Título con nombre del jugador
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblTitulo = new JLabel("Jugador: " + jugador.getNombre() + " (" + jugador.getPosicion() + ")");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 13f));
        camposPanel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Goles
        gbc.gridx = 0; gbc.gridy = 1;
        camposPanel.add(new JLabel("Goles:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtGoles = new JTextField(String.valueOf(est.getGoles()), 10);
        camposPanel.add(txtGoles, gbc);

        // Asistencias
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Asistencias:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtAsistencias = new JTextField(String.valueOf(est.getAsistencias()), 10);
        camposPanel.add(txtAsistencias, gbc);

        // Partidos jugados
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Partidos Jugados:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField txtPartidos = new JTextField(String.valueOf(est.getPartidosJugados()), 10);
        camposPanel.add(txtPartidos, gbc);

        panelContenido.add(camposPanel, BorderLayout.CENTER);

        // Botones del diálogo
        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            // Resetear bordes
            txtGoles.setBorder(BORDE_NORMAL);
            txtAsistencias.setBorder(BORDE_NORMAL);
            txtPartidos.setBorder(BORDE_NORMAL);

            // Validar estadísticas
            List<String> errores = Validador.validarEstadisticas(
                    txtGoles.getText(), txtAsistencias.getText(), txtPartidos.getText());

            if (!errores.isEmpty()) {
                // Marcar campos inválidos
                if (!Validador.esEnteroNoNegativo(txtGoles.getText())) {
                    txtGoles.setBorder(BORDE_ERROR);
                }
                if (!Validador.esEnteroNoNegativo(txtAsistencias.getText())) {
                    txtAsistencias.setBorder(BORDE_ERROR);
                }
                if (!Validador.esEnteroNoNegativo(txtPartidos.getText())) {
                    txtPartidos.setBorder(BORDE_ERROR);
                }
                JOptionPane.showMessageDialog(dialogo,
                        Validador.formatearErrores(errores),
                        "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Actualizar estadísticas
            est.setGoles(Integer.parseInt(txtGoles.getText().trim()));
            est.setAsistencias(Integer.parseInt(txtAsistencias.getText().trim()));
            est.setPartidosJugados(Integer.parseInt(txtPartidos.getText().trim()));

            jugadorService.update(jugador);
            actualizarTabla();

            JOptionPane.showMessageDialog(dialogo,
                    "Estadísticas actualizadas exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogo.dispose();
        });

        btnCancelar.addActionListener(e -> dialogo.dispose());

        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnCancelar);
        panelContenido.add(botonesPanel, BorderLayout.SOUTH);

        dialogo.setContentPane(panelContenido);
        dialogo.setVisible(true);
    }

    /**
     * Crea el panel para la pestaña de ranking de rendimiento.
     */
    private JPanel crearPanelRanking() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Cabecera del panel de ranking
        JPanel panelCabecera = new JPanel(new BorderLayout());
        JLabel lblTitulo = new JLabel("Ranking de Rendimiento Deportivo (Top Jugadores)");
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 16f));
        panelCabecera.add(lblTitulo, BorderLayout.WEST);

        JLabel lblFormula = new JLabel("Fórmula: Puntaje = (Goles * 2) + (Asistencias * 1.5)");
        lblFormula.setFont(lblFormula.getFont().deriveFont(Font.ITALIC, 11f));
        panelFormulaColor(lblFormula);
        panelCabecera.add(lblFormula, BorderLayout.EAST);
        panel.add(panelCabecera, BorderLayout.NORTH);

        // Tabla de ranking
        String[] columnasRanking = {
                "Puesto", "Nombre", "Posición", "Equipo", "Goles", "Asistencias", "Partidos", "Puntaje"
        };

        modeloTablaRanking = new DefaultTableModel(columnasRanking, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRanking = new JTable(modeloTablaRanking);
        tablaRanking.getTableHeader().setReorderingAllowed(false);

        // Ajustar columnas
        tablaRanking.getColumnModel().getColumn(0).setPreferredWidth(60);
        tablaRanking.getColumnModel().getColumn(0).setMaxWidth(80);
        tablaRanking.getColumnModel().getColumn(7).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(tablaRanking);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void panelFormulaColor(JLabel label) {
        label.setForeground(Color.BLUE.darker());
    }

    /**
     * Llena la tabla de ranking con la lista ordenada de jugadores.
     */
    private void actualizarRanking() {
        if (modeloTablaRanking == null) {
            return;
        }
        modeloTablaRanking.setRowCount(0);
        List<Jugador> ranking = jugadorService.getRanking();
        int puesto = 1;
        for (Jugador j : ranking) {
            Estadistica est = j.getEstadistica();
            modeloTablaRanking.addRow(new Object[]{
                    puesto + "º",
                    j.getNombre(),
                    j.getPosicion().getDescripcion(),
                    j.getEquipo(),
                    est.getGoles(),
                    est.getAsistencias(),
                    est.getPartidosJugados(),
                    String.format("%.1f", j.getPuntajeRendimiento())
            });
            puesto++;
        }
    }

    /**
     * Marca visualmente con borde rojo los campos que tienen datos inválidos.
     * Evalúa cada campo individualmente para resaltar solo los problemáticos.
     */
    private void marcarCamposInvalidos() {
        if (!Validador.esTextoValido(txtNombre.getText())) {
            txtNombre.setBorder(BORDE_ERROR);
        }
        if (!Validador.esEnteroPositivo(txtEdad.getText())) {
            txtEdad.setBorder(BORDE_ERROR);
        }
        if (!Validador.esTextoValido(txtEquipo.getText())) {
            txtEquipo.setBorder(BORDE_ERROR);
        }
    }

    /**
     * Restaura los bordes de todos los campos de texto a su estado normal.
     */
    private void resetearBordes() {
        txtNombre.setBorder(BORDE_NORMAL);
        txtEdad.setBorder(BORDE_NORMAL);
        txtEquipo.setBorder(BORDE_NORMAL);
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

            // Limpiar bordes de error al cargar datos válidos
            resetearBordes();

            // Habilitar botones de edición/eliminación/estadísticas
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);
            btnEstadisticas.setEnabled(true);
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

        // Restaurar bordes y estado de botones
        resetearBordes();
        btnAgregar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnEstadisticas.setEnabled(false);
    }

    /**
     * Actualiza la tabla con la lista completa de jugadores del servicio.
     * Si hay un filtro de búsqueda activo, aplica el filtro automáticamente.
     */
    public void actualizarTabla() {
        String textoBusqueda = (txtBusqueda != null) ? txtBusqueda.getText().trim() : "";
        if (!textoBusqueda.isEmpty()) {
            filtrarTabla();
        } else {
            cargarJugadoresEnTabla(jugadorService.getAll());
        }
        // Actualizar ranking en tiempo real
        actualizarRanking();
    }

    /**
     * Filtra la tabla mostrando solo los jugadores cuyo nombre coincida
     * parcialmente con el texto de búsqueda (insensible a mayúsculas).
     */
    private void filtrarTabla() {
        String textoBusqueda = txtBusqueda.getText().trim();
        if (textoBusqueda.isEmpty()) {
            cargarJugadoresEnTabla(jugadorService.getAll());
        } else {
            List<Jugador> resultados = jugadorService.findByNombre(textoBusqueda);
            cargarJugadoresEnTabla(resultados);
        }
    }

    /**
     * Carga una lista de jugadores en la tabla.
     * Método auxiliar usado tanto por actualizarTabla como por filtrarTabla.
     */
    private void cargarJugadoresEnTabla(List<Jugador> jugadores) {
        modeloTabla.setRowCount(0);
        for (Jugador j : jugadores) {
            Estadistica est = j.getEstadistica();
            modeloTabla.addRow(new Object[]{
                    j.getId(),
                    j.getNombre(),
                    j.getPosicion().getDescripcion(),
                    j.getEdad(),
                    j.getEquipo(),
                    est.getGoles(),
                    est.getAsistencias(),
                    est.getPartidosJugados()
            });
        }
    }
}
