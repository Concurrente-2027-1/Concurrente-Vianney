package unam.fc.concurrent.practica3;
// Version de Peterson que recibe el id (0 o 1) como parametro en vez de
// calcularlo con Thread.currentThread().getId() % 2.
// Esto es necesario para poder anidarlo en un arbol de torneo (DoblePeterson):
// si dejaramos que cada candado calculara el id a partir del id real del hilo,
// dos hilos distintos podrian caer en el mismo id (0 o 1) dentro del mismo
// candado y se rompería la exclusión mutua.

public class PetersonID {
	private volatile boolean[] flag = new boolean[2];
	private volatile int victim;

	public PetersonID() {
		flag[0] = false;
		flag[1] = false;
	}

	// El hilo con id i avisa que quiere entrar, cede el turno al otro (j) y
	// solo espera si el otro tambien quiere entrar y ademas fue el ultimo en ceder.
	public void lock(int i) {
		int j = 1 - i;
		flag[i] = true;
		victim = i;
		while (flag[j] && victim == i) {
		};
	}

	// El hilo con id i avisa que ya salio de la seccion critica.
	public void unlock(int i) {
		flag[i] = false;
	}
}