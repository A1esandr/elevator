package elevator;

import static elevator.Main.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

class MainTest {
    @Test
    void printAvailableActionsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        printAvailableActions(messenger);

        verify(messenger, times(1)).printMessage("Доступные действия:\n");
        verify(messenger, times(1)).printMessage("Вызов лифта из подъезда с этажа - введите 'э+номер_этажа' и нажмите Enter (Пример: э12)\n");
        verify(messenger, times(1)).printMessage("Выбор этажа из кабины лифта - введите 'л+номер_этажа' и нажмите Enter (Пример: л21)\n");
    }

    @Test
    void printDefaultSettingsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        Elevator elevator = new Elevator(messenger);
        InputLoop inputLoop = new InputLoop(messenger);
        printDefaultSettings(messenger, inputLoop, elevator);

        verify(messenger, times(1)).printMessage("Аргументы не заданы\n");
        verify(messenger, times(1)).printMessage("Приложение запущено с аргументами по умолчанию:\n");
    }

    @Test
    void printActualSettingsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        Elevator elevator = new Elevator(messenger);
        InputLoop inputLoop = new InputLoop(messenger);
        printActualSettings(messenger, inputLoop, elevator);

        verify(messenger, times(1)).printMessage("Приложение запущено со следующими параметрами:\n");
    }

    @Test
    void printSettingsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printFormattedMessage(any(), any());
        Elevator elevator = new Elevator(messenger);
        InputLoop inputLoop = new InputLoop(messenger);

        inputLoop.setFloorsNumber(11);
        elevator.setElevatorSpeed(3.5F);
        elevator.setFloorHeight(3.8F);
        elevator.setDoorsTime(12);

        printSettings(messenger, inputLoop, elevator);

        verify(messenger, times(1)).printFormattedMessage("Количество этажей в здании - %s\n", "11");
        verify(messenger, times(1)).printFormattedMessage("Скорость движения лифта - %s м/с\n", "3.5");
        verify(messenger, times(1)).printFormattedMessage("Высота этажа в здании - %s метра\n", "3.8");
        verify(messenger, times(1)).printFormattedMessage("Время между открытием и закрытием дверей лифта - %s секунд\n", "12");
    }

    @Test
    void parseArgsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printMessage(any());
        Elevator elevator = new Elevator(messenger);
        InputLoop inputLoop = new InputLoop(messenger);
        String[] args = new String[0];
        parseArgs(messenger, args, inputLoop, elevator);

        verify(messenger, times(1)).printMessage("Приложение запущено с аргументами по умолчанию:\n");

        String[] notEmptyArgs = {"5","1","1","1"};
        parseArgs(messenger, notEmptyArgs, inputLoop, elevator);

        verify(messenger, times(1)).printMessage("Приложение запущено со следующими параметрами:\n");
    }

    @Test
    void checkArgsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printErrorMessage(any());
        Exiter exiter = mock(Exiter.class);
        doNothing().when(exiter).exit();

        String[] args = new String[0];
        checkArgs(messenger, exiter, args);

        verify(messenger, times(1)).printErrorMessage("Должно быть 4 аргумента");
        verify(exiter, times(1)).exit();

        String[] notEmptyArgs = {"fake","1","1","1"};
        checkArgs(messenger, exiter, notEmptyArgs);

        verify(messenger, times(1)).printErrorMessage("Аргументы должны быть числами");
        verify(exiter, times(2)).exit();

        String[] otherNotEmptyArgs = {"-5","1","1","1"};
        checkArgs(messenger, exiter, otherNotEmptyArgs);

        verify(messenger, times(1)).printErrorMessage("Аргументы должны быть неотрицательными числами больше нуля");
        verify(exiter, times(3)).exit();

        String[] otherArgs = {"0","1","1","1"};
        checkArgs(messenger, exiter, otherArgs);

        verify(messenger, times(2)).printErrorMessage("Аргументы должны быть неотрицательными числами больше нуля");
        verify(exiter, times(4)).exit();
    }

    @Test
    void parseFloorsNumberParamTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printErrorMessage(any());
        Exiter exiter = mock(Exiter.class);
        doNothing().when(exiter).exit();
        Elevator elevator = new Elevator(messenger);
        InputLoop inputLoop = new InputLoop(messenger);

        String[] args = {"0.1","1","1","1"};
        parseFloorsNumberParam(messenger, exiter, inputLoop, elevator, args);

        verify(messenger, times(1)).printErrorMessage("Количество этажей должно быть целым числом");
        verify(exiter, times(1)).exit();

        String[] otherArgs = {"4","1","1","1"};
        parseFloorsNumberParam(messenger, exiter, inputLoop, elevator, otherArgs);

        verify(messenger, times(1)).printErrorMessage("Количество этажей должно быть от 5 до 20");
        verify(exiter, times(2)).exit();

        String[] oneMoreArgs = {"40","1","1","1"};
        parseFloorsNumberParam(messenger, exiter, inputLoop, elevator, oneMoreArgs);

        verify(messenger, times(2)).printErrorMessage("Количество этажей должно быть от 5 до 20");
        verify(exiter, times(3)).exit();

        String[] normalArgs = {"12","1","1","1"};
        parseFloorsNumberParam(messenger, exiter, inputLoop, elevator, normalArgs);

        assertEquals(Integer.valueOf(12), inputLoop.getFloorsNumber());
        assertEquals(Integer.valueOf(12), elevator.getFloorsNumber());
    }

    @Test
    void parseElevatorParamsTest() {
        Messenger messenger = mock(Messenger.class);
        doNothing().when(messenger).printErrorMessage(any());
        Exiter exiter = mock(Exiter.class);
        doNothing().when(exiter).exit();
        Elevator elevator = new Elevator(messenger);

        String[] args = {"5","fake","1","1"};
        parseElevatorParams(messenger, exiter, elevator, args);

        verify(messenger, times(1)).printErrorMessage("Скорость движения лифта должна быть числом");
        verify(exiter, times(1)).exit();

        String[] otherArgs = {"5","1","fake","1"};
        parseElevatorParams(messenger, exiter, elevator, otherArgs);

        verify(messenger, times(1)).printErrorMessage("Высота этажа в здании должна быть числом");
        verify(exiter, times(2)).exit();

        String[] oneMoreArgs = {"5","1","1","fake"};
        parseElevatorParams(messenger, exiter, elevator, oneMoreArgs);

        verify(messenger, times(1)).printErrorMessage("Время между открытием и закрытием дверей лифта должно быть целым числом");
        verify(exiter, times(3)).exit();

        String[] normalArgs = {"5","4.5","3.2","14"};
        parseElevatorParams(messenger, exiter, elevator, normalArgs);

        assertEquals(Float.valueOf(4.5F), elevator.getElevatorSpeed());
        assertEquals(Float.valueOf(3.2F), elevator.getFloorHeight());
        assertEquals(Integer.valueOf(14), elevator.getDoorsTime());
    }
}
