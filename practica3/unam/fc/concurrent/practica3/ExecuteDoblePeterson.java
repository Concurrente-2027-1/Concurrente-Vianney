package unam.fc.concurrent.practica3;
// Ejercicio 1: prueba el candado DoblePeterson (Peterson para 4 hilos) con un
// ExecutorService de 4 hilos que ejecuta 400 tareas sobre un CounterNaive.
// Cada uno de los 4 hilos reales del pool recibe un id fijo (0-3) la primera
// vez que toma una tarea (via ThreadLocal); ese id es el que usa en todos los
// candados durante toda la ejecucion, evitando el problema de ids repetidos.

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class ExecuteDoblePeterson {

	// Contador compartido que reparte un id (0,1,2,3) por hilo real del pool,
	// asignado una sola vez, la primera vez que ese hilo pide un id.
	private static final AtomicInteger siguienteId = new AtomicInteger(0);
	private static final ThreadLocal<Integer> idHilo = ThreadLocal.withInitial(siguienteId::getAndIncrement);

	// Cada tarea toma el id fijo de su hilo, entra a la seccion critica
	// protegida por DoblePeterson, incrementa el contador compartido, y al
	// salir registra en porHilo cuantas tareas ha hecho ese id.
	private static void tarea(DoblePeterson lock, CounterNaive counter, AtomicIntegerArray porHilo) {
		int id = idHilo.get();
		try {
			lock.lock(id);
			counter.increment();
		} finally {
			lock.unlock(id);
		}
		porHilo.incrementAndGet(id);
	}

	public static void main(String[] args) throws InterruptedException {
		DoblePeterson lock = new DoblePeterson();
		CounterNaive counter = new CounterNaive();
		AtomicIntegerArray porHilo = new AtomicIntegerArray(4);

		// Pool fijo de 4 hilos: son exactamente los 4 hilos que van a competir
		// por el candado, cada uno con su id fijo asignado por ThreadLocal.
		ExecutorService executor = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 400; i++) {
			executor.execute(() -> tarea(lock, counter, porHilo));
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		// Se espera Contador final = 400 siempre y
		// se imprime el reparto por hilo para revisar si fue equitativo.
		System.out.println("Contador final: " + counter.getValue());
		for (int i = 0; i < 4; i++) {
			System.out.println("Hilo id " + i + " ejecuto " + porHilo.get(i) + " tareas");
		}
	}
}