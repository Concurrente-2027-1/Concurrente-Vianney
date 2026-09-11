package unam.fc.concurrent.practica2;

// Ejercicio 1: Cola concurrente usando ExecutorService, SIN locks ni synchronized.
// Envuelve una ColaSecuencial y expone enq/deq como Callables
// que se mandan a un pool de hilos.

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ColaConcurrente {

    private ColaSecuencial cola;

    public ColaConcurrente() {
        this.cola = new ColaSecuencial();
    }

    // Tarea de encolar, se manda al pool con submit()
    public Callable<Boolean> tareaEnq(String x) {
        return () -> cola.enq(x);
    }

    // Tarea de desencolar, se manda al pool con submit()
    public Callable<String> tareaDeq() {
        return () -> cola.deq();
    }

    public void print() {
        cola.print();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ColaConcurrente cq = new ColaConcurrente();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<Future<?>> futures = new ArrayList<>();

        // Secuencia de ejemplo: enq(a), enq(b), deq()
        futures.add(executor.submit(cq.tareaEnq("a")));
        futures.add(executor.submit(cq.tareaEnq("b")));
        futures.add(executor.submit(cq.tareaDeq()));

        // Secuencia 2: enq(a), enq(b) 
        // futures.add(executor.submit(cq.tareaEnq("a")));
        // futures.add(executor.submit(cq.tareaEnq("b")));

        // Secuencia 3: enq(a), deq(), deq() 
        // futures.add(executor.submit(cq.tareaEnq("a")));
        //futures.add(executor.submit(cq.tareaDeq()));
        //futures.add(executor.submit(cq.tareaDeq()));

        executor.shutdown();
        executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS);

        // Resultados de cada Future.
        for (int i = 0; i < futures.size(); i++) {
            Object resultado = futures.get(i).get();
            System.out.println("Tarea " + i + " resultado: " + resultado);
        }

        System.out.println("Estado final de la cola:");
        cq.print();
    }
}