import java.util.List;

// Handles role selection and validates direct client logins.
public class LoginState extends WarehouseState {

    // SINGLETON INSTANCE
    private static LoginState instance;

    // Existing warehouse backend
    private WarehouseBusiness warehouse;

    // private constructor


    private LoginState() {
        warehouse = WarehouseBusiness.instance();
    }


    // Singleton instance

    public static LoginState instance() {

        if (instance == null) {
            instance = new LoginState();
        }

        return instance;
    }

    // DISPLAY LOGIN MENU


    // LOGIN AS CLIENT

    // Enter the client menu only when the ID exists in the backend.
    private boolean loginClient(String clientID) {

        Client client = warehouse.getClient(clientID);

        if (client == null) {
            System.out.println("Invalid Client ID.");
            return false;
        }

        WarehouseContext context =
                WarehouseContext.instance();

        context.setClientID(clientID);
        context.setLoginRole(
            WarehouseContext.ROLE_CLIENT
        );

        context.changeState(
            WarehouseContext.TO_CLIENT
        );

        System.out.println(
            "Welcome, " + client.getName() + "!"
        );

        return true;
    }

    // LOGIN AS CLERK

    private boolean loginClerk() {
        if (!authenticateRole(
                "WAREHOUSE_CLERK_PASSWORD", "Clerk")) {
            return false;
        }

        WarehouseContext context =
                WarehouseContext.instance();
        context.setLoginRole(
            WarehouseContext.ROLE_CLERK
        );
        context.changeState(
            WarehouseContext.TO_CLERK
        );
        return true;
    }

    // LOGIN AS MANAGER

    private boolean loginManager() {
        if (!authenticateRole(
                "WAREHOUSE_MANAGER_PASSWORD", "Manager")) {
            return false;
        }

        WarehouseContext context =
                WarehouseContext.instance();
        context.setLoginRole(
            WarehouseContext.ROLE_MANAGER
        );
        context.changeState(
            WarehouseContext.TO_MANAGER
        );
        return true;
    }

    private boolean authenticateRole(
            String environmentVariable,
            String roleName) {
        String configuredPassword =
                System.getenv(environmentVariable);

        if (configuredPassword == null
                || configuredPassword.isEmpty()) {
            System.out.println(
                roleName + " login is not configured. Set "
                + environmentVariable + " and restart."
            );
            return false;
        }

        String enteredPassword = WarehouseContext.promptPassword("Enter " + roleName + " password: ");
        if (!configuredPassword.equals(enteredPassword)) {
            System.out.println("Incorrect " + roleName + " password.");
            return false;
        }

        return true;
    }


    // EXIT SYSTEM
    private void exit() {

        WarehouseContext.instance().changeState(
            WarehouseContext.EXIT
        );
    }

    // RUN LOGIN STATE
    @Override
    String title() {
        return "Warehouse Login";
    }

    @Override
    List<Action> actions() {
        return List.of(
            new Action("Login as Client", () -> loginClient(WarehouseContext.prompt("Enter Client ID: ").trim())),
            new Action("Login as Clerk", this::loginClerk),
            new Action("Login as Manager", this::loginManager),
            new Action("Exit", this::exit)
        );
    }
}
