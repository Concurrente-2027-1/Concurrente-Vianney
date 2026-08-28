package unam.fc.concurrent.practica1;
/*	Programa: Determinante de una matriz 3x3 de forma secuencial
 * 	Misma lógica que DeterminanteConcurrente, pero sin usar hilos,
 * 	calculando todo directamente en el hilo principal.
*/
public class DeterminanteSecuencial {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        int p1 = matriz[0][0] * matriz[1][1] * matriz[2][2];
        int p2 = matriz[1][0] * matriz[2][1] * matriz[0][2];
        int p3 = matriz[2][0] * matriz[0][1] * matriz[1][2];
        int p4 = matriz[2][0] * matriz[1][1] * matriz[0][2];
        int p5 = matriz[1][0] * matriz[0][1] * matriz[2][2];
        int p6 = matriz[0][0] * matriz[2][1] * matriz[1][2];

        int result = p1 + p2 + p3 - p4 - p5 - p6;

        return result;
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}