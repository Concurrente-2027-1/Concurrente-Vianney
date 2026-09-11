package unam.fc.concurrent.practica2;

// Ejercicio 4: misma idea que ColaConcurrente (Ejercicio 1), pero ahora
// enq() y deq() se ejecutan dentro de un bloque synchronized sobre el
// mismo objeto (la instancia de ColaSecuencial), de forma que ambos
// metodos forman UNA SOLA seccion critica: mientras un hilo esta dentro
// (sea haciendo enq o deq), ningun otro hilo puede entrar a ninguno de
// los dos metodos al mismo tiempo.

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ColaConcurrenteSync {

    private ColaSecuencial cola;

    public ColaConcurrenteSync() {
        this.cola = new ColaSecuencial();
    }

    public Callable<Boolean> tareaEnq(String x) {
        return () -> {
            synchronized (cola) {
                return cola.enq(x);
            }
        };
    }

    public Callable<String> tareaDeq() {
        return () -> {
            synchronized (cola) {
                return cola.deq();
            }
        };
    }

    public void print() {
        cola.print();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ColaConcurrenteSync cq = new ColaConcurrenteSync();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<?>> futures = new ArrayList<>();

        // Misma secuencia del Ejercicio 2, caso 1, para comparar directamente
        futures.add(executor.submit(cq.tareaEnq("a")));
        futures.add(executor.submit(cq.tareaEnq("b")));
        futures.add(executor.submit(cq.tareaDeq()));

        executor.shutdown();
        executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);

        for (int i = 0; i < futures.size(); i++) {
            Object resultado = futures.get(i).get();
            System.out.println("Tarea " + i + " resultado: " + resultado);
        }

        System.out.println("Estado final de la cola:");
        cq.print();
    }
}