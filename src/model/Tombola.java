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
    private Integer tipoModo;

    public Tombola(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
        this.tipoModo = 0;
        this.ultimoNumero = -1;
        this.modoAutomatico = false;
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

    public Integer getTipoModo() {
        return tipoModo;
    }

    
    public void setModoAutomatico(boolean modoAutomatico) {
        this.modoAutomatico = modoAutomatico;
    }

    public void setUltimoNumero(Integer ultimoNumero) {
        this.ultimoNumero = ultimoNumero;
    }

    public void setTipoModo(int tipoModo) {
        if (tipoModo < 0 || tipoModo > 3) {
            throw new IllegalArgumentException("El valor tipoModo solo puede ser 0, 1, 2 o 3.");
        }
        this.tipoModo = tipoModo;
    }

    public void setMarcados(boolean[][] marcados) {
        if (marcados.length == 5 && marcados[0].length == 15) {
            this.marcados = marcados;
        } else {
            throw new IllegalArgumentException("La matriz de marcados debe ser 5x15.");
        }
    }

    public void setNumeros(int[][] numeros) {
        if (numeros == null || numeros.length != 5 || numeros[0].length != 15) {
            throw new IllegalArgumentException("La matriz de números debe ser de 5 filas por 15 columnas.");
        }
        this.numeros = numeros;
    }

    //Reiniciar tombola
    public void reiniciar() {
        inicializar();
    }
}
