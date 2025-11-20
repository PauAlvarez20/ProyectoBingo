package Controller;


import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import model.Tombola;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 8040d
 */
public class TombolaController {
    
    // LLENAR MODELO + COLORES
    public void llenarTablaConMarcados(Tombola tombola, JTable tabla) {

        int[][] numeros = tombola.getNumeros();
        boolean[][] marcados = tombola.getMarcados();

        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); // Limpiar
        modelo.setColumnCount(15);

        // Llenar matriz
        for (int fila = 0; fila < 5; fila++) {
            Object[] filaData = new Object[15];
            for (int col = 0; col < 15; col++) {
                filaData[col] = numeros[fila][col];
            }
            modelo.addRow(filaData);
        }

        // Asignar renderer para colorear marcados
        tabla.setDefaultRenderer(Object.class, new TableCellRenderer() {

            private final TableCellRenderer baseRenderer = tabla.getDefaultRenderer(Object.class);

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {

                Component cell = baseRenderer.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // Si está marcado → colorear
                if (marcados[row][column]) {
                    cell.setBackground(Color.YELLOW);
                } else {
                    cell.setBackground(Color.WHITE);
                }

                return cell;
            }
        });

        tabla.repaint();
    }

    //LLENAR SOLO NÚMEROS
    public void llenarSoloNumeros(Tombola tombola, DefaultTableModel modelo) {

        int[][] numeros = tombola.getNumeros();

        modelo.setRowCount(0);
        modelo.setColumnCount(15);

        for (int fila = 0; fila < 5; fila++) {
            Object[] filaData = new Object[15];
            for (int col = 0; col < 15; col++) {
                filaData[col] = numeros[fila][col];
            }
            modelo.addRow(filaData);
        }
    }

    public boolean marcarNumero(int numero, Tombola tombola) {

        int[][] numeros = tombola.getNumeros();
        boolean[][] marcados = tombola.getMarcados();

        for (int fila = 0; fila < numeros.length; fila++) {
            for (int col = 0; col < numeros[fila].length; col++) {

                if (numeros[fila][col] == numero) {

                    marcados[fila][col] = true;
                    tombola.setMarcados(marcados);

                    // guardar el último número marcado si quieres
                    tombola.setUltimoNumero(numero);

                    return true;
                }
            }
        }

        return false;
    }
}
