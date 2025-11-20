/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import model.Carton;
import model.Tombola;

/**
 *
 * @author Cjared
 */
public class frmPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmPrincipal.class.getName());
    pnlPrincipal panelPrincipal = new pnlPrincipal();
    pnlModoJuego panelModoJuego = new pnlModoJuego();
    pnlGeneraCarton panelGeneraCarton = new pnlGeneraCarton();
    pnlTipoJuego panelTipoJuego = new pnlTipoJuego();
    pnlBingo panelBingo = new pnlBingo();

    private Tombola tombola;
    private Carton carton1;
    private Carton carton2;
    private Carton carton3;

    /**
     * Creates new form frmPrincipal
     */
    public frmPrincipal() {
        initComponents();

        tombola = new Tombola(false);
        carton1 = new Carton("C1");
        carton2 = new Carton("C2");
        carton3 = new Carton("C3");

        // ENVIAR OBJETOS A LOS PANELS
        inyectarDependencias();

        mostrarPanel(panelPrincipal);

        eventosNavegacion();
    }

    private void mostrarPanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }

    private void eventosNavegacion() {

        // pnlPrincipal - pnlModoJuego
        panelPrincipal.getBtnJugar().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelModoJuego);
        });

        // pnlModoJuego - pnlGeneraCarton
        panelModoJuego.getPnlCartonNormal().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inyectarDependencias();
                mostrarPanel(panelGeneraCarton);
            }
        });

        panelModoJuego.getPnlCartonLleno().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inyectarDependencias();
                mostrarPanel(panelGeneraCarton);
            }
        });

        panelModoJuego.getPnlCuatroEsquinas().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inyectarDependencias();
                mostrarPanel(panelGeneraCarton);
            }
        });

        // pnlGeneraCarton - pnlTipoJuego
        panelGeneraCarton.getBtnGuardar().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelTipoJuego);
        });

        // pnlTipoJuego - pnlBingo
        panelTipoJuego.getPnlAutomatico().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inyectarDependencias();
                mostrarPanel(panelBingo);
                panelBingo.cargarDatos();
                panelBingo.configurarEventosCartones();
            }
        });

        panelTipoJuego.getPnlManual().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                inyectarDependencias();
                imprimirEstadoCartones();
                panelBingo.cargarDatos();
                panelBingo.configurarEventosCartones();
                mostrarPanel(panelBingo);
            }
        });

        // Todos los volver - pnlModoJuego
        panelModoJuego.getBtnVolver().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelPrincipal);
        });

        panelGeneraCarton.getBtnVolver().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelModoJuego);
        });

        panelTipoJuego.getBtnVolver().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelModoJuego);
        });

        // pnlBingo botones de volver
        panelBingo.getBtnInicio().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelPrincipal);
        });

        panelBingo.getBtnCambiarJuego().addActionListener(e -> {
            inyectarDependencias();
            mostrarPanel(panelModoJuego);
        });
    }

    private void inyectarDependencias() {

        // ---- Paneles que usan SOLO Tómbola ----
        panelModoJuego.setTombola(tombola);
        panelTipoJuego.setTombola(tombola);
        panelBingo.setTombola(tombola);
        panelGeneraCarton.setTombola(tombola);

        // ---- Paneles que usan LOS TRES CARTONES ----
        panelGeneraCarton.setCartones(carton1, carton2, carton3);
        panelBingo.setCartones(carton1, carton2, carton3);

        //imprimirEstadoTombola();
    }

    private void imprimirEstadoCartones() {

        System.out.println("\n================= ESTADO DE LOS CARTONES =================");

        imprimirCarton("CARTÓN 1", carton1);
        imprimirCarton("CARTÓN 2", carton2);
        imprimirCarton("CARTÓN 3", carton3);

        System.out.println("==========================================================\n");
    }

    private void imprimirEstadoTombola() {

        System.out.println("----- ESTADO ACTUAL DE LA TÓMBOLA -----");

        System.out.println("Modo automático: " + tombola.isModoAutomatico());
        System.out.println("Último número: " + tombola.getUltimoNumero());
        System.out.println("TipoModo: " + tombola.getTipoModo());

        System.out.println("\n--- MATRIZ DE NÚMEROS ---");
        int[][] nums = tombola.getNumeros();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(nums[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("\n--- MATRIZ DE MARCADOS ---");
        boolean[][] marc = tombola.getMarcados();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print((marc[i][j] ? "1" : "0") + "\t");
            }
            System.out.println();
        }

        System.out.println("----------------------------------------");
    }

    private void imprimirCarton(String titulo, Carton c) {

        System.out.println("\n----- " + titulo + " (" + c.getId() + ") -----");

        int[][] nums = c.getNumeros();
        boolean[][] marc = c.getMarcados();

        System.out.println("NÚMEROS:");
        for (int f = 0; f < 5; f++) {
            for (int col = 0; col < 5; col++) {
                System.out.printf("%3d\t", nums[f][col]);
            }
            System.out.println();
        }

        System.out.println("MARCADOS:");
        for (int f = 0; f < 5; f++) {
            for (int col = 0; col < 5; col++) {
                System.out.print(marc[f][col] ? "1\t" : "0\t");
            }
            System.out.println();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 810, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 474, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
