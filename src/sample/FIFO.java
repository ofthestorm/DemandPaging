package sample;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by keke on 2017/5/17.
 */

public class FIFO {

    //    public enum algorithms{
//        FIFO, // First In First Out
//        LRU   // Least Recently Used
//    }
    public FIFO(Instruction ins) {

        indexNeedChange = false;
        currentInstruction = 0;
        missPageCount = 0;
        executedInstructions = 0;
        index = 0;
        indexCopy = 0;
        i = 0;
        memory = new int[4];
        bitmap = new boolean[] {false, false, false, false};
        instructions = new int[320];
        df = new DecimalFormat("#.00");
        this.instructions = ins.getInstructions();
    }

    private boolean indexNeedChange = false;
//    private static algorithms algorithm ;
    //the order number of current instruction
    private static int currentInstruction = 0;
    //the count of missing page
    private static int missPageCount = 0;
    //the number of executed instructions
    private static int executedInstructions = 0;
    //the index should be replaced
    private static int index = 0;
    private static int indexCopy = 0;
    //
    private static int i = 0;
    //the list of memory which contains 4 parts
    //private ArrayList<Integer> memory = new ArrayList<>(4);
    private static int [] memory = new int[4];
    //false:memory is empty, true:memory is full
    private static boolean [] bitmap = new boolean[] {false,false,false,false};
    //the array contains the executing order of instructions
    private static int [] instructions = new int[320];
    //format
    DecimalFormat df = new DecimalFormat("#.00");


    //execute instructions

    public void executeInstructions() throws InterruptedException {

        //    generateInstructions();

//        if(indexNeedChange == true) {
//            index = (index + 1) % 4;
//        }
//        indexNeedChange = false;
        executedInstructions += 1;


        currentInstruction = instructions[i++];
        int currentPage = getPage(currentInstruction);
        if (isExist(currentPage)/*memory.contains(currentPage)*/) { // memory contains current pag
//            System.out.println("exist");
        } else {
            missPageCount += 1; // record missing page
            if (getEmptyPart() == 0/*memory.size() == 4*/) { // memory is already full
                memory[index] = currentPage;
                indexCopy = index;
                index = (index + 1) % 4;
//                indexNeedChange = true;
//                System.out.println("fifo");
            } else {

                //add current page
                //memory.add(currentPage);
                index = getFirstEmptyIndex();
                indexCopy = index;
                memory[index] = currentPage;
                bitmap[index] = true;
                index = (index + 1) % 4;
//                System.out.println("find empty");
            }

       //     displayMemory();

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

    public int [] getInstructions() {
        return instructions;
    }

    public int getIndex() {
//        return indexCopy;
        return getIndexOfPage(getPage(currentInstruction));
    }

    public String getRateOfMissingPage() {
//        return missPageCount + " / " + 320 + " = " + Double.toString((double)missPageCount / (double)320);
        double result = (double)missPageCount / (double)320 * 100;
        return missPageCount + " / " + 320 + " = " + df.format(result) + "%";
    }

    private static void displayMemory() {
        for (int i = 0; i < 4; i++) {
            System.out.print(memory[i]+" ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) throws InterruptedException {
    //    executeInstructions();

        System.out.println(currentInstruction);
        System.out.println(missPageCount);
        System.out.println(executedInstructions);
   //     System.out.println(getRateOfMissingPage());
    }

}