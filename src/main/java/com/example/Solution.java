package com.example;
import java.io.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Result {

 
/*
 * List<Long[]> pricesWithYears = new ArrayList<>();

        for(int i = 0; i < price.size(); i++){
            pricesWithYears.add(new Long[] { Long.valueOf(i), price.get(i) });
        }

        pricesWithYears.sort((pair1, pair2) -> pair1[1].compareTo(pair2[1]));

        int minLoss = -1;

        for(int i = 0; i < price.size(); i++){
            if(minLoss == -1) {
                int nextIndex = i + 1;

                while(nextIndex < price.size()){
                    Long[] priceWithYear = pricesWithYears.get(nextIndex);
                    long nextYear = priceWithYear[0];
                    long nextPrice = priceWithYear[1];

                }
            }
        }
 */

 /*
  * price.sort(Comparator.naturalOrder());

        long minLoss = -1l;

        for(int i = 0; i < price.size(); i++){
            if(i + 1 < price.size()){
                if(minLoss == -1l){
                    minLoss = Math.abs(price.get(i) - price.get(i+1));
                } else {
                    minLoss = Math.min(minLoss, Math.abs(price.get(i) - price.get(i+1)));
                }
            }
        }

        return minLoss;    


          /* 
            while(nextIndex < price.size()){
                Long[] priceWithYear = pricesWithYears.get(nextIndex);
                long nextYear = priceWithYear[0];
                long nextPrice = priceWithYear[1];
                
                if(nextYear > currentYear) {
                    if(minLoss == -1) {
                        minLoss = Math.abs(currentPrice - nextPrice);
                    } else {
                        minLoss = Math.min(minLoss, Math.abs(currentPrice - nextPrice));
                    }

                    break;
                }

                nextIndex++;
            }

            while(lastIndex >= 0){
                Long[] priceWithYear = pricesWithYears.get(lastIndex);
                long lastYear = priceWithYear[0];
                long lastPrice = priceWithYear[1];
                
                if(lastYear > currentYear) {
                    if(minLoss == -1) {
                        minLoss = Math.abs(currentPrice - lastPrice);
                    } else {
                        minLoss = Math.min(minLoss, Math.abs(currentPrice - lastPrice));
                    }

                    break;
                }

                lastIndex--;
            }
        }

               List<Long> candidates =
                pricesWithYears.stream()
                .filter((pwy) -> pwy[0] > currentYear)
                .map((pwy) -> pwy[1])
                .collect(Collectors.toUnmodifiableList());
            */


    
    public static long minimumLoss(List<Long> price) {
        List<Long[]> pricesWithYears = new ArrayList<>();

        for(int i = 0; i < price.size(); i++){
            pricesWithYears.add(new Long[] { Long.valueOf(i), price.get(i) });
        }

        pricesWithYears.sort((pair1, pair2) -> pair1[1].compareTo(pair2[1]));

        long minLoss = -1l;

        for(int i = 1; i < price.size(); i++){
            Long[] currentPriceWithIndex = pricesWithYears.get(i);
            long currentYear = currentPriceWithIndex[0];
            long currentPrice = currentPriceWithIndex[1];

            Long[] lastPriceWithIndex = pricesWithYears.get(i - 1);
            long lastYear = lastPriceWithIndex[0];
            long lastPrice = lastPriceWithIndex[1];

            if(currentYear < lastYear){
                if(minLoss == -1l){
                    minLoss = currentPrice - lastPrice;
                } else {
                    minLoss = Math.min(minLoss, currentPrice - lastPrice);
                }
            }            
        }

        return minLoss;
    }
}

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        int n = Integer.parseInt(bufferedReader.readLine().trim());

        List<Long> price = Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        long result = Result.minimumLoss(price);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedReader.close();
        bufferedWriter.close();
    }
}