package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class AdventOfCode {
    public static void main(String[] args) throws FileNotFoundException {
        File file = null;
        day4();
    }

    private static ArrayList<String> listFromFile(File f) throws FileNotFoundException {
        Scanner scanner = new Scanner(f);
        ArrayList<String> list = new ArrayList<>();

        while (scanner.hasNext()) {
            list.add(scanner.nextLine());
        }
        return list;
    }

    private static int getPriorityFromIndex(int index){
        if(97 <= index && index <= 122) {
            return index - 96;
        }
        return index - 38;
    }

    static class Interval{
        public int start;
        public int end;

        Interval(String start, String end){
            this.start = Integer.parseInt(start);
            this.end = Integer.parseInt(end);
        }
    }

    private static boolean fullyContains(Interval X, Interval Y){
        return ((X.start <= Y.start) && (Y.end <= X.end)) ||
               ((Y.start <= X.start) && (X.end <= Y.end));
    }
/*
    private static boolean overlaps(Interval X, Interval Y){
      return ((X.start <= Y.start) && (Y.start <= X.end)) ||
              ((X.start <= Y.end) && (Y.end <= X.end)) ||
              ((Y.start <= X.start) && (X.start <= Y.end)) ||
              ((Y.start <= X.end) && (X.end <= Y.end));
    }
 */

    private static boolean overlaps(int[] x){

        return ((x[0] <= x[2]) && (x[2] <= x[1])) ||
                ((x[0] <= x[3]) && (x[3] <= x[1]) ||
                ((x[2] <= x[0]) && (x[0] <= x[3])) ||
                ((x[2] <= x[1]) && (x[1] <= x[3])));
    }



    private static void day4() throws FileNotFoundException {
        ArrayList<String> list = listFromFile(new File("/Users/annamarcinkowska/Desktop/AdventOfCode/day4.txt"));

        int rowNum = 0;
        int count = 0;

        while (rowNum < list.size()) {
            String currentRowData = list.get(rowNum);
            int[] entries = Arrays.stream(currentRowData.split("-|\\,")).mapToInt(Integer::parseInt).toArray();
            count += overlaps(entries) ? 1 : 0;

            /*
            Interval elf1 = new Interval(entries[0], entries[1]);
            Interval elf2 = new Interval(entries[2], entries[3]);

            count += overlaps(elf1, elf2) ? 1 : 0;
            //count += fullyContains(elf1, elf2) ? 1 : 0;
             */

            rowNum++;
        }
        System.out.println("Count = " + count);
    }

    
    private static void day3() throws FileNotFoundException {
        ArrayList<String> list = listFromFile(new File("/Users/annamarcinkowska/Desktop/AdventOfCode/day3.txt"));
        ArrayList<String> leftList = new ArrayList<>();
        ArrayList<String> rightList = new ArrayList<>();
        StringBuffer str = new StringBuffer();

        //PART I
        //Split list into left and right list
        for(String entry : list){ //TODO:refactor
            str.setLength(0);
            int size = entry.length()/2;
            for(int i = 0; i < size; i++){
                str = str.append(entry.charAt(i));
            }
            leftList.add(str.toString());
        }

        for(String entry : list){
            str.setLength(0);
            int size = entry.length()/2;
            for(int i = size; i < entry.length(); i++){
                str = str.append(entry.charAt(i));
            }
            rightList.add(str.toString());
        }

        int listSize = leftList.size();
        int index = 0;

        int totalSum = 0;
        HashSet<Integer> set = new HashSet<>();

        while(index < listSize){
            String leftSide = leftList.get(index);
            String rightSide = rightList.get(index);

            for(int i = 0; i < leftSide.length(); i++){
                for(int j = 0; j < rightSide.length(); j++){
                    int ascii = (int) leftSide.charAt(i);
                    if(leftSide.charAt(i) == rightSide.charAt(j) && !set.contains(ascii)){
                        set.add(ascii);

                        //priority:
                        //Lowercase item types a through z have priorities 1 through 26.
                        //Uppercase item types A through Z have priorities 27 through 52.
                        totalSum += getPriorityFromIndex(ascii);
                    }
                }
            }
            index++;
            set.clear();
        }

        System.out.println("Total sum = " + totalSum);

        //PART II

        int rowNum = 0;
        int[] asciiTable = new int[255]; //ASCII table is 256 long
        Arrays.fill(asciiTable,0);
        int total = 0;

        while(rowNum < list.size()){
            String currentRowData = list.get(rowNum);
            HashSet<Integer> uniqueGuard = new HashSet<>();

            for(int i = 0; i < currentRowData.length(); i++){
                int ascii = (int) currentRowData.charAt(i);

                if(!uniqueGuard.contains(ascii)){
                    uniqueGuard.add(ascii);
                    asciiTable[ascii]++;

                    if(asciiTable[ascii] == 3){
                        total += getPriorityFromIndex(ascii);
                        Arrays.fill(asciiTable,0);
                        break;
                    }
                }
            }
            rowNum++;
        }
        System.out.println("The total is = " + total);
    }

    private static void day2() throws FileNotFoundException {
        ArrayList<String> list = listFromFile(new File("/Users/annamarcinkowska/Desktop/AdventOfCode/day2.txt"));
        int totalSum = 0;

        for (String entry : list){
            for(int i = 0; i < entry.length(); i++){
                if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'X'){
                    totalSum += (0+3); //you need to lose
                    i += 2;
                }else if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'Y'){
                    totalSum += (3+1); //you need to draw
                    i += 2;
                }else if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'Z'){
                    totalSum += (6+2); //you need to win
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'X'){
                    totalSum += (0+1); //lose
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'Y'){
                    totalSum += (3+2); //draw
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'Z'){
                    totalSum += (6+3); //win
                    i += 2;
                }else if(entry.charAt(i) == 'C' && entry.charAt(i+2) == 'X'){
                    totalSum += (0+2); //lose
                    i += 2;
                }else if(entry.charAt(i) == 'C' && entry.charAt(i+2) == 'Y'){
                    totalSum += (3+3); //draw
                    i += 2;
                }else{
                    totalSum += (6+1); //win
                    i += 2;
                }
            }
        }

        /*


        //A = Rock, X. worth 1
        //B = Paper, Y. worth 2
        //C = Scissors, Z. worth 3
        //loose = 0, draw = 3, win = 6

        for (String entry : list){
            for(int i = 0; i < entry.length(); i++){
                if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'X'){
                    totalSum += (1+3); //draw
                    i += 2;
                }else if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'Y'){
                    totalSum += (2+6); //win
                    i += 2;
                }else if(entry.charAt(i) == 'A' && entry.charAt(i+2) == 'Z'){
                    totalSum += (3+0); //loose
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'X'){
                    totalSum += (1+0); //loose
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'Y'){
                    totalSum += (2+3); //draw
                    i += 2;
                }else if(entry.charAt(i) == 'B' && entry.charAt(i+2) == 'Z'){
                    totalSum += (3+6); //win
                    i += 2;
                }else if(entry.charAt(i) == 'C' && entry.charAt(i+2) == 'X'){
                    totalSum += (1+6); //win
                    i += 2;
                }else if(entry.charAt(i) == 'C' && entry.charAt(i+2) == 'Y'){
                    totalSum += (2+0); //loose
                    i += 2;
                }else{ //entry.charAt(i) == 'C' && entry.charAt(i+2) == 'Z'
                    totalSum += (3+3); //draw
                    i += 2;
                }
            }
        }

         */
        System.out.println(totalSum);
    }

    private static void day1() throws FileNotFoundException {
        ArrayList<String> list = listFromFile(new File("/Users/annamarcinkowska/Desktop/AdventOfCode/day1.txt"));
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        int elfNumber = 1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isEmpty()) {
                sum = 0;
                elfNumber++;
                map.put(elfNumber, sum);
            } else {
                sum += Integer.parseInt(list.get(i));
                map.put(elfNumber, sum);
            }
        }

        int largestNum = Collections.max(map.values());
        HashMap<Integer, Integer> tempMap = new HashMap<>(map);
        int totalSum = 0;

        /*
        for(var entry : tempMap.entrySet()){
            if(entry.getValue() == largestNum){
                System.out.println("Elf with max number of calories is elf number = " + entry.getValue());
                totalSum += map.remove(entry.getKey());
            }
        }

         */

        totalSum = largestNum;
        Iterator<Map.Entry<Integer,Integer>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer,Integer> entry = iterator.next();
            if(entry.getValue() == largestNum){
                System.out.println("Elf with max number of calories is elf number = " + entry.getValue());
                iterator.remove();
                largestNum = Collections.max(map.values());
                totalSum += largestNum;
            }
        }

        System.out.println("Total = " + totalSum);
    }
}


