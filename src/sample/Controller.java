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

    Thread thread = new Thread(updateThread, "thead1");

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
        ins.generateInstructions();
        fifo = new FIFO(ins);
        lru = new LRU(ins);
    }


    @FXML
    private void resetButtonOnMouseClicked() {
        this.init();

        if(choice == choices.continuousExecution) {
            updateThread.setExit(true);
        }

        startButton.setDisable(false);
        startButton.setText("Start");
        button = buttons.start;
        executionModeChoice.setDisable(false);
        this.setVisible(false);
    }

    @FXML
    private void startButtonOnMouseClicked() {
//        for (int i = 0; i < 8; i++) {
//            if (memory[i] == -1) {
//                map.get(i).setVisible(false);
//            } else {
//                map.get(i).setVisible(true);
//            }
//        }
        if (choice == choices.singleStep) {
            executionModeChoice.setDisable(true);
            if (button == buttons.start) {
                startButton.setText("Next");
                button = buttons.next;
                singleStep();
                this.setMemory();
                this.setVisible(true);

            } else { //next
                if(fifo.getExecutedInstructions() != 320) {
                    singleStep();
                } else {
                    startButton.setDisable(true);
                }
            }
        } else if (choice == choices.continuousExecution) {
            updateThread = new UpdateThread(this);
            thread = new Thread(updateThread, "thead1");

            updateThread.setExit(false);
            executionModeChoice.setDisable(true);
            this.setVisible(true);

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
    }

    public void continuousExecute(){
        try {
            fifo.executeInstructions();
            lru.executeInstructions();
            this.setMemory();
            this.setVisible(true);
            this.update();

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

        //change memory and color
        for (int i = 0; i < 8; i++) {
            map.get(i).setText(Integer.toString(memory[i]));
            if(i == fifoIndex) {
                map.get(i).setFill(Color.rgb(205,85,85));
            } else if(i == lruIndex) {
                map.get(i).setFill(Color.rgb(205,85,85));
            } else {
                map.get(i).setFill(Color.BLACK);
            }

//            if (memory[i] == -1) {
//                map.get(i).setVisible(false);
//            } else {
//                map.get(i).setVisible(true);
//            }
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

        while (i != 320) {
            try {
                if(!exit) {
                    controller.continuousExecute();
                    i++;
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}