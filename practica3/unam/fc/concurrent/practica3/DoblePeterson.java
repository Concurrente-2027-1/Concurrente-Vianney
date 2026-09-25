package unam.fc.concurrent.practica3;
// Candado para 4 hilos armado como un arbol de torneo de tres candados
// PetersonID: uno para el par de hilos globales {0,1} (left), otro para el
// par {2,3} (right), y un tercero (top) donde compite el ganador de left
// contra el ganador de right.

public class DoblePeterson {
	private PetersonID left = new PetersonID();  // hilos globales 0 y 1
	private PetersonID right = new PetersonID(); // hilos globales 2 y 3
	private PetersonID top = new PetersonID();   // ganador de left vs ganador de right

	// Primero compite en su candado de hoja (left o right segun a que par
	// pertenezca) y, al ganarlo, compite en "top" contra el ganador del otro par.
	// Solo entra a la seccion critica si gana ambos niveles.
	public void lock(int id) {
		int local = id % 2;
		int nivelTop = id / 2;
		if (id == 0 || id == 1) {
			left.lock(local);
		} else {
			right.lock(local);
		}
		top.lock(nivelTop);
	}

	// Libera los candados en orden inverso a como se pidieron: primero "top",
	// luego el de hoja correspondiente.
	public void unlock(int id) {
		int local = id % 2;
		int nivelTop = id / 2;
		top.unlock(nivelTop);
		if (id == 0 || id == 1) {
			left.unlock(local);
		} else {
			right.unlock(local);
		}
	}
}