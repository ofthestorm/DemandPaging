package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.HashMap;
import java.util.Map;


public class Controller {

    UpdateThread updateThread = new UpdateThread(this);

    Thread thread = new Thread(updateThread, "Chef     1");


    private enum choices {
        singleStep,
        continuousExecution,
        none
    }
    private choices choice = choices.none;

    private enum buttons {
        start,
        next
    }
    private buttons button = buttons.start;

    private Instruction ins = new Instruction();
    private FIFO fifo;
    private LRU lru;

    private int [] memory = new int[8];
//    private static int executedInstructions = 0;

    private Map<Integer,Text> map = new HashMap<Integer,Text>();



    @FXML private Text memory0;
    @FXML private Text memory1;
    @FXML private Text memory2;
    @FXML private Text memory3;
    @FXML private Text memory4;
    @FXML private Text memory5;
    @FXML private Text memory6;
    @FXML private Text memory7;

    @FXML private Text FIFOCurrentInstruction;
    @FXML private Text FIFOExecutedInstruction;
    @FXML private Text FIFORateOfMissingPage;
    @FXML private Text LRUCurrentInstruction;
    @FXML private Text LRUExecutedInstruction;
    @FXML private Text LRURateOfMissingPage;
    @FXML private Button startButton;
    @FXML private ChoiceBox executionModeChoice;

//    private Integer currentChoice = new Integer();

    public void initialize() {
        init();
        initMap();
        executionModeChoice.getItems().addAll(
                "Single step",
                "Continuous execution"
        );
        executionModeChoice.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNum, Number newNum) {
                if(executionModeChoice.getItems().get((Integer) newNum) == "Single step" ) {
                    choice = choices.singleStep;
                } else if (executionModeChoice.getItems().get((Integer) newNum) == "Continuous execution") {
                    choice = choices.continuousExecution;
                } else {
                    choice = choices.none;
                }
            }
        });

    }

    private void init() {

        this.setVisible(false);

        ins.generateInstructions();
        fifo = new FIFO(ins);
        lru = new LRU(ins);

    }


    @FXML
    private void resetButtonOnMouseClicked() {
        if(choice == choices.continuousExecution) {
            updateThread.setExit(true);
        }
        startButton.setDisable(false);
        startButton.setText("Start");
        button = buttons.start;
        executionModeChoice.setDisable(false);
        this.setVisible(false);
        this.init();
    }

    @FXML
    private void startButtonOnMouseClicked() {

        if (choice == choices.singleStep) {
            executionModeChoice.setDisable(true);
            if (button == buttons.start) {
                startButton.setText("Next");
//                System.out.println("s");
                button = buttons.next;
                singleStep();
                this.setMemory();
                this.setVisible(true);

            } else { //next
                //执行
                if(fifo.getExecutedInstructions() != 320) {
                    singleStep();
                } else {
                    startButton.setDisable(true);
                }
//                System.out.println("n");
            }
        } else if (choice == choices.continuousExecution) {
            executionModeChoice.setDisable(true);
//            this.setMemory();
            this.setVisible(true);
//            continuousExecute();

            thread.start();
            //执行
            //sleep(1000)
        } else {

        }
    }

    private void setVisible(boolean isVisible) {
        memory0.setVisible(isVisible);
        memory1.setVisible(isVisible);
        memory2.setVisible(isVisible);
        memory3.setVisible(isVisible);
        memory4.setVisible(isVisible);
        memory5.setVisible(isVisible);
        memory6.setVisible(isVisible);
        memory7.setVisible(isVisible);
        FIFOCurrentInstruction.setVisible(isVisible);
        FIFOExecutedInstruction.setVisible(isVisible);
        FIFORateOfMissingPage.setVisible(isVisible);
        LRUCurrentInstruction.setVisible(isVisible);
        LRUExecutedInstruction.setVisible(isVisible);
        LRURateOfMissingPage.setVisible(isVisible);
    }

    private void setMemory() {
            System.arraycopy(fifo.getMemory(),0,memory,0,4);
            System.arraycopy(lru.getMemory(),0,memory,4,4);
//        for (int i = 0; i < 8; i++) {
//            if(i < 4)
//                memory[i] = fifo.getMemory()[i];
//            else
//                memory[i] = lru.getMemory()[i-4];
//        }
    }

    public void continuousExecute(){
        try {
//            while(fifo.getExecutedInstructions() != 320){
                fifo.executeInstructions();
                lru.executeInstructions();
                this.setMemory();
                this.setVisible(true);
                for (int i = 0; i < 8; i++) {
                    System.out.print(memory[i]+" ");
                }
                this.update();
                System.out.println("\n");

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void singleStep() {

        try {
            fifo.executeInstructions();
            lru.executeInstructions();
            this.setMemory();
            this.update();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        int fifoIndex = fifo.getIndex();
        int lruIndex = lru.getIndex() + 4;
//        System.out.println(fifoIndex+" " +lruIndex);

        //change memory and color
        for (int i = 0; i < 8; i++) {
            map.get(i).setText(Integer.toString(memory[i]));

            if(i == fifoIndex) {
                map.get(i).setFill(Color.rgb(107,142,35));
//                System.out.println(i+" f ");
            } else if(i == lruIndex) {
                map.get(i).setFill(Color.rgb(107,142,35));
//                System.out.println(i+ "l ");
            } else {
                map.get(i).setFill(Color.BLACK);
            }
        }

        //change number
        FIFOCurrentInstruction.setText(Integer.toString(fifo.getCurrentInstruction()));
        FIFOExecutedInstruction.setText(Integer.toString(fifo.getExecutedInstructions()));
        FIFORateOfMissingPage.setText(fifo.getRateOfMissingPage());
        LRUCurrentInstruction.setText(Integer.toString(lru.getCurrentInstruction()));
        LRUExecutedInstruction.setText(Integer.toString(lru.getExecutedInstructions()));
        LRURateOfMissingPage.setText(lru.getRateOfMissingPage());

    }

    private void initMap() {
        map.put(0,memory0);
        map.put(1,memory1);
        map.put(2,memory2);
        map.put(3,memory3);
        map.put(4,memory4);
        map.put(5,memory5);
        map.put(6,memory6);
        map.put(7,memory7);
    }
}

class UpdateThread implements Runnable {
    private Controller controller;

    public UpdateThread(Controller controller) {
        this.controller = controller;
    }

    private boolean exit = false;

    public void setExit(boolean flag) {
        exit = flag;
    }

    @Override
    public void run() {
        int i = 0;

        while (!exit && i != 320) {
            try {
                controller.continuousExecute();
                i++;
                System.out.println("thread\n");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}