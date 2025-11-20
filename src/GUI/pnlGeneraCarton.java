/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import Controller.CartonController;
import Controller.TombolaController;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Carton;
import model.Tombola;
import javax.swing.JOptionPane;

/**
 *
 * @author Cjared
 */
public class pnlGeneraCarton extends javax.swing.JPanel {

    private Tombola tombola;
    private Carton c1, c2, c3;
    private Integer numeroSeleccionado = null;
    private JTable tablaSeleccionada = null;

    /**
     * Creates new form pnlGeneraCarton
     */
    public pnlGeneraCarton() {
        initComponents();
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public void setTombola(Tombola t) {
        this.tombola = t;

        if (t != null) {
            inicializarTablas();
        }
    }

    public void setCartones(Carton a, Carton b, Carton c) {
        this.c1 = a;
        this.c2 = b;
        this.c3 = c;
    }

    private void inicializarTablas() {

        // Hacer todas las tablas NO EDITABLES
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

        tblCrearCarton.setModel(new DefaultTableModel(5, 15) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        });

        // 1) Todas las tablas de cartones llenas de ceros
        llenarTablaConCeros(tblMiCarton1);
        llenarTablaConCeros(tblMiCarton2);
        llenarTablaConCeros(tblMiCarton3);

        // 2) Llenar tabla creadora con rangos
        llenarTablaCrearCarton();

        tblMiCarton1.getTableHeader().setReorderingAllowed(false);
        tblMiCarton2.getTableHeader().setReorderingAllowed(false);
        tblMiCarton3.getTableHeader().setReorderingAllowed(false);
        tblCrearCarton.getTableHeader().setReorderingAllowed(false);

        // Eventos de selección
        configurarEventos();
    }

