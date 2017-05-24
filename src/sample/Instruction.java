package sample;

import java.util.Random;

/**
 * Created by keke on 2017/5/18.
 */
public class Instruction {

    //the array contains the executing order of instructions
    private static int [] instructions = new int[320];

    //random
    static Random random = new Random();

    //init instructions[320]
    public void generateInstructions() {
        for (int i = 0;i < 320;i++) {
            switch (i % 6) {
                case 0 : { // 0 ~ 319 -> m
                    instructions[i] = random.nextInt(319);
                }break;
                case 1 : { // m + 1
                    instructions[i] = instructions[i - 1] + 1;
                }break;
                case 2 : { // 0 ~ m - 1 -> m1
                    if(instructions[i - 1] > 2){
                        instructions[i] = random.nextInt(instructions[i - 1] - 2);
                    } else {
                        instructions[i] = random.nextInt(instructions[i - 1]);
                    }
                }break;
                case 3 : { // m1 + 1
                    instructions[i] = instructions[i - 1] + 1;
                }break;
                case 4 : { // m1 + 1 ~ 319 -> m2
                    instructions[i] = random.nextInt(319 - (instructions[i - 1] + 1)) + instructions[i - 1];
                }break;
                case 5 : { // m2 + 1
                    instructions[i] = instructions[i - 1] + 1;
                }break;
                default:
                    break;
            }
        }
    }

    public int [] getInstructions() {
        return instructions;
    }
}
