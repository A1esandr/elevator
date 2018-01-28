package elevator;

import static elevator.Main.elevatorQueue;
import static elevator.Main.porchQueue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class ElevatorTest {
    @Test
    void elevatorSpeedTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setElevatorSpeed(1.5F);
        assertEquals(Float.valueOf(1.5F), elevator.getElevatorSpeed());
    }

    @Test
    void floorsNumberTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setFloorsNumber(10);
        assertEquals(Integer.valueOf(10), elevator.getFloorsNumber());
    }

    @Test
    void floorHeightTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setFloorHeight(2.5F);
        assertEquals(Float.valueOf(2.5F), elevator.getFloorHeight());
    }

    @Test
    void doorsTimeTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setDoorsTime(8);
        assertEquals(Integer.valueOf(8), elevator.getDoorsTime());
    }

    @Test
    void currentFloorTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setCurrentFloor(7);
        assertEquals(Integer.valueOf(7), elevator.getCurrentFloor());
    }

    @Test
    void busyTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setBusy(true);
        assertEquals(true, elevator.getBusy());
    }

    @Test
    void openCloseDoorsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        Elevator elevator = new Elevator(messenger);
        elevator.setDoorsTime(1);
        elevator.openCloseDoors();
        verify(messenger, times(1)).printMessage("Лифт открыл двери\n");
        verify(messenger, times(1)).printMessage("Лифт закрыл двери\n");
    }

    @Test
    void getElevatorQueueTest() {
        Elevator elevator = new Elevator(new Messenger());
        assertEquals(elevatorQueue, elevator.getElevatorQueue());
    }

    @Test
    void getPorchQueueTest() {
        Elevator elevator = new Elevator(new Messenger());
        assertEquals(porchQueue, elevator.getPorchQueue());
    }

    @Test
    void getMessengerTest() {
        Messenger messenger = new Messenger();
        Elevator elevator = new Elevator(messenger);
        assertEquals(messenger, elevator.getMessenger());
    }

    @Test
    void checkQueueAndMoveElevatorTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        Elevator elevator = new Elevator(messenger);

        // set params to speed test
        elevator.setDoorsTime(1);
        elevator.setFloorHeight(2F);
        elevator.setElevatorSpeed(10F);

        elevator.getElevatorQueue().add(3);
        elevator.checkQueueAndMoveElevator();
        // check that elevator open-close doors - means elevator execute move method
        verify(messenger, times(1)).printMessage("Лифт открыл двери\n");
        verify(messenger, times(1)).printMessage("Лифт закрыл двери\n");

        elevator.getPorchQueue().add(5);
        elevator.checkQueueAndMoveElevator();
        // check that elevator open-close doors - means elevator execute move method
        verify(messenger, times(2)).printMessage("Лифт открыл двери\n");
        verify(messenger, times(2)).printMessage("Лифт закрыл двери\n");
    }

    @Test
    void getTimePerFloorTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setFloorHeight(2F);
        elevator.setElevatorSpeed(1F);
        assertEquals(Long.valueOf(2000), elevator.getTimePerFloor());
    }

    @Test
    void getNumberOfFloorsToPassTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setCurrentFloor(1);
        assertEquals(Integer.valueOf(4), elevator.getNumberOfFloorsToPass(5));
    }

    @Test
    void moveTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printFormattedMessage(any(), any());
        Elevator elevator = new Elevator(messenger);
        elevator.setCurrentFloor(1);

        // set params to speed test
        elevator.setDoorsTime(1);
        elevator.setFloorHeight(2F);
        elevator.setElevatorSpeed(10F);

        elevator.move(3);
        // check that elevator pass floor - means elevator execute elevatorMoving method
        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "1");
    }

    @Test
    void getDirectionTest() {
        Elevator elevator = new Elevator(new Messenger());
        assertEquals("up", elevator.getDirection(5));
        assertEquals("down", elevator.getDirection(-5));
    }

    @Test
    void getFloorsCountToMoveTest() {
        Elevator elevator = new Elevator(new Messenger());
        assertEquals(Integer.valueOf(5), elevator.getFloorsCountToMove(5));
        assertEquals(Integer.valueOf(5), elevator.getFloorsCountToMove(-5));
        assertEquals(Integer.valueOf(0), elevator.getFloorsCountToMove(0));
    }

    @Test
    void changeCurrentFloorTest() {
        Elevator elevator = new Elevator(new Messenger());
        elevator.setCurrentFloor(1);
        elevator.changeCurrentFloor("up");
        assertEquals(Integer.valueOf(2), elevator.getCurrentFloor());

        elevator.setFloorsNumber(5);
        elevator.setCurrentFloor(5);
        elevator.changeCurrentFloor("up");
        assertEquals(Integer.valueOf(5), elevator.getCurrentFloor());

        elevator.setCurrentFloor(4);
        elevator.changeCurrentFloor("down");
        assertEquals(Integer.valueOf(3), elevator.getCurrentFloor());

        elevator.setCurrentFloor(1);
        elevator.changeCurrentFloor("down");
        assertEquals(Integer.valueOf(1), elevator.getCurrentFloor());
    }

    @Test
    void elevatorMovingTest() {

        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printFormattedMessage(any(), any());

        Elevator elevator = new Elevator(messenger);
        elevator.setFloorsNumber(10);
        elevator.setCurrentFloor(1);
        elevator.elevatorMoving(3,1L);

        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "1");
        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "2");
        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "3");
        verify(messenger, times(1)).printFormattedMessage("Лифт на %s этаже%n", "4");

        elevator.setCurrentFloor(9);
        elevator.elevatorMoving(-3,1L);

        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "8");
        verify(messenger, times(1)).printFormattedMessage("Лифт проезжает %s этаж%n", "7");
        verify(messenger, times(1)).printFormattedMessage("Лифт на %s этаже%n", "6");
    }
}