    private void llenarTablaConCeros(JTable tabla) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                m.setValueAt(0, f, c);
            }
        }
    }

    private void llenarTablaCrearCarton() {

        DefaultTableModel m = (DefaultTableModel) tblCrearCarton.getModel();

        int numero = 1;

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 15; col++) {
                m.setValueAt(numero, fila, col);
                numero++;
            }
        }
    }

    private void configurarEventos() {

        // seleccionar número
        tblCrearCarton.getSelectionModel().addListSelectionListener(e -> {
            int fila = tblCrearCarton.getSelectedRow();
            int col = tblCrearCarton.getSelectedColumn();
            if (fila >= 0 && col >= 0) {
                numeroSeleccionado = Integer.parseInt(tblCrearCarton
                        .getValueAt(fila, col).toString());
            }
        });

        // clic en cualquiera de los tres cartones
        configurarClickEnCarton(tblMiCarton1);
        configurarClickEnCarton(tblMiCarton2);
        configurarClickEnCarton(tblMiCarton3);
    }

    private void configurarClickEnCarton(JTable tabla) {

        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                tablaSeleccionada = tabla;

                int fila = tabla.getSelectedRow();
                int col = tabla.getSelectedColumn();

                if (fila < 0 || col < 0) {
                    return;
                }

                if (numeroSeleccionado == null) {
                    return;
                }

                // Determinar cuál cartón corresponde
                Carton cartonActual;
                if (tabla == tblMiCarton1) {
                    cartonActual = c1;
                } else if (tabla == tblMiCarton2) {
                    cartonActual = c2;
                } else {
                    cartonActual = c3;
                }

                int[][] nums = cartonActual.getNumeros();

                // 1. Validar rango permitido
                int[][] rangos = {
                    {1, 15}, // Columna 0
                    {16, 30}, // Columna 1
                    {31, 45}, // Columna 2
                    {46, 60}, // Columna 3
                    {61, 75} // Columna 4
                };

                int min = rangos[col][0];
                int max = rangos[col][1];

                if (numeroSeleccionado < min || numeroSeleccionado > max) {
                    JOptionPane.showMessageDialog(null,
                            "Número fuera del rango permitido para esta columna.");
                    return;
                }

                // 2. Validar que no esté repetido en el cartón
                for (int f = 0; f < 5; f++) {
                    for (int c = 0; c < 5; c++) {
                        if (nums[f][c] == numeroSeleccionado) {
                            JOptionPane.showMessageDialog(null,
                                    "Este número ya está en este cartón.");
                            return;
                        }
                    }
                }

                // 3. Insertar en la tabla visual
                tabla.setValueAt(numeroSeleccionado, fila, col);

                // 4. Insertar en la matriz interna del cartón
                nums[fila][col] = numeroSeleccionado;
            }
        });
    }

    private void generarAutomatico(JTable tabla, Carton carton) {

        int[][] rangos = {
            {1, 15},
            {16, 30},
            {31, 45},
            {46, 60},
            {61, 75}
        };

        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        int[][] nums = carton.getNumeros();

        java.util.Random rnd = new java.util.Random();

        for (int col = 0; col < 5; col++) {

            int min = rangos[col][0];
            int max = rangos[col][1];

            java.util.List<Integer> disponibles = new java.util.ArrayList<>();

            for (int n = min; n <= max; n++) {
                disponibles.add(n);
            }

            java.util.Collections.shuffle(disponibles);

            for (int fila = 0; fila < 5; fila++) {
                int numero = disponibles.remove(0); // no repetido
                m.setValueAt(numero, fila, col);
                nums[fila][col] = numero;
            }
        }
    }

    private void imprimirTabla(String titulo, JTable tabla) {
        System.out.println("----- " + titulo + " -----");

        for (int f = 0; f < tabla.getRowCount(); f++) {
            for (int c = 0; c < tabla.getColumnCount(); c++) {
                System.out.print(tabla.getValueAt(f, c) + "\t");
            }
            System.out.println();
        }

        System.out.println("-----------------------------\n");
    }

    private boolean cartonCompleto(Carton carton) {
        int[][] nums = carton.getNumeros();

        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (nums[f][c] == 0) {  // Celda vacía
                    return false;
                }
            }
        }

        return true; // Todo lleno
    }

    private java.util.List<String> obtenerCartonesValidos() {
        java.util.List<String> list = new java.util.ArrayList<>();

        if (cartonCompleto(c1)) {
            list.add("Cartón 1");
        }
        if (cartonCompleto(c2)) {
            list.add("Cartón 2");
        }
        if (cartonCompleto(c3)) {
            list.add("Cartón 3");
        }

        return list;
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
        btnAutomatico = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        lblMiCarton = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCrearCarton = new javax.swing.JTable();
        btnVolver = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblMiCarton2 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblMiCarton3 = new javax.swing.JTable();

        jScrollPane1.setViewportView(tblMiCarton1);

        btnAutomatico.setText("Generar");
        btnAutomatico.addActionListener(this::btnAutomaticoActionPerformed);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        lblMiCarton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMiCarton.setText("Mi Carton");
        lblMiCarton.setToolTipText("");

        jScrollPane2.setViewportView(tblCrearCarton);

        btnVolver.setText("Volver");

        jScrollPane3.setViewportView(tblMiCarton2);

        jScrollPane4.setViewportView(tblMiCarton3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblMiCarton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(btnAutomatico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnVolver))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnAutomatico)
                    .addComponent(btnVolver))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMiCarton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAutomaticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutomaticoActionPerformed
        if (tablaSeleccionada == null) {
            JOptionPane.showMessageDialog(this,
                    "Debe tocar un cartón antes de generar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Carton cartonActual;

        if (tablaSeleccionada == tblMiCarton1) {
            cartonActual = c1;
        } else if (tablaSeleccionada == tblMiCarton2) {
            cartonActual = c2;
        } else if (tablaSeleccionada == tblMiCarton3) {
            cartonActual = c3;
        } else {
            return;
        }

        generarAutomatico(tablaSeleccionada, cartonActual);

        imprimirTabla("GENERADO", tablaSeleccionada);
    }//GEN-LAST:event_btnAutomaticoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        java.util.List<String> validos = obtenerCartonesValidos();

        if (validos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ningún cartón está completamente lleno.\n"
                    + "Debe completar al menos uno para continuar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Al menos 1 cartón está correcto
        String mensaje = "Los siguientes cartones están completos y serán usados en el juego:\n\n";

        for (String c : validos) {
            mensaje += "✔ " + c + "\n";
        }

        if (validos.size() < 3) {
            mensaje += "\nLos cartones incompletos NO serán utilizados.";
        }

        JOptionPane.showMessageDialog(this, mensaje, "Cartones Validados", JOptionPane.INFORMATION_MESSAGE);

        // Si necesitás aquí hacer algo con ellos, por ejemplo:
        // tombola.setCartonesValidos(validos);
    }//GEN-LAST:event_btnGuardarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAutomatico;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblMiCarton;
    private javax.swing.JTable tblCrearCarton;
    private javax.swing.JTable tblMiCarton1;
    private javax.swing.JTable tblMiCarton2;
    private javax.swing.JTable tblMiCarton3;
    // End of variables declaration//GEN-END:variables
}
