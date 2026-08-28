package unam.fc.concurrent.practica1;
/*	Programa 13: Determinante de una matriz 3x3 usando 2 hilos
 * 	Un hilo calcula las 3 diagonales principales (se suman),
 * 	el otro calcula las 3 diagonales secundarias (se restan).
*/
public class DeterminanteDosHilos extends Thread {
    static int determinante;
    static int n_prueba = 3;
    static int matriz_prueba[][] = { { 1, 2, 2 }, { 1, 0, -2 }, { 3, -1, 1 }};
    int[][] matriz;
    int tipo; // 1 = diagonales que se suman, 2 = diagonales que se restan
    int partial;

    public DeterminanteDosHilos(int[][] matriz, int tipo) {
        this.matriz = matriz;
        this.tipo = tipo;
    }

    public static int determinanteMatriz3x3(int matriz[][], int n_prueba) {
        DeterminanteDosHilos thr1 = new DeterminanteDosHilos(matriz, 1);
        DeterminanteDosHilos thr2 = new DeterminanteDosHilos(matriz, 2);

        thr1.start();
        thr2.start();

        try {
            thr1.join();
            thr2.join();
        } catch (InterruptedException e) {}

        int result = thr1.partial - thr2.partial;

        return result;
    }

    public void run() {
        if (tipo == 1) {
            int p1 = matriz[0][0] * matriz[1][1] * matriz[2][2];
            int p2 = matriz[1][0] * matriz[2][1] * matriz[0][2];
            int p3 = matriz[2][0] * matriz[0][1] * matriz[1][2];
            this.partial = p1 + p2 + p3;
        } else {
            int p4 = matriz[2][0] * matriz[1][1] * matriz[0][2];
            int p5 = matriz[1][0] * matriz[0][1] * matriz[2][2];
            int p6 = matriz[0][0] * matriz[2][1] * matriz[1][2];
            this.partial = p4 + p5 + p6;
        }
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        determinante = determinanteMatriz3x3(matriz_prueba, n_prueba);
        long endTime = System.nanoTime();
        System.out.println("Program took " +
                (endTime - startTime) + "ns, result: " + determinante);
    }
}