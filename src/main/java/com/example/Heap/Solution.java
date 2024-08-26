package com.example.Heap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class FraudulentActivityNotification {
    public static int activityNotifications(List<Integer> expenditure, int d) {
        final Heap minHeap = new Heap(Comparator.naturalOrder());
        final Heap maxHeap = new Heap(Comparator.reverseOrder());

        List<Integer> firstWindow = new ArrayList<>(expenditure.subList(0, d));
        firstWindow.sort(Comparator.naturalOrder());

        for(int i = 0; i < firstWindow.size() / 2; i++) {
            maxHeap.insert(firstWindow.get(i));
        }

        for(int i = firstWindow.size() / 2; i < firstWindow.size(); i++) {
            minHeap.insert(firstWindow.get(i));
        }

        int alerts = 0;

        for(int i = d; i < expenditure.size(); i++){
            int current = expenditure.get(i);

            float median = 0;

            if(minHeap.size() == maxHeap.size())  { //window is even
                median = (maxHeap.root() + minHeap.root()) / 2f;
            } else if(maxHeap.size() > minHeap.size()) { //window is odd 
                median = maxHeap.root();
            } else {
                median = minHeap.root(); // Added this to cover the case where minHeap might be larger
            }

            if(current >= 2f * median){
                alerts++;
            }

            // Remove the element that is sliding out of the window
            int outgoing = expenditure.get(i - d);

            if (outgoing <= maxHeap.root()) {
                int indexToRemove = maxHeap.findIndex(outgoing);
                maxHeap.removeAt(indexToRemove);
            } else {
                int indexToRemove = minHeap.findIndex(outgoing);
                minHeap.removeAt(indexToRemove);
            }

            // Rebalance the heaps if necessary
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.insert(maxHeap.removeAt(0));
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.insert(minHeap.removeAt(0));
            }

            if (current <= maxHeap.root()) {
                maxHeap.insert(current);
            } else {
                minHeap.insert(current);
            }

            // Rebalance the heaps if necessary
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.insert(maxHeap.removeAt(0));
            } else if (minHeap.size() > maxHeap.size()) {
                maxHeap.insert(minHeap.removeAt(0));
            }
        }

        return alerts;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int n = Integer.parseInt(firstMultipleInput[0]);

        int d = Integer.parseInt(firstMultipleInput[1]);

        List<Integer> expenditure = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        int result = FraudulentActivityNotification.activityNotifications(expenditure, d);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}