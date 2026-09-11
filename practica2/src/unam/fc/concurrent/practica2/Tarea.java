package unam.fc.concurrent.practica2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tarea implements Runnable{
	int tiempoTarea;
	int task;
	final Semaphore smphre;

	// Candado compartido SOLO entre las tareas del hilo 0 y del hilo 2,
	// para que nunca se ejecuten al mismo tiempo entre ellas dos.
	static Lock lockCeroDos = new ReentrantLock();

	public Tarea(int i, Semaphore smphre) {
		this.task = i;
		this.smphre = smphre;
	}

	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		long id = currentThread.getId();
		int value = (int) (id % 6);
		boolean necesitaExclusion = (value == 0 || value == 2);

		try {
			smphre.acquire(); // Maximo 3 tareas al mismo tiempo

			if (necesitaExclusion) {
				lockCeroDos.lock(); // Los hilos 0 y 2 no pueden entrar juntos
			}

			try {
				System.out.println("Running Thread " + value + " task: " + this.task);
				switch(value) {
				  case 0, 2:
					  this.tiempoTarea = 500;
				    break;
				  case 1:
					  this.tiempoTarea = 2000;
				    break;
				  default:
					  this.tiempoTarea = 3000;
				}

				Thread.sleep(this.tiempoTarea);

				System.out.println("Running Thread " + value + " time: " + this.tiempoTarea);
			} finally {
				if (necesitaExclusion) {
					lockCeroDos.unlock();
				}
			}

		} catch(InterruptedException e) {
			System.out.println(e);
		} finally {
			smphre.release();
		}
	}
}