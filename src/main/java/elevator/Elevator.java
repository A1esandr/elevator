package elevator;

import java.util.concurrent.LinkedBlockingQueue;

import static elevator.Main.elevatorQueue;
import static elevator.Main.porchQueue;

class Elevator implements Runnable {

    private Float elevatorSpeed = 1F;
    private Float floorHeight = 3F;
    private Integer doorsTime = 5;
    private Integer floorsNumber = 5;
    private Integer currentFloor = 1;
    private Boolean busy = false;
    private Messenger messenger;

    Elevator(Messenger messenger) {
        this.messenger = messenger;
    }

    /**
     * Set speed of elevator's moving (meters per second)
     * @param speed - speed of elevator's moving
     */
    protected void setElevatorSpeed(Float speed){
        this.elevatorSpeed = speed;
    }

    /**
     * Get speed of elevator's moving (meters per second)
     * @return - speed of elevator's moving
     */
    protected Float getElevatorSpeed(){
        return this.elevatorSpeed;
    }

    /**
     * Set height of floor in building (meters)
     * @param height - height of floor in building (meters)
     */
    protected void setFloorHeight(Float height){
        this.floorHeight = height;
    }

    /**
     * Get height of floor in building (meters)
     * @return height of floor in building (meters)
     */
    protected Float getFloorHeight(){
        return this.floorHeight;
    }

    /**
     * Set time of cycle open-close for elevator's doors (seconds)
     * @param time - time of cycle open-close for elevator's doors (seconds)
     */
    protected void setDoorsTime(Integer time){
        this.doorsTime = time;
    }

    /**
     * Get time of cycle open-close for elevator's doors (seconds)
     * @return time of cycle open-close for elevator's doors (seconds)
     */
    protected Integer getDoorsTime(){
        return this.doorsTime;
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
     * Set current floor of elevator
     * @param floor current floor of elevator
     */
    protected void setCurrentFloor(Integer floor){
        this.currentFloor = floor;
    }

    /**
     * Get current floor of elevator
     * @return current floor of elevator
     */
    protected Integer getCurrentFloor() {
        return  this.currentFloor;
    }

    /**
     * Set state of elevator
     * @param busy state of elevator: true - busy, false - free
     */
    protected void setBusy(Boolean busy){
        this.busy = busy;
    }

    /**
     * Get state of elevator
     * @return state of elevator: true - busy, false - free
     */
    protected Boolean getBusy() {
        return this.busy;
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
     * Return instance of Messenger
     * @return instance of Messenger
     */
    protected Messenger getMessenger() {
        return this.messenger;
    }

    /**
     * Entry point for start of work
     * Periodical checking of queues and sending commands to elevator
     */
    public void run() {
        try {
            while (true) {
                Thread.sleep(10);
                this.checkQueueAndMoveElevator();
            }
        } catch (InterruptedException e) {
            this.getMessenger().printMessage("Ошибка в работе лифта");
        }
    }

    /**
     * Checking of queues and sending commands to elevator
     */
    protected void checkQueueAndMoveElevator() {
        try {
            if(!this.getBusy()){
                if(!this.getElevatorQueue().isEmpty()){
                    Integer target = (int)this.getElevatorQueue().take();
                    this.move(target);
                } else if(!this.getPorchQueue().isEmpty()){
                    Integer target = (int)this.getPorchQueue().take();
                    this.move(target);
                }
            }
        } catch (InterruptedException e) {
            this.getMessenger().printMessage("Ошибка в работе лифта");
        }
    }

    /**
     * Move elevator to target floor, command to doors
     * @param targetFloor - target floor
     */
    protected void move(Integer targetFloor) {
        this.setBusy(true);
        if (!targetFloor.equals(this.getCurrentFloor())){
            this.elevatorMoving(this.getNumberOfFloorsToPass(targetFloor), this.getTimePerFloor());
        }
        this.openCloseDoors();
        this.setBusy(false);
    }

    /**
     * Compute time needed for elevator to pass one floor (milliseconds)
     * @return time needed for elevator to pass one floor (milliseconds)
     */
    protected Long getTimePerFloor() {
        Long result = 0L;
        if(this.getElevatorSpeed() > 0){
            result = (long)(this.getFloorHeight()/this.getElevatorSpeed() * 1000);
        }
        return result;
    }

    /**
     * Compute number of floors elevator needs to pass
     * @param targetFloor - floor number where elevator must be
     * @return difference between current and target position of elevator (it may be negative number)
     */
    protected Integer getNumberOfFloorsToPass(Integer targetFloor){
        return targetFloor - this.getCurrentFloor();
    }

    /**
     * Moving of elevator, printing state of moving to console
     * @param floors number of floors to pass (if negative - means elevator need to go down)
     * @param floorTime time needed for elevator to pass one floor (milliseconds)
     */
    protected void elevatorMoving(Integer floors, Long floorTime) {
        Messenger messenger = this.getMessenger();
        String direction = this.getDirection(floors);
        Integer floorsToMove = this.getFloorsCountToMove(floors);
        try {
            for (Integer fl = 0; fl < floorsToMove; fl++){
                if(!(direction.equals("down") && fl == 0)){
                    messenger.printFormattedMessage("Лифт проезжает %s этаж%n", this.getCurrentFloor().toString());
                }

                Thread.sleep(floorTime);
                this.changeCurrentFloor(direction);

                if (fl == (floorsToMove-1)){
                    messenger.printFormattedMessage("Лифт на %s этаже%n", this.getCurrentFloor().toString());
                }
            }
        } catch (InterruptedException e) {
            messenger.printMessage("Ошибка при движении лифта");
        }
    }

    /**
     * Get direction for elevator moving
     * @param floorsNumber - number of floors to pass (if negative - means elevator need to go down)
     * @return direction for elevator moving: up or down
     */
    protected String getDirection(Integer floorsNumber) {
        return floorsNumber > 0 ? "up" : "down";
    }

    /**
     * Get count of floors to move
     * @param floors number of floors to pass (if negative - means elevator need to go down)
     * @return count of floors to move (not negative number)
     */
    protected Integer getFloorsCountToMove(Integer floors) {
        return floors > 0 ? floors : -floors;
    }

    /**
     * Change current position of elevator
     * @param direction of moving - up or down
     */
    protected void changeCurrentFloor(String direction) {
        if(direction.equals("up")){
            if (this.getCurrentFloor() < this.getFloorsNumber()){
                this.setCurrentFloor(this.getCurrentFloor()+1);
            }
        } else if(this.getCurrentFloor() > 1) {
            this.setCurrentFloor(this.getCurrentFloor()-1);
        }
    }

    /**
     * Prints doors moving to console
     */
    protected void openCloseDoors() {
        Messenger messenger = this.getMessenger();
        try {
            messenger.printMessage("Лифт открыл двери\n");
            Thread.sleep(this.getDoorsTime()*1000);
            messenger.printMessage("Лифт закрыл двери\n");
        } catch (InterruptedException e) {
            messenger.printMessage("Ошибка в ходе работы дверей лифта");
        }
    }
}