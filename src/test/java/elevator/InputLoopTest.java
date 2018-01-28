package elevator;

import static elevator.Main.elevatorQueue;
import static elevator.Main.porchQueue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class InputLoopTest {

    @Test
    void floorsNumberTest() {
        InputLoop inputLoop = new InputLoop(new Messenger());
        inputLoop.setFloorsNumber(10);
        assertEquals(Integer.valueOf(10), inputLoop.getFloorsNumber());
    }

    @Test
    void inputStateTest() {
        InputLoop inputLoop = new InputLoop(new Messenger());
        inputLoop.setInputState(true);
        assertEquals(true, inputLoop.getInputState());
    }

    @Test
    void printConfirmTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        InputLoop inputLoop = new InputLoop(messenger);
        inputLoop.printConfirm();
        verify(messenger, times(1)).printMessage("Некорректный ввод\n");

        Messenger messengerTrue = mock(Messenger.class);
        doNothing().when(messengerTrue).printMessage(any());
        InputLoop inputLoopTrue = new InputLoop(messengerTrue);
        inputLoopTrue.setInputState(true);
        inputLoopTrue.printConfirm();
        verify(messengerTrue, times(1)).printMessage("Ввод принят\n");
    }

    @Test
    void getElevatorQueueTest() {
        InputLoop inputLoop = new InputLoop(new Messenger());
        assertEquals(elevatorQueue, inputLoop.getElevatorQueue());
    }

    @Test
    void getPorchQueueTest() {
        InputLoop inputLoop = new InputLoop(new Messenger());
        assertEquals(porchQueue, inputLoop.getPorchQueue());
    }

    @Test
    void getScannerTest() {
        InputLoop inputLoop = new InputLoop(new Messenger());
        assertTrue(inputLoop.getScanner() != null);
    }

    @Test
    void getMessengerTest() {
        Messenger messenger = new Messenger();
        InputLoop inputLoop = new InputLoop(messenger);
        assertEquals(messenger, inputLoop.getMessenger());
    }

    @Test
    void parseInputTest() {

        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());

        InputLoop inputLoop = new InputLoop(messenger);
        inputLoop.parseInput("э5");
        assertEquals(Integer.valueOf(1), (Integer)inputLoop.getPorchQueue().size());
        try {
            assertEquals(Integer.valueOf(5), (Integer) inputLoop.getPorchQueue().take());
        } catch (InterruptedException e){
            System.out.print("Exception when getting value from porchQueue");
        }
        verify(messenger, times(1)).printMessage("Ввод принят\n");

        inputLoop.parseInput("л3");
        assertEquals(Integer.valueOf(1), (Integer)inputLoop.getElevatorQueue().size());
        try {
            assertEquals(Integer.valueOf(3), (Integer) inputLoop.getElevatorQueue().take());
        } catch (InterruptedException e){
            System.out.print("Exception when getting value from elevatorQueue");
        }
        verify(messenger, times(2)).printMessage("Ввод принят\n");

        inputLoop.parseInput("5");
        verify(messenger, times(1)).printMessage("Некорректный ввод\n");

        inputLoop.parseInput("л35");
        verify(messenger, times(2)).printMessage("Некорректный ввод\n");
    }
}