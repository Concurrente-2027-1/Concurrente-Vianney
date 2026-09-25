package unam.fc.concurrent.practica3;
// Ejercicio 2: Bakery de Lamport
// para 4 hilos. Cada hilo con id i:
//  1. levanta su bandera flag[i]
//  2. saca un numero (label[i]) mayor a todos los numeros actuales
//  3. espera su turno: mientras algun otro hilo k tenga bandera levantada y
//     su (label[k], k) sea "menor" que (label[i], i), se espera.
// El orden lexicografico (label, id) rompe empates cuando dos hilos sacan el
// mismo numero, garantizando orden FCFS aproximado y sin dos hilos con la
// misma prioridad exacta.

public class Bakery4 {
	private static final int N = 4;
	private volatile boolean[] flag = new boolean[N];
	private volatile int[] label = new int[N];

	// Avisa que quiere entrar, saca numero, y espera a que nadie con mejor
	// prioridad (numero mas chico, o mismo numero con id mas chico) este activo.
	public void lock(int i) {
		flag[i] = true;
		label[i] = maxLabel() + 1;
		for (int k = 0; k < N; k++) {
			if (k == i) continue;
			while (flag[k] && tienePrioridad(k, i)) {
				// espera activa
			}
		}
	}

	// Avisa que ya salio de la seccion critica.
	public void unlock(int i) {
		flag[i] = false;
	}

	// El numero mas grande que se ha repartido hasta ahora, para sacar uno mayor.
	private int maxLabel() {
		int max = 0;
		for (int k = 0; k < N; k++) {
			if (label[k] > max) max = label[k];
		}
		return max;
	}

	// true si el hilo k tiene prioridad sobre el hilo i: numero mas chico, o
	// mismo numero pero id mas chico (para desempatar).
	private boolean tienePrioridad(int k, int i) {
		if (label[k] != label[i]) {
			return label[k] < label[i];
		}
		return k < i;
	}
}