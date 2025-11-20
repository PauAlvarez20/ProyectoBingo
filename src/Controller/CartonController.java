package Controller;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Carton;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Pau
 */
public class CartonController {

    private final Random random = new Random();

    //GENERACIÓN DE CARTÓN AUTOMÁTICO (24 números)
    public Carton generarCarton(Carton carton) {
        int[][] numeros = carton.getNumeros();
        HashSet<Integer> usados = new HashSet<>();

        // Rangos por columna
        int[][] rangos = {
            {1, 15},
            {16, 30},
            {31, 45},
            {46, 60},
            {61, 75}
        };

        for (int col = 0; col < 5; col++) {
            int min = rangos[col][0];
            int max = rangos[col][1];

            for (int fila = 0; fila < 5; fila++) {

                // Casilla libre
                if (fila == 2 && col == 2) {
                    numeros[fila][col] = 0; // FREE
                    carton.getMarcados()[fila][col] = true;
                    continue;
                }

                int num;
                do {
                    num = random.nextInt(max - min + 1) + min;
                } while (usados.contains(num));

                usados.add(num);
                numeros[fila][col] = num;
            }
        }

        return carton;
    }

    public Carton ordenarYAsignarMatriz(Carton carton, int[][] matrizDesordenada) {

        int[][] ordenada = new int[5][5];

        int[][] rangos = {
            {1, 15},
            {16, 30},
            {31, 45},
            {46, 60},
            {61, 75}
        };

        List<Integer> columna = new ArrayList<>();

        for (int col = 0; col < 5; col++) {
            columna.clear();

            int min = rangos[col][0];
            int max = rangos[col][1];

            for (int fila = 0; fila < 5; fila++) {
                int valor = matrizDesordenada[fila][col];
                if (valor >= min && valor <= max) {
                    columna.add(valor);
                }
            }

            Collections.sort(columna);

            for (int fila = 0; fila < 5; fila++) {

                if (fila == 2 && col == 2) {
                    ordenada[fila][col] = 0;
                    carton.getMarcados()[fila][col] = true;
                    continue;
                }

                if (fila < columna.size()) {
                    ordenada[fila][col] = columna.get(fila);
                }
            }
        }

        // Asignar al cartón final
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                carton.setNumero(f, c, ordenada[f][c]);
            }
        }

        return carton;
    }

    public boolean verificarCarton(Carton carton) {

        int[][] numeros = carton.getNumeros();

        // Rangos válidos por columna
        int[][] rangos = {
            {1, 15},
            {16, 30},
            {31, 45},
            {46, 60},
            {61, 75}
        };

        for (int col = 0; col < 5; col++) {
            int min = rangos[col][0];
            int max = rangos[col][1];

            for (int fila = 0; fila < 5; fila++) {

                // Saltar casilla central (FREE)
                if (fila == 2 && col == 2) {
                    continue;
                }

                int valor = numeros[fila][col];

                // Cero NO es válido (solo FREE)
                if (valor == 0) {
                    return false;
                }

                // Validar rango
                if (valor < min || valor > max) {
                    return false;
                }
            }
        }

        return true; // Todo correcto
    }

    public void ponerCeros(Carton carton) {

        int[][] nums = carton.getNumeros();
        boolean[][] marc = carton.getMarcados();

        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {

                if (f == 2 && c == 2) {
                    nums[f][c] = 0;
                    marc[f][c] = true;
                } else {
                    nums[f][c] = 0;
                    marc[f][c] = false;
                }
            }
        }
    }

    public Carton marcarNumeroEnCarton(Carton carton, int numero) {
        int[][] nums = carton.getNumeros();
        boolean[][] marks = carton.getMarcados();

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 5; col++) {

                if (fila == 2 && col == 2) {
                    continue; // FREE
                }
                if (nums[fila][col] == numero) {
                    marks[fila][col] = true;
                    return carton;
                }
            }
        }

        return carton;
    }

    // ELIMINAR CARTÓN (cuando se cierra interfaz)
    public String eliminarCarton(List<Carton> lista, Carton carton) {

        if (lista.remove(carton)) {
            return "Cartón eliminado correctamente.";
        } else {
            return "No se pudo eliminar el cartón: no existe en la lista.";
        }
    }
}
