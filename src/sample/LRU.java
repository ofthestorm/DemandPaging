package sample;

import java.text.DecimalFormat;

/**
 * Created by keke on 2017/5/17.
 */

public class LRU {

    public LRU(Instruction ins) {
        currentInstruction = 0;
        missPageCount = 0;
        executedInstructions = 0;
        index = 0;
        count = 0;
        memory = new int[] {-1, -1, -1, -1};
        bitmap = new boolean[] {false, false, false, false};
        time = new int[] {0,0,0,0};
        instructions = new int[320];
        df = new DecimalFormat("#.00");
        this.instructions = ins.getInstructions();

    }

    //the order number of current instruction
    private static int currentInstruction;
    //the count of missing page
    private static int missPageCount;
    //the number of executed instructions
    private static int executedInstructions;
    //the index should be replaced
    private static int index;
    //i
    private static int count;
    //the list of memory which contains 4 parts
    private static int [] memory = new int[4];
    //false:memory is empty, true:memory is full
    private static boolean [] bitmap = new boolean[4];
    //unused time
    private static int [] time = new int[4];
    //the array contains the executing order of instructions
    private static int [] instructions = new int[320];
    //format
    DecimalFormat df = new DecimalFormat("#.00");

    public void executeInstructions()  {
            executedInstructions += 1;

            currentInstruction = instructions[count++];
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
                    index = getFirstEmptyIndex();
                    memory[index] = currentPage;
                    bitmap[index] = true;
                    time[index] = 0;
                    for (int j = 0; j < 4; j++) {
                        if (j != index)
                            time[j] += 1;
                    }
                }
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

    public int [] getMemory() {
        return memory;
    }

    public int getIndex() {
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

//    public static void main(String[] args) throws InterruptedException {
//        System.out.println(currentInstruction);
//        System.out.println(missPageCount);
//        System.out.println(executedInstructions);
//    }
}
