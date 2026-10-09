# Práctica 4 – Spinlocks y algunas primitivas

## Estructura

```
practica4/
└── unam/fc/concurrent/practica4/
    ├── README.md
    ├── reporte_practica4.pdf
    ├── TASLock.java               ┐
    ├── TTASLock.java              │
    ├── BackoffLock.java           │
    ├── Backoff.java               │  programas proporcionados
    ├── MCSLock.java               │  (sin modificar)
    ├── ALock.java                 │
    ├── CLHLock.java               │
    ├── CounterAtomic.java         │  ← Programa 1
    ├── RunSpin.java               ┘  ← Programa 2 (usado en el Ejercicio 1)
    └── ejercicios/
        └── RunSpinMatrix.java     ← Ejercicio 2 (programa implementado)
```

## Compilar (Java 21 LTS)

Desde la carpeta `practica4`:

```
javac --release 21 -d out unam/fc/concurrent/practica4/*.java unam/fc/concurrent/practica4/ejercicios/*.java
```

## Ejecutar

Ejercicio 1:

```
java -cp out unam.fc.concurrent.practica4.RunSpin
```

Ejercicio 2:

```
java -cp out unam.fc.concurrent.practica4.ejercicios.RunSpinMatrix
```

En ambos programas el candado se elige descomentando una sola línea
`Lock lock = new ...;` y el número de hilos con `numberThreads`.
En `RunSpin`, el número de tareas es el límite del `for`.
