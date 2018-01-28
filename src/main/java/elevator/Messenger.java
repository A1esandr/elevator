package elevator;

/**
 * Class to print output to console
 */
class Messenger {
    /**
     * Print message to console
     * @param message message to print
     */
    public void printMessage(String message){
        System.out.print(message);
    }

    /**
     * Print formatted message to console
     * @param template for formatting message
     * @param value value for message
     */
    public void printFormattedMessage(String template, String value){
        System.out.format(template, value);
    }

    /**
     * Print error message to console
     * @param message message to print
     */
    public void printErrorMessage(String message){
        System.err.println(message);
    }
}
