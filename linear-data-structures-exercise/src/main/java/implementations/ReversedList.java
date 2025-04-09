package implementations;

import interfaces.List;

import java.util.Arrays;
import java.util.Iterator;

public class ReversedList<E> implements List<E> {
    private final int INITIAL_CAPACITY = 2;
    private int head;
    private int tail;
    private int size;

    private Object[] elements;

    public ReversedList() {
        this.elements = new Object[INITIAL_CAPACITY];
        this.head = 0;
        this.tail = this.head;
    }

    @Override
    public boolean add(E element) {
        if (this.size == 0) {
            this.elements[this.head] = element;
        } else {
            if (this.head == this.elements.length - 1) {
                this.elements = grow();
            }

            this.elements[++this.head] = element;
        }

        this.size++;
        return true;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int capacity() {
        return this.elements.length;
    }

    @Override
    public E get(int index) {
        int realIndex = this.size - 1 - index;

        ensureIndex(realIndex);

        return this.getAt(realIndex);
    }

    @Override
    public E removeAt(int index) {
        int realIndex = this.head + index;

        ensureIndex(realIndex);

        E toRemove = this.getAt(realIndex);

        this.remove(toRemove);
        return toRemove;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = elements.length - 1;

            @Override
            public boolean hasNext() {
                return index != head;
            }

            @Override
            public E next() {
                return getAt(index--);
            }
        };
    }

    private Object[] grow() {
        int newCapacity = this.elements.length * 2;

        Object[] newElements;

        newElements = Arrays.copyOf(this.elements, newCapacity);

        return newElements;
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) this.elements[index];
    }

    private void ensureIndex(int index) {
        if (index < this.head || index > this.tail) {
            throw new IndexOutOfBoundsException();
        }
    }

    private E remove(Object object) {
        if (this.size == 0) {
            return null;
        }

        for (int i = this.head; i <= this.tail; i++) {
            if (this.elements[i].equals(object)) {
                E element = this.getAt(i);
                this.elements[i] = null;

                for (int j = i; j < this.tail; j++) {
                    this.elements[j] = this.elements[j + 1];

                }

                this.removeLast();
                return element;
            }
        }

        return null;
    }

    private E removeLast() {
        if (this.size != 0) {
            E element = this.getAt(this.tail);
            this.elements[this.tail--] = null;
            this.size--;

            return element;
        }

        return null;
    }
}
