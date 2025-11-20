/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Controller.CartonController;
import Controller.GanadorController;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Carton;
import model.Tombola;

/**
 *
 * @author Cjared
 */
public class pnlBingo extends javax.swing.JPanel {

    private Tombola tombola;
    private Carton c1, c2, c3;
    private javax.swing.Timer timer;
    private java.util.Set<Integer> numerosGenerados = new java.util.HashSet<>();
    private final java.util.Random rnd = new java.util.Random();
    private GanadorController ganadorController = new GanadorController();
    private CartonController cartonController = new CartonController();

    /**
     * Creates new form pnlBingo
     */
    public pnlBingo() {
        initComponents();
        inicializarEstructurasTablas();
    }

    public JButton getBtnInicio() {
        return btnInicio;
    }

    public JButton getBtnCambiarJuego() {
        return btnCambiarJuego;
    }

    public void setTombola(Tombola t) {
        this.tombola = t;
    }

    public void setCartones(Carton a, Carton b, Carton c) {
        this.c1 = a;
        this.c2 = b;
        this.c3 = c;
    }

    public void cargarDatos() {

        // === TÓMBOLA ===
        int[][] numsT = tombola.getNumeros();
        DefaultTableModel mT = (DefaultTableModel) tblNumeros.getModel();

        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 15; c++) {
                mT.setValueAt(numsT[f][c], f, c);
            }
        }

        // === CARTONES ===
        llenarCartonEnTabla(tblMiCarton1, c1);
        llenarCartonEnTabla(tblMiCarton2, c2);
        llenarCartonEnTabla(tblMiCarton3, c3);

        // Protegerlas contra edición
        bloquearEdicion(tblNumeros);
        bloquearEdicion(tblMiCarton1);
        bloquearEdicion(tblMiCarton2);
        bloquearEdicion(tblMiCarton3);

        aplicarRendererCarton(tblMiCarton1, c1);
        aplicarRendererCarton(tblMiCarton2, c2);
        aplicarRendererCarton(tblMiCarton3, c3);
        aplicarRendererTombola();
    }

    private void llenarCartonEnTabla(JTable tabla, Carton c) {
        int[][] nums = c.getNumeros();
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();

        for (int f = 0; f < 5; f++) {
            for (int col = 0; col < 5; col++) {
                m.setValueAt(nums[f][col], f, col);
            }
        }
    }

    private void bloquearEdicion(JTable tabla) {
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellEditor(null);
        }
    }

    public void configurarEventosCartones() {

        configurarEventoTabla(tblMiCarton1, c1);
        configurarEventoTabla(tblMiCarton2, c2);
        configurarEventoTabla(tblMiCarton3, c3);
    }

    private void configurarEventoTabla(JTable tabla, Carton carton) {

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int fila = tabla.getSelectedRow();
                int col = tabla.getSelectedColumn();

                if (fila < 0 || col < 0) {
                    return;
                }

                boolean[][] marcados = carton.getMarcados();
                boolean actual = marcados[fila][col];

                // Alternar valor
                marcados[fila][col] = !actual;
                carton.setMarcados(marcados);

                // Cambiar colores
                tabla.repaint();

                verificarYManejarGanador(carton, carton == c1 ? "Cartón 1" : carton == c2 ? "Cartón 2" : "Cartón 3");
            }
        });

        // renderer para colorear celda marcada
        aplicarRendererTombola();
    }

    private void aplicarRendererTombola() {

        tblNumeros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                boolean[][] marc = tombola.getMarcados();

                if (marc[row][column]) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }

    private void inicializarEstructurasTablas() {

        // ---- TÓMBOLA (5x15) ----
        DefaultTableModel mT = (DefaultTableModel) tblNumeros.getModel();

        if (mT.getRowCount() == 0 || mT.getColumnCount() == 0) {
            mT.setRowCount(5);
            mT.setColumnCount(15);
        }

        // ---- CARTONES (5x5) ----
        inicializarTabla5x5((DefaultTableModel) tblMiCarton1.getModel());
        inicializarTabla5x5((DefaultTableModel) tblMiCarton2.getModel());
        inicializarTabla5x5((DefaultTableModel) tblMiCarton3.getModel());

        tblMiCarton1.getTableHeader().setReorderingAllowed(false);
        tblMiCarton2.getTableHeader().setReorderingAllowed(false);
        tblMiCarton3.getTableHeader().setReorderingAllowed(false);

        // ---- TÓMBOLA (5x15) ----
        tblNumeros.setModel(new DefaultTableModel(5, 15) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        tblMiCarton1.setModel(new DefaultTableModel(5, 5) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        tblMiCarton2.setModel(new DefaultTableModel(5, 5) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });
        tblMiCarton3.setModel(new DefaultTableModel(5, 5) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

    }

    private void inicializarTabla5x5(DefaultTableModel m) {
        if (m.getRowCount() == 0 || m.getColumnCount() == 0) {
            m.setRowCount(5);
            m.setColumnCount(5);
        }
    }

    private void generarMovimiento() {

        // 1. Verificar si ya se generaron todos los números
        if (numerosGenerados.size() == 75) {
            timer.stop();
            System.out.println("Fin: Todos los números fueron generados");
            return;
        }

        // 2. Generar número sin repetición
        int numero;
        do {
            numero = rnd.nextInt(75) + 1; // 1..75
        } while (numerosGenerados.contains(numero));

        numerosGenerados.add(numero);
        

        System.out.println("Número generado: " + numero);

        // 3. Marcar en la tabla tblNumeros
        marcarNumeroEnTombola(numero);

        // 4. Si el modo automático está activo → marcar cartones
        System.out.print(tombola.isModoAutomatico());
        if (tombola.isModoAutomatico()) {
            marcarEnCarton(c1, tblMiCarton1, numero);
            marcarEnCarton(c2, tblMiCarton2, numero);
            marcarEnCarton(c3, tblMiCarton3, numero);
        }

        // 5. Refrescar tablas
        tblNumeros.repaint();
        tblMiCarton1.repaint();
        tblMiCarton2.repaint();
        tblMiCarton3.repaint();
    }

    private void marcarNumeroEnTombola(int numero) {

        int[][] nums = tombola.getNumeros();
        boolean[][] marc = tombola.getMarcados();
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 15; c++) {

                if (nums[f][c] == numero) {
                    marc[f][c] = true;          // marcar matriz
                    tombola.setMarcados(marc);
                    return;
                }
            }
        }
    }

    private void marcarEnCarton(Carton carton, JTable tabla, int numero) {

        int[][] nums = carton.getNumeros();
        boolean[][] marc = carton.getMarcados();
        System.out.print("HH 3");
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {

                if (nums[f][c] == numero) {
                    marc[f][c] = true;
                    carton.setMarcados(marc);
                    System.out.print("HH 2");
                    verificarYManejarGanador(carton, carton == c1 ? "Cartón 1" : carton == c2 ? "Cartón 2" : "Cartón 3");
                    return;
                }
            }
        }
    }

    private void aplicarRendererCarton(JTable tabla, Carton carton) {

        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int col) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                boolean[][] marc = carton.getMarcados();

                if (marc[row][col]) {
                    c.setBackground(java.awt.Color.GREEN);
                    c.setForeground(java.awt.Color.WHITE);
                } else {
                    c.setBackground(java.awt.Color.WHITE);
                    c.setForeground(java.awt.Color.BLACK);
                }

                return c;
            }
        });
    }

    // --- Comprueba ganador tras una marca y lo maneja ---
    private void verificarYManejarGanador(Carton carton, String nombreCarton) {
        // Construir lista de números cantados (orden no importa para la verificación)
        java.util.List<Integer> cantados = new java.util.ArrayList<>(numerosGenerados);

        // Mapear tipoModo a cadena
        String modo = "NORMAL";
        Integer tipo = tombola.getTipoModo();
        if (tipo != null) {
            switch (tipo) {
                case 2:
                    modo = "ESQUINAS";
                    break;
                case 3:
                    modo = "LLENO";
                    break;
                default:
                    modo = "NORMAL";
                    break;
            }
        }

        boolean esGanador = ganadorController.verificarGanador(carton, cantados, modo);

        if (esGanador) {
            // Mostrar mensaje de ganador (en EDT)
            JOptionPane.showMessageDialog(this,
                    "¡BINGO! Ganó: " + nombreCarton + "\nModo: " + modo,
                    "Ganador", JOptionPane.INFORMATION_MESSAGE);

            // Reiniciar juego
            reiniciarJuego();
        }
    }

