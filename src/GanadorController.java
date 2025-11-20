
import java.util.List;
import model.Carton;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 8040d
 */
public class GanadorController {
    
    //VERIFICAR GANADOR SEGÚN EL MODO DE JUEGO
    /**
     * modos posibles: 
     * NORMAL = filas, columnas, diagonales o 4 esquinas
     * ESQUINAS = solo cuatro esquinas 
     * LLENO = cartel lleno
     */
    public boolean verificarGanador(Carton carton, List<Integer> numerosCantados, String modo) {

        // Marcar números cantados
        for (int n : numerosCantados) {
            carton.marcar(n);
        }

        switch (modo.toUpperCase()) {
            case "NORMAL":
                return tieneLinea(carton) || tiene4Esquinas(carton);

            case "ESQUINAS":
                return tiene4Esquinas(carton);

            case "LLENO":
                return cartonLleno(carton);

            default:
                return false;
        }
    }

    //VERIFICACIONES DE LÍNEAS (H, V, DIAGONAL)
    public boolean tieneLinea(Carton carton) {
        boolean[][] m = carton.getMarcados();

        // Horizontales
        for (int f = 0; f < 5; f++) {
            boolean completa = true;
            for (int c = 0; c < 5; c++) {
                if (!m[f][c]) {
                    completa = false;
                }
            }
            if (completa) {
                return true;
            }
        }

        // Verticales
        for (int c = 0; c < 5; c++) {
            boolean completa = true;
            for (int f = 0; f < 5; f++) {
                if (!m[f][c]) {
                    completa = false;
                }
            }
            if (completa) {
                return true;
            }
        }

        // Diagonal principal
        boolean diag1 = true;
        for (int i = 0; i < 5; i++) {
            if (!m[i][i]) {
                diag1 = false;
            }
        }

        // Diagonal secundaria
        boolean diag2 = true;
        for (int i = 0; i < 5; i++) {
            if (!m[i][4 - i]) {
                diag2 = false;
            }
        }

        return diag1 || diag2;
    }

    //VERIFICACIÓN SOLO CUATRO ESQUINAS
    public boolean tiene4Esquinas(Carton carton) {
        boolean[][] m = carton.getMarcados();

        return m[0][0] && m[0][4] && m[4][0] && m[4][4];
    }

    //VERIFICAR CARTÓN LLENO
    public boolean cartonLleno(Carton carton) {
        boolean[][] m = carton.getMarcados();

        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (!m[f][c]) {
                    return false;
                }
            }
        }
        return true;
    }
}
