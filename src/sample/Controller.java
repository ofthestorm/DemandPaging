package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


public class Controller {



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

    private Map<String,Text> map = new HashMap<String,Text>();

    @FXML private Text memory1;
    @FXML private Text memory2;
    @FXML private Text memory3;
    @FXML private Text memory4;
    @FXML private Text memory5;
    @FXML private Text memory6;
    @FXML private Text memory7;
    @FXML private Text memory8;
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

        this.setVisible(false);

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

        ins.generateInstructions();
        fifo = new FIFO(ins);
        lru = new LRU(ins);

//        for (int i = 0; i < 320; i++) {
//            System.out.print(ins.getInstructions()[i]+" " );
//        }
//        System.out.println("\n");
//        for (int i = 0; i < 320; i++) {
//            System.out.print(fifo.getInstructions()[i]+" " );
//        }

    }


    @FXML
    private void resetButtonOnMouseClicked() {
        startButton.setText("Start");
        button = buttons.start;
        executionModeChoice.setDisable(false);
        this.setVisible(false);

    }

    @FXML
    private void startButtonOnMouseClicked() {

        if (choice == choices.singleStep) {
            executionModeChoice.setDisable(true);
            if (button == buttons.start) {
                startButton.setText("Next");
//                System.out.println("s");
                button = buttons.next;
                this.setMemory();
                this.setVisible(true);

            } else { //next
                //执行

//                System.out.println("n");
            }
        } else if (choice == choices.continuousExecution) {
            executionModeChoice.setDisable(true);
            this.setMemory();
            this.setVisible(true);

            //执行
            //sleep(1000)
        } else {

        }
    }

    private void setVisible(boolean isVisible) {
        memory1.setVisible(isVisible);
        memory2.setVisible(isVisible);
        memory3.setVisible(isVisible);
        memory4.setVisible(isVisible);
        memory5.setVisible(isVisible);
        memory6.setVisible(isVisible);
        memory7.setVisible(isVisible);
        memory8.setVisible(isVisible);
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

    private void continuousExecute(){
        try {
            fifo.executeInstructions();
            lru.executeInstructions();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