// --- Reiniciar tómbola, cartones y timer ---
    private void reiniciarJuego() {
        detenerTimer();
        // Detener timer si existe
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // Limpiar números ya generados
        numerosGenerados.clear();

        // Reiniciar tómbola
        if (tombola != null) {
            tombola.reiniciar();
        }

        // Reiniciar cartones: poner todo en 0 y limpiar marcados
        if (c1 != null) {
            c1.limpiar();
        }
        if (c2 != null) {
            c2.limpiar();
        }
        if (c3 != null) {
            c3.limpiar();
        }

        // Refrescar interfaz: volver a cargar datos en tablas
        cargarDatos();
        // Reaplicar renderers (si corresponde)
        aplicarRendererCarton(tblMiCarton1, c1);
        aplicarRendererCarton(tblMiCarton2, c2);
        aplicarRendererCarton(tblMiCarton3, c3);
        // Repintar
        tblNumeros.repaint();
        tblMiCarton1.repaint();
        tblMiCarton2.repaint();
        tblMiCarton3.repaint();
    }

    private void reiniciarTableros() {
        detenerTimer();

        if (c1 != null) {
            c1.limpiar();
        }
        if (c2 != null) {
            c2.limpiar();
        }
        if (c3 != null) {
            c3.limpiar();
        }

        // Recargar datos en tablas
        cargarDatos();

        // Reaplicar renderers
        aplicarRendererCarton(tblMiCarton1, c1);
        aplicarRendererCarton(tblMiCarton2, c2);
        aplicarRendererCarton(tblMiCarton3, c3);

        tblMiCarton1.repaint();
        tblMiCarton2.repaint();
        tblMiCarton3.repaint();
    }

    private void reiniciarTombola() {
        detenerTimer();
        // Limpiar números generados
        numerosGenerados.clear();

        // Reiniciar matriz de marcados y números
        if (tombola != null) {
            tombola.reiniciar();
        }

        // Recargar datos
        cargarDatos();

        tblNumeros.repaint();
    }

    private void detenerTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            System.out.println("Timer detenido.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblMiCarton1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblNumeros = new javax.swing.JTable();
        btnReiniciar = new javax.swing.JButton();
        btnReiniciarTablero = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMiCarton2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMiCarton3 = new javax.swing.JTable();
        btnReiniciarTombola = new javax.swing.JButton();
        btnCambiarJuego = new javax.swing.JButton();
        btnInicio = new javax.swing.JButton();
        btnIniciar = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(810, 474));

        jScrollPane1.setViewportView(tblMiCarton1);

        jScrollPane2.setViewportView(tblNumeros);

        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(this::btnReiniciarActionPerformed);

        btnReiniciarTablero.setText("Reiniciar Tablero");
        btnReiniciarTablero.addActionListener(this::btnReiniciarTableroActionPerformed);

        jScrollPane3.setViewportView(tblMiCarton2);

        jScrollPane4.setViewportView(tblMiCarton3);

        btnReiniciarTombola.setText("Reiniciar Tombola");
        btnReiniciarTombola.addActionListener(this::btnReiniciarTombolaActionPerformed);

        btnCambiarJuego.setText("Cambiar Juego");

        btnInicio.setText("Inicio");

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(this::btnIniciarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnReiniciar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReiniciarTablero)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnReiniciarTombola)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIniciar)
                .addContainerGap(380, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnCambiarJuego)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnInicio)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnReiniciar)
                            .addComponent(btnReiniciarTablero)
                            .addComponent(btnReiniciarTombola)
                            .addComponent(btnIniciar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnInicio)
                            .addComponent(btnCambiarJuego))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        reiniciarJuego();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        if (tombola == null) {
            System.out.println("Tómbola no configurada");
            return;
        }

        if (timer != null && timer.isRunning()) {
            return; // Evita múltiples timers
        }

        timer = new javax.swing.Timer(2000, e -> generarMovimiento());
        timer.start();

        System.out.println("Juego iniciado...");
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnReiniciarTableroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarTableroActionPerformed
        reiniciarTableros();
    }//GEN-LAST:event_btnReiniciarTableroActionPerformed

    private void btnReiniciarTombolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarTombolaActionPerformed
        reiniciarTombola();
    }//GEN-LAST:event_btnReiniciarTombolaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarJuego;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnInicio;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JButton btnReiniciarTablero;
    private javax.swing.JButton btnReiniciarTombola;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblMiCarton1;
    private javax.swing.JTable tblMiCarton2;
    private javax.swing.JTable tblMiCarton3;
    private javax.swing.JTable tblNumeros;
    // End of variables declaration//GEN-END:variables
}
