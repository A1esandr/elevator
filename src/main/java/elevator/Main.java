package elevator;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Main class for program with entry point
 */
public class Main {

    /**
     * Queue for signals from porch of building
     */
    static LinkedBlockingQueue porchQueue = new LinkedBlockingQueue();

    /**
     * Queue for signals from elevator
     */
    static LinkedBlockingQueue elevatorQueue = new LinkedBlockingQueue();

    /**
     * Print default settings of program to console
     * @param messenger instance of Messenger class for interaction with console
     * @param inputLoop instance of InputLoop class
     * @param elevator instance of Elevator class
     */
    static void printDefaultSettings(Messenger messenger, InputLoop inputLoop, Elevator elevator) {
        messenger.printMessage("Аргументы не заданы\n");
        messenger.printMessage("Приложение запущено с аргументами по умолчанию:\n");
        printSettings(messenger, inputLoop, elevator);
    }

    /**
     * Print actual settings of program to console
     * @param messenger instance of Messenger class for interaction with console
     * @param inputLoop instance of InputLoop class
     * @param elevator instance of Elevator class
     */
    static void printActualSettings(Messenger messenger, InputLoop inputLoop, Elevator elevator) {
        messenger.printMessage("Приложение запущено со следующими параметрами:\n");
        printSettings(messenger, inputLoop, elevator);
    }

    /**
     * Print settings of program to console
     * @param messenger instance of Messenger class for interaction with console
     * @param inputLoop instance of InputLoop class
     * @param elevator instance of Elevator class
     */
    static void printSettings(Messenger messenger, InputLoop inputLoop, Elevator elevator) {
        messenger.printFormattedMessage("Количество этажей в здании - %s\n", inputLoop.getFloorsNumber().toString());
        messenger.printFormattedMessage("Скорость движения лифта - %s м/с\n", elevator.getElevatorSpeed().toString());
        messenger.printFormattedMessage("Высота этажа в здании - %s метра\n", elevator.getFloorHeight().toString());
        messenger.printFormattedMessage("Время между открытием и закрытием дверей лифта - %s секунд\n", elevator.getDoorsTime().toString());
    }

    /**
     * Print available actions for program
     * @param messenger instance of Messenger class for interaction with console
     */
    static void printAvailableActions(Messenger messenger) {
        messenger.printMessage("Доступные действия:\n");
        messenger.printMessage("Вызов лифта из подъезда с этажа - введите 'э+номер_этажа' и нажмите Enter (Пример: э12)\n");
        messenger.printMessage("Выбор этажа из кабины лифта - введите 'л+номер_этажа' и нажмите Enter (Пример: л21)\n");
    }

    /**
     * Parse given args and print setting of program
     * @param messenger instance of Messenger class for interaction with console
     * @param args arguments for program
     * @param inputLoop instance of InputLoop class
     * @param elevator instance of Elevator class
     */
    static void parseArgs(Messenger messenger, String[] args, InputLoop inputLoop, Elevator elevator) {
        if (args.length > 0) {
            Exiter exiter = new Exiter();
            checkArgs(messenger, exiter, args);
            parseFloorsNumberParam(messenger, exiter, inputLoop, elevator, args);
            parseElevatorParams(messenger, exiter, elevator, args);
            printActualSettings(messenger, inputLoop, elevator);
        } else {
            printDefaultSettings(messenger, inputLoop, elevator);
        }
    }

    /**
     * Check args is actually numbers and there are four entries in args
     * @param messenger instance of Messenger class for interaction with console
     * @param exiter instance of Exiter to terminate program
     * @param args arguments for program
     */
    static void checkArgs(Messenger messenger, Exiter exiter, String[] args) {
        if (args.length != 4) {
            messenger.printErrorMessage("Должно быть 4 аргумента");
            exiter.exit();
        }
        try {
            for (String arg : args){
                if (Float.parseFloat(arg) <= 0){
                    messenger.printErrorMessage("Аргументы должны быть неотрицательными числами больше нуля");
                    exiter.exit();
                }
            }
        } catch (NumberFormatException e) {
            messenger.printErrorMessage("Аргументы должны быть числами");
            exiter.exit();
        }
    }

    /**
     * Parse floorsNumber param from args
     * @param messenger instance of Messenger class for interaction with console
     * @param exiter instance of Exiter to terminate program
     * @param inputLoop instance of InputLoop class
     * @param elevator instance of Elevator class
     * @param args arguments for program
     */
    static void parseFloorsNumberParam(Messenger messenger, Exiter exiter, InputLoop inputLoop, Elevator elevator, String[] args) {
        try {
            Integer number = Integer.parseInt(args[0]);
            if (number > 20 || number < 5){
                messenger.printErrorMessage("Количество этажей должно быть от 5 до 20");
                exiter.exit();
            }
            inputLoop.setFloorsNumber(number);
            elevator.setFloorsNumber(number);
        } catch (NumberFormatException e) {
            messenger.printErrorMessage("Количество этажей должно быть целым числом");
            exiter.exit();
        }
    }

    /**
     * Parse elevator's params from args
     * @param messenger instance of Messenger class for interaction with console
     * @param exiter instance of Exiter to terminate program
     * @param elevator instance of Elevator class
     * @param args arguments for program
     */
    static void parseElevatorParams(Messenger messenger, Exiter exiter, Elevator elevator, String[] args) {
        try {
            elevator.setElevatorSpeed(Float.parseFloat(args[1]));
        } catch (NumberFormatException e) {
            messenger.printErrorMessage("Скорость движения лифта должна быть числом");
            exiter.exit();
        }
        try {
            elevator.setFloorHeight(Float.parseFloat(args[2]));
        } catch (NumberFormatException e) {
            messenger.printErrorMessage("Высота этажа в здании должна быть числом");
            exiter.exit();
        }
        try {
            elevator.setDoorsTime(Integer.parseInt(args[3]));
        } catch (NumberFormatException e) {
            messenger.printErrorMessage("Время между открытием и закрытием дверей лифта должно быть целым числом");
            exiter.exit();
        }
    }

    /**
     * Entry point to start program
     * @param args arguments for program:
     *             first argument - number of floors in building,
     *             second argument - speed of elevator,
     *             third argument - height of floor in building,
     *             fourth argument - time of cycle open-close for elevator's doors
     */
    public static void main(String[] args) {
        Messenger messenger = new Messenger();
        messenger.printMessage("*** Симулятор лифта ***\n");

        Elevator elevator = new Elevator(new Messenger());
        InputLoop inputLoop = new InputLoop(new Messenger());

        parseArgs(messenger, args, inputLoop, elevator);
        printAvailableActions(messenger);

        Thread inputThread = new Thread(inputLoop);
        Thread elevatorThread = new Thread(elevator);
        inputThread.start();
        elevatorThread.start();
    }
}
