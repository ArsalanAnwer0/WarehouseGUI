import java.util.function.BiFunction;


import java.io.Console;
import java.util.Scanner;
import java.util.NoSuchElementException;

// Owns the active menu state and controls permitted role transitions.
public class WarehouseContext {
    // STATE CONSTANTS
    public static final int LOGIN = 0;
    public static final int CLIENT = 1;
    public static final int CLERK = 2;
    public static final int MANAGER = 3;
    public static final int WISHLIST = 4;

    // TRANSITION EVENT CONSTANTS
    public static final int TO_LOGIN = 0;
    public static final int TO_CLIENT = 1;
    public static final int TO_CLERK = 2;
    public static final int TO_MANAGER = 3;
    public static final int EXIT = 4;
    public static final int TO_WISHLIST = 5;

    // LOGIN ROLE CONSTANTS
    public static final int ROLE_NONE = -1;
    public static final int ROLE_CLIENT = 0;
    public static final int ROLE_CLERK = 1;
    public static final int ROLE_MANAGER = 2;

    // SPECIAL TRANSITION VALUES


    private static final int INVALID = -1;
    private static final int EXIT_STATE = -2;

    // FIELDS
 

    private static WarehouseContext instance;

    // Redirected input uses this single scanner across every menu.
    private static final Scanner inputScanner =
            new Scanner(System.in);

    private static BiFunction<String, Boolean, String> inputDialog;

    private int currentState;

    private WarehouseState[] states;

    private int[][] nextState;

    private String clientID;

    private int loginRole;


    // private constructor
   
    private WarehouseContext() {

        // Create the state array
        states = new WarehouseState[5];

        states[LOGIN] = LoginState.instance();
        states[CLIENT] = ClientMenuState.instance();
        states[CLERK] = ClerkMenuState.instance();
        states[MANAGER] = ManagerMenuState.instance();
        states[WISHLIST] = WishlistMenuState.instance();

        nextState = new int[][] {
            // TO_LOGIN, TO_CLIENT, TO_CLERK, TO_MANAGER, EXIT, TO_WISHLIST
            { INVALID, CLIENT, CLERK, MANAGER, EXIT_STATE, INVALID }, // LOGIN
            { LOGIN, INVALID, CLERK, INVALID, INVALID, WISHLIST },    // CLIENT
            { LOGIN, CLIENT, INVALID, MANAGER, INVALID, INVALID },   // CLERK
            { LOGIN, INVALID, CLERK, INVALID, INVALID, INVALID },    // MANAGER
            { LOGIN, CLIENT, CLERK, INVALID, INVALID, INVALID }      // WISHLIST
        };

        // Set initial state
        currentState = LOGIN;

        // No client selected
        clientID = null;

        // No original login role
        loginRole = ROLE_NONE;
    }

    // SINGLETON INSTANCE

    public static WarehouseContext instance() {

        if (instance == null) {
            instance = new WarehouseContext();
        }

        return instance;
    }

    public static Scanner inputScanner() {
        return inputScanner;
    }

    static void setInputDialog(BiFunction<String, Boolean, String> dialog) {
        inputDialog = dialog;
    }

    static String prompt(String message) {
        if (inputDialog != null) {
            return inputDialog.apply(message, false);
        }
        System.out.print(message);
        return readLine();
    }

    static String promptPassword(String message) {
        if (inputDialog != null) {
            return inputDialog.apply(message, true);
        }
        System.out.print(message);
        return readPassword();
    }

    WarehouseState activeState() {
        return currentState == EXIT_STATE ? null : states[currentState];
    }

    // Use console input interactively and the one shared scanner in scripts.
    public static String readLine() {
        System.out.flush();
        Console console = System.console();
        if (console != null) {
            String line = console.readLine();
            if (line == null) {
                throw new InputClosedException();
            }
            return line;
        }

        try {
            return inputScanner.nextLine();
        } catch (NoSuchElementException exception) {
            throw new InputClosedException();
        }
    }

