package com.vocahype.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CustomList<T> implements Iterable<T> {
    private ArrayList<T> list;

    // Constructor
    public CustomList() {
        list = new ArrayList<>();
    }

    // Add method
    public void add(T element) {
        list.add(element);
    }

    // AddAll method
    public void addAll(Collection<? extends T> elements) {
        list.addAll(elements);
    }

    // Size method
    public int size() {
        return list.size();
    }

    // Get method
    public T get(int index) {
        return list.get(index);
    }

    // Iterator method
    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }
}


