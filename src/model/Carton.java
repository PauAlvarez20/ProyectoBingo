/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ccore
 */
public class Carton {

    private String id;
    private int[][] numeros;
    private boolean[][] marcados;
    private Set<Integer> usados;  // Para validar duplicados

    public Carton(String id) {
        this.id = id;
        this.numeros = new int[5][5];
        this.marcados = new boolean[5][5];
        this.usados = new HashSet<>();

        // Casilla central es libre
        this.marcados[2][2] = true;
    }

    public String getId() {
        return id;
    }

    public int[][] getNumeros() {
        return numeros;
    }

    public boolean[][] getMarcados() {
        return marcados;
    }

    public void setNumero(int fila, int col, int valor) {
        numeros[fila][col] = valor;
        usados.add(valor);
    }

    public int[][] setNumeros() {
        return numeros;
    }

    public void setNumeros(int[][] numeros) {
        this.numeros = numeros;
    }

    public void setMarcados(boolean[][] marcados) {
        this.marcados = marcados;
    }

    public void marcar(int n) {
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (numeros[f][c] == n) {
                    marcados[f][c] = true;
                }
            }
        }
    }

    public void desmarcar(int n) {
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (numeros[f][c] == n) {
                    marcados[f][c] = false;
                }
            }
        }
    }

    public void limpiar() {
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                marcados[f][c] = false;
            }
        }
        // Casilla libre
        marcados[2][2] = true;
    }
}
