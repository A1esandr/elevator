package elevator;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import static elevator.Main.elevatorQueue;
import static elevator.Main.porchQueue;

import static java.lang.Integer.parseInt;

/**
 * Class for getting user input
 */
class InputLoop implements Runnable {

    private Integer floorsNumber = 5;
    private Boolean inputState = false;
    private Messenger messenger;

    InputLoop(Messenger messenger) {
        this.messenger = messenger;
    }

    /**
     * Set number of floors in building
     * @param number of floors in building
     */
    protected void setFloorsNumber(Integer number){
        this.floorsNumber = number;
    }

    /**
     * Return number of floors in building
     * @return number of floors in building
     */
    protected Integer getFloorsNumber(){
        return this.floorsNumber;
    }

    /**
     * Set inputState - flag for correctness check of user input
     * @param state flag for correctness check of user input
     */
    protected void setInputState(Boolean state) {
        this.inputState = state;
    }

    /**
     * Return inputState - flag for correctness check of user input
     * @return flag for correctness check of user input
     */
    protected Boolean getInputState() {
        return this.inputState;
    }

    /**
     * Return instance of Scanner for getting user's input from console
     * @return instance of Scanner
     */
    protected Scanner getScanner() {
        return new Scanner(System.in);
    }

    /**
     * Return instance of Messenger
     * @return instance of Messenger
     */
    protected Messenger getMessenger() {
        return this.messenger;
    }

    /**
     * Print message to console by inputState
     */
    protected void printConfirm() {
        Messenger messengerInst = this.getMessenger();
        if(this.getInputState()){
            messengerInst.printMessage("Ввод принят\n");
        } else {
            messengerInst.printMessage("Некорректный ввод\n");
        }
    }

    /**
     * Return elevatorQueue - queue of inputs from elevator cabin
     * @return elevatorQueue - queue of inputs from elevator cabin
     */
    protected LinkedBlockingQueue getElevatorQueue() {
        return elevatorQueue;
    }

    /**
     * Return porchQueue - queue of inputs from porch
     * @return porchQueue - queue of inputs from porch
     */
    protected LinkedBlockingQueue getPorchQueue() {
        return porchQueue;
    }

    /**
     * Get user input and analyze it, writing results to appropriate queue and call printing response to console
     * @param enter user input
     */
    protected void parseInput(String enter) {
        this.setInputState(false);
        try {
            if(enter.indexOf("э") == 0){
                enter = enter.replace("э", "");
                if(parseInt(enter) > 0 && parseInt(enter) <= this.getFloorsNumber()){
                    this.getPorchQueue().add(parseInt(enter));
                    this.setInputState(true);
                }
            } else if (enter.indexOf("л") == 0){
                enter = enter.replace("л", "");
                if(parseInt(enter) > 0 && parseInt(enter) <= this.getFloorsNumber()){
                    this.getElevatorQueue().add(parseInt(enter));
                    this.setInputState(true);
                }
            }
        } catch (NumberFormatException e) {
            this.setInputState(false);
        }

        this.printConfirm();
    }

    /**
     * Entry point for start of work
     * Getting input from console and passing it to parser
     */
    public void run() {
        Scanner scanner = this.getScanner();
        this.getMessenger().printMessage("Ввод: \n");
        while (true) {
            String enter = scanner.next();
            this.parseInput(enter);
        }
    }
}
