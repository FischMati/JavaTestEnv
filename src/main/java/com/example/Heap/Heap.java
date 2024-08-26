package com.example.Heap;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Heap {
    private Comparator<Integer> comparator;
    private List<Integer> nodes = new LinkedList<>();


    public Heap(Comparator<Integer> comparator) {
        this.comparator = comparator;
    }

    public int size(){
        return nodes.size();
    }

    public int root(){
        return nodes.get(0);
    }

    private void heapifyOnInsert(int index) { 
       int parentIndex = (index - 1) / 2;
       
       while(index > 0) {
            int parent = nodes.get(parentIndex);
            int current = nodes.get(index);

            if(comparator.compare(parent,nodes.get(index)) > 0){
                nodes.set(parentIndex, current);
                nodes.set(index, parent);
                index = parentIndex;
                parentIndex = (index - 1) / 2;
            } else {
                break;
            }
       }

    }

    public void insert(int node) {
        nodes.add(node);
        this.heapifyOnInsert(nodes.size() - 1); 
    }

    public int findIndex(int value) {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == value) {
                return i;
            }
        }
        return -1; 
    }

    private void heapifyOnRemove(int index){
        int lastNodeIndex = nodes.size() - 1;

        if (index == lastNodeIndex) {
            nodes.remove(lastNodeIndex);
            return;
        }

        nodes.set(index, nodes.get(lastNodeIndex));
        nodes.remove(lastNodeIndex);
        
        int leftChildIndex = 2*index + 1;
        int rightChildIndex = 2*index + 2;

        while(leftChildIndex < nodes.size()){
            int current = nodes.get(index);
            int leftChild = nodes.get(leftChildIndex);
            int minChild = leftChild;
            int minChildIndex = leftChildIndex;

            if(rightChildIndex < nodes.size() && comparator.compare(nodes.get(rightChildIndex), minChild) < 0) {
                minChild = nodes.get(rightChildIndex);
                minChildIndex = rightChildIndex;
            }

            if(comparator.compare(current, minChild) > 0){
                nodes.set(index, minChild);
                nodes.set(minChildIndex, current);

                index = minChildIndex;
            } else {
                break;
            }


            leftChildIndex = 2 * index + 1;
            rightChildIndex = 2 * index + 2;
        }
    }


    public int removeAt(int nodeIndex){
        if (nodeIndex == -1) {
            throw new IllegalArgumentException("Element not found in heap.");
        }

        int value = nodes.get(nodeIndex);
        if (nodeIndex == nodes.size() - 1) {
            nodes.remove(nodeIndex);
        } else {
            heapifyOnRemove(nodeIndex);
        }
        return value;
    }
}
