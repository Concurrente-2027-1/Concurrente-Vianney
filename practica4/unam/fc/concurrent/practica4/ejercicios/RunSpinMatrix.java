package unam.fc.concurrent.practica4.ejercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import unam.fc.concurrent.practica4.*;

/**
 * Ejercicio 2 de la Practica 4.
 * Igual que RunSpin, pero cada tarea asigna valores a una matriz compartida
 * de 10 x 10 y la imprime, todo dentro de la seccion critica.
 * Al final se imprime el tiempo total en milisegundos.
 */
public class RunSpinMatrix {

	/** Tamano de la matriz (N x N). */
	static final int N = 10;

	/** Matriz compartida por todos los hilos. */
	static int[][] matrix = new int[N][N];

	/**
	 * Llena la matriz con valores de la tarea id.
	 * @param id numero de la tarea
	 */
	public static void asignarMatriz(int id) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				matrix[i][j] = id * 1000 + i * N + j;
			}
		}
	}

	/**
	 * Imprime la matriz con el numero de tarea y el hilo que la ejecuta.
	 * @param id numero de la tarea
	 */
	public static void imprimirMatriz(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("Tarea ").append(id).append(" (").append(Thread.currentThread().getName()).append(")\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sb.append(String.format("%7d", matrix[i][j]));
			}
			sb.append('\n');
		}
		System.out.print(sb);
	}

	/**
	 * Tarea de cada hilo: toma el candado, asigna e imprime la matriz
	 * y libera el candado.
	 * @param lock candado que protege la seccion critica
	 * @param id numero de la tarea
	 * @return el numero de la tarea
	 */
	private static int task(Lock lock, int id) {
		lock.lock();
		try {
			asignarMatriz(id);
			imprimirMatriz(id);
		} finally {
			lock.unlock();
		}
		return id;
	}

	/**
	 * Manda las tareas al pool de hilos y mide cuanto tardan.
	 */
	public static void main(String[] args) {
		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		int numberTasks = 100;   // numero de tareas
		int numberThreads = 4;   // 4  o  max-1
		ExecutorService executor = Executors.newFixedThreadPool(numberThreads);

		// Descomentar solo el candado que se quiere medir
		Lock lock = new TASLock();
//		Lock lock = new TTASLock();
//		Lock lock = new BackoffLock();
//		Lock lock = new MCSLock();
//		Lock lock = new ALock(numberThreads);
//		Lock lock = new ReentrantLock();
//		Lock lock = new CLHLock();

		long startTime = System.nanoTime(); // Start time
		for (int i = 0; i < numberTasks; i++) {
			final int id = i;
			futures.add(executor.submit(() -> task(lock, id)));
		}
		executor.shutdown();

		for (int i = 0; i < futures.size(); i++) {
			while (!futures.get(i).isDone()) {}; // Comprobar que todas las tareas terminen
		}
		long endTime = System.nanoTime(); // Finish time

		System.out.println("Program took " + (endTime - startTime) * 0.000001 + "ms, "
				+ numberTasks + " tareas con " + numberThreads + " hilos, candado: "
				+ lock.getClass().getSimpleName()); // En milisegundos
	}
}