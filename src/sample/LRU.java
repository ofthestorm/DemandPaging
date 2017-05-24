package sample;

import java.text.DecimalFormat;
import java.time.Clock;
import java.util.Random;

/**
 * Created by keke on 2017/5/17.
 */

public class LRU {

    public LRU(Instruction ins) {


        currentInstruction = 0;
        missPageCount = 0;
        executedInstructions = 0;
        index = 0;

        i = 0;
        memory = new int[4];
        bitmap = new boolean[] {false, false, false, false};
        instructions = new int[320];
        df = new DecimalFormat("#.00");
        this.instructions = ins.getInstructions();

    }

    //the order number of current instruction
    private static int currentInstruction = 0;
    //the count of missing page
    private static int missPageCount = 0;
    //the number of executed instructions
    private static int executedInstructions = 0;
    //the index should be replaced
    private static int index = 0;

    private static int i = 0;
    //the list of memory which contains 4 parts
    //private ArrayList<Integer> memory = new ArrayList<>(4);
    private static int [] memory = new int[4];
    //false:memory is empty, true:memory is full
    private static boolean [] bitmap = new boolean[] {false,false,false,false};
    //unused time
    private static int [] time = new int[] {0,0,0,0};
 //   private static Clock[] time = new Clock [4];
    //the array contains the executing order of instructions
    private static int [] instructions = new int[320];

    DecimalFormat df = new DecimalFormat("#.00");

    //execute instructions
//    public void executeInstructions() throws InterruptedException {
//
//     //   generateInstructions();
//        int i = 0;
//
//        while (executedInstructions != 320) {
//            executedInstructions += 1;
//
//            currentInstruction = instructions[i++];
//            int currentPage = getPage(currentInstruction);
//            if (isExist(currentPage)) { // memory contains current page
//                time[getIndexOfPage(currentPage)] = 0;
//            } else {
//                missPageCount += 1; // record missing page
//                if (getEmptyPart() == 0) { // memory is already full
//
//                    int leastRecentlyUsedIndex = getLeastRecentlyUsedIndex();
//                    memory[leastRecentlyUsedIndex] = currentPage;
//                    time[leastRecentlyUsedIndex] = 0;
//                    for (int j = 0; j < 4; j++) {
//                        if (j != leastRecentlyUsedIndex)
//                            time[j] += 1;
//                    }
//
//                } else {
//                    //add current page
//                    //memory.add(currentPage);
//                    int firstEmptyIndex = getFirstEmptyIndex();
//                    memory[firstEmptyIndex] = currentPage;
//                    bitmap[firstEmptyIndex] = true;
//                    time[firstEmptyIndex] = 0;
//                    for (int j = 0; j < 4; j++) {
//                        if (j != firstEmptyIndex)
//                            time[j] += 1;
//                    }
//
//                }
//            }
//            displayMemory();
//            Thread.sleep(1000);
//        }
//    }
    public void executeInstructions()  {

        //   generateInstructions();

            executedInstructions += 1;

            currentInstruction = instructions[i++];
            int currentPage = getPage(currentInstruction);
            if (isExist(currentPage)) { // memory contains current page
                time[getIndexOfPage(currentPage)] = 0;
            } else {
                missPageCount += 1; // record missing page
                if (getEmptyPart() == 0) { // memory is already full

                    index = getLeastRecentlyUsedIndex();
                    memory[index] = currentPage;
                    time[index] = 0;
                    for (int j = 0; j < 4; j++) {
                        if (j != index)
                            time[j] += 1;
                    }

                } else {
                    //add current page
                    //memory.add(currentPage);
                    index = getFirstEmptyIndex();
                    memory[index] = currentPage;
                    bitmap[index] = true;
                    time[index] = 0;
                    for (int j = 0; j < 4; j++) {
                        if (j != index)
                            time[j] += 1;
                    }

                }

        //    displayMemory();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
    }

    private static boolean isExist(int currentPage) {
        for (int i = 0; i < 4; i++) {
            if (memory[i] == currentPage)
                return true;
        }
        return false;
    }

    private static int getIndexOfPage(int page) {
        for (int i = 0; i < 4; i++) {
            if(memory[i] == page)
                return i;
        }
        return -1;
    }

    private static int getEmptyPart() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (bitmap[i] == false)
                count++;
        }
        return count;
    }

    private static int getFirstEmptyIndex() {
        for (int i = 0; i < 4; i++) {
            if(bitmap[i] == false)
                //    bitmap[i] = true;
                return i;
        }
        return -1;
    }

    private static int getLeastRecentlyUsedIndex() {
        int index = 0;
        for (int i = 1; i < 4; i++) {
            if(time[index] < time[i]) {
                index = i;
            }
        }
        return index;
    }



    private static int getPage(int instruction) {
        return (int)(instruction / 10);
    }

    public int getCurrentInstruction() {
        return currentInstruction;
    }

    public int getMissingPageCount() {
        return missPageCount ;
    }

    public int getExecutedInstructions() {
        return executedInstructions;
    }

    //    public ArrayList<Integer> getMemory() {
//        return memory;
//    }
    public int [] getMemory() {
        return memory;
    }

    public int getIndex() {
//        return index;
        return getIndexOfPage(getPage(currentInstruction));
    }


    public String getRateOfMissingPage() {
        double result = (double)missPageCount / (double)320 * 100;
        return missPageCount + " / " + 320 + " = " + df.format(result) + "%";
    }

    private static void displayMemory() {
        for (int i = 0; i < 4; i++) {
            System.out.print(memory[i]+" ");
        }
        System.out.print("\n");
        for (int i = 0; i < 4; i++) {
            System.out.print(time[i]+" ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) throws InterruptedException {
//        executeInstructions();

        System.out.println(currentInstruction);
        System.out.println(missPageCount);
        System.out.println(executedInstructions);
//        System.out.println(getRateOfMissingPage());
    }
}
