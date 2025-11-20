/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ccore
 */
public class Tombola {

    private int[][] numeros;
    private boolean[][] marcados;
    private boolean modoAutomatico;
    private Integer ultimoNumero;

    public Tombola(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
        inicializar();
    }

    //Inicializar matrices
    private void inicializar() {
        numeros = new int[5][15];
        marcados = new boolean[5][15];

        int inicio = 1;

        for (int fila = 0; fila < 5; fila++) {

            // Definir el rango correspondiente
            int min = 1 + (fila * 15);
            int max = 15 + (fila * 15);

            int numero = min;

            // Llenar la fila con números ordenados
            for (int col = 0; col < 15; col++) {
                numeros[fila][col] = numero;
                numero++;
            }
        }

        // Matriz de marcados en false
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 15; c++) {
                marcados[f][c] = false;
            }
        }

        ultimoNumero = null;
    }

    
    public int[][] getNumeros() {
        return numeros;
    }

    public boolean[][] getMarcados() {
        return marcados;
    }

    public boolean isModoAutomatico() {
        return modoAutomatico;
    }

    public Integer getUltimoNumero() {
        return ultimoNumero;
    }


    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public void setMarcados(boolean[][] marcados) {
        if (marcados.length == 5 && marcados[0].length == 15) {
            this.marcados = marcados;
        } else {
            throw new IllegalArgumentException("La matriz de marcados debe ser 5x15.");
        }
    }

    //Reiniciar tómbola
    public void reiniciar() {
        inicializar();
    }
}