    // Passwords are masked in an interactive terminal; redirected input is supported too.
    public static String readPassword() {
        System.out.flush();
        Console console = System.console();
        if (console == null) {
            return readLine();
        }

        char[] password = console.readPassword();
        if (password == null) {
            throw new InputClosedException();
        }

        try {
            return new String(password);
        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }

    public static final class InputClosedException
            extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    // CLIENT ID GETTER AND SETTER

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        if (currentState != LOGIN && currentState != CLERK) {
            System.out.println(
                "Client selection is not allowed from the current state."
            );
            return;
        }
        this.clientID = clientID == null ? null : clientID.trim();
    }

    // LOGIN ROLE GETTER AND SETTER

    public int getLoginRole() {
        return loginRole;
    }

    public void setLoginRole(int role) {
        if (currentState != LOGIN
                || (role != ROLE_CLIENT
                    && role != ROLE_CLERK
                    && role != ROLE_MANAGER)) {
            System.out.println(
                "Login role can only be set after a valid login."
            );
            return;
        }
        loginRole = role;
    }

    // CHANGE STATE USING MATRIX

    public void changeState(int event) {

        // Check whether event is valid
        if (event < TO_LOGIN || event > TO_WISHLIST) {
            System.out.println("Invalid transition event.");
            return;
        }

        // Do not transition after exit
        if (currentState == EXIT_STATE) {
            return;
        }

        // Find destination using matrix
        int destination = nextState[currentState][event];

        // Check for invalid transition
        if (destination == INVALID) {
            System.out.println("Transition not allowed.");
            return;
        }

        // Handle exit
        if (destination == EXIT_STATE) {
            currentState = EXIT_STATE;
            System.out.println("Exiting Warehouse System.");
            return;
        }

        // Client state requires a client ID
        if (destination == CLIENT || destination == WISHLIST) {

            if (clientID == null ||
                    clientID.trim().isEmpty()) {

                System.out.println(
                    "A valid Client ID is required."
                );
                return;
            }

            if (WarehouseBusiness.instance().getClient(clientID) == null) {
                System.out.println("Invalid Client ID.");
                return;
            }
        }

        // Clear the client only when leaving both client-related screens.
        if ((currentState == CLIENT || currentState == WISHLIST)
                && destination != CLIENT
                && destination != WISHLIST) {

            clientID = null;
        }

        // Update the current state
        currentState = destination;

        // Clear session when returning to Login
        if (currentState == LOGIN) {
            clearSession();
        }
    }

    public void exitForEndOfInput() {
        if (currentState != EXIT_STATE) {
            currentState = EXIT_STATE;
            System.out.println(
                "\nInput closed. Exiting Warehouse System."
            );
        }
    }

    // LOGOUT


    // Return to the role that entered the current menu, or to login.
    public void logout() {

        if (currentState == CLIENT || currentState == WISHLIST) {

            if (loginRole == ROLE_CLIENT) {

                // Direct client login
                changeState(TO_LOGIN);

            } else if (loginRole == ROLE_CLERK ||
                       loginRole == ROLE_MANAGER) {

                // Client entered through Clerk
                changeState(TO_CLERK);
            }

        } else if (currentState == CLERK) {

            if (loginRole == ROLE_MANAGER) {

                // Manager temporarily became Clerk
                changeState(TO_MANAGER);

            } else if (loginRole == ROLE_CLERK) {

                // Direct clerk login
                changeState(TO_LOGIN);
            }

        } else if (currentState == MANAGER) {

            // Manager returns to Login
            changeState(TO_LOGIN);
        }
    }

    // CLEAR SESSION
    

    public void clearSession() {

        clientID = null;
        loginRole = ROLE_NONE;
    }


    // PROCESS FSM

    // Keep dispatching states until exit is requested or input ends.
    public void process() {

        while (currentState != EXIT_STATE) {

            try {
                states[currentState].run();
            } catch (InputClosedException e) {
                exitForEndOfInput();
            }
        }
    }


    // MAIN METHOD
   

    public static void main(String[] args) {

        WarehouseContext.instance().process();
    }
}
