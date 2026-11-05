package co.umariana.lamento.estructura;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista enlazada CIRCULAR del equipo (tarea de ESTRUCTURA).
 *
 * <p>
 * El último nodo apunta de nuevo al primero. Implemente la lista con sus
 * propios nodos: no se
 * permite usar {@code ArrayList}, {@code LinkedList} ni ninguna otra colección
 * de {@code java.util}
 * para guardar los elementos (las pruebas lo revisan).
 *
 * <p>
 * Comportamiento especial por ser circular: el cursor NUNCA se detiene.
 * {@link #avanzar()} desde
 * el último elemento vuelve al primero, y {@link #retroceder()} desde el
 * primero va al último.
 * Ambos devuelven {@code true} mientras la lista no esté vacía.
 *
 * <p>
 * El iterador recorre los elementos UNA sola vez, desde el primero hasta el
 * último (¡sin quedar
 * dando vueltas para siempre!).
 */
public class ListaCircular<T> implements Lista<T> {

    private static class Nodo<T> {
        T dato;
        Nodo<T> siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private Nodo<T> cursor;
    private int tamano;

    private Nodo<T> nodoEn(int indice) {
        Nodo<T> nodo = cabeza;
        for (int i = 0; i < indice; i++) {
            nodo = nodo.siguiente;
        }
        return nodo;
    }

    @Override
    public void agregar(T elemento) {
        insertar(tamano, elemento);
    }

    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Índice " + indice + ", tamaño " + tamano);
        }
        Nodo<T> nuevo = new Nodo<>(elemento);

        if (cabeza == null) {
            nuevo.siguiente = nuevo;
            cabeza = nuevo;
            cola = nuevo;
            cursor = nuevo;

        } else if (indice == 0) {
            nuevo.siguiente = cabeza;
            cola.siguiente = nuevo; 
            cabeza = nuevo;

        } else {

            Nodo<T> anterior = (indice == tamano) ? cola : nodoEn(indice - 1);
            nuevo.siguiente = anterior.siguiente;
            anterior.siguiente = nuevo;
            if (anterior == cola) {
                cola = nuevo;
            }
        }
        tamano++;
    }

    @Override
    public T obtener(int indice) {
         if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice " + indice + ", tamaño " + tamano);
        }
        return nodoEn(indice).dato;
    }

    @Override
    public T eliminar(int indice) {
                if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Índice " + indice + ", tamaño " + tamano);
        }
        if (tamano == 1) {
            T dato = cabeza.dato;
            cabeza = null;
            cola = null;
            cursor = null;
            tamano = 0;
            return dato;
        }
        
        Nodo<T> anterior = (indice == 0) ? cola : nodoEn(indice - 1);
        Nodo<T> nodo = anterior.siguiente;
        anterior.siguiente = nodo.siguiente;

        if (nodo == cabeza) {
            cabeza = nodo.siguiente;
            }
            
        if (nodo == cola) {
            cola = anterior;
        }
        if (nodo == cursor) {
            cursor = nodo.siguiente;
        }
        tamano--;
        return nodo.dato;
    }

    @Override
    public int tamano() {
        return tamano;
    }

    @Override
    public boolean estaVacia() {
       return tamano == 0;
    }

    @Override
    public void vaciar() {
        cabeza = null;
        cola = null;
        cursor = null;
        tamano = 0;
    }

    @Override
    public T actual() {
         return cursor == null ? null : cursor.dato;
    }

    @Override
    public boolean avanzar() {
        if (cursor == null) {
            return false;
        }
        cursor = cursor.siguiente;
        return true;
    }

    @Override
    public boolean retroceder() {
      if (cursor == null) {
            return false;
        }
        
        Nodo<T> nodo = cabeza;
        while (nodo.siguiente != cursor) {
            nodo = nodo.siguiente;
        }
        cursor = nodo;
        return true;
    }

    @Override
    public Iterator<T> iterator() {
       return new Iterator<T>() {
            private Nodo<T> siguiente = cabeza;
            private int restantes = tamano; 
            @Override
            public boolean hasNext() {
                return restantes > 0;
            }

            @Override
            public T next() {
                if (restantes == 0) {
                    throw new NoSuchElementException();
                }
                T dato = siguiente.dato;
                siguiente = siguiente.siguiente;
                restantes--;
                return dato;
            }
        };
    }
}
