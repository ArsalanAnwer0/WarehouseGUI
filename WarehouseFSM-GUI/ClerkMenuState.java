import java.util.List;

import java.util.Iterator;

// Provides client administration and payment actions for clerks.
public class ClerkMenuState extends WarehouseState {

    // =====================================
    // FIELDS
    // =====================================

    private static ClerkMenuState instance;

    private WarehouseBusiness warehouse;

    // private constructor

    private ClerkMenuState() {
        warehouse = WarehouseBusiness.instance();
    }

    // =====================================
    // SINGLETON INSTANCE
    // =====================================

    public static ClerkMenuState instance() {

        if (instance == null) {
            instance = new ClerkMenuState();
        }

        return instance;
    }

    // =====================================
    // DISPLAY CLERK MENU
    // =====================================


    // =====================================
    // ADD CLIENT
    // =====================================

    private void addClient() {

        System.out.println(
            "\n===== ADD CLIENT ====="
        );

        String name = WarehouseContext.prompt("Enter Client Name: ").trim();

        String address = WarehouseContext.prompt("Enter Address: ").trim();

        if (name.isEmpty() || address.isEmpty()) {
            System.out.println(
                "Name and address cannot be empty."
            );
            return;
        }

        Request request = Request.instance();

        request.setClientName(name);
        request.setAddress(address);

        Result result = warehouse.addClient(request);

        if (result.getResultCode()
                == Result.OPERATION_COMPLETED) {

            System.out.println(
                "Client created successfully."
            );

            System.out.println(
                "Client ID: " + result.getClientID()
            );

            System.out.println(
                "Client Name: " + result.getClientName()
            );

        } else {

            System.out.println(
                "Unable to create client."
            );
        }
    }

    // =====================================
    // SHOW PRODUCTS
    // =====================================

    private void showProducts() {

        System.out.println(
            "\n===== AVAILABLE PRODUCTS ====="
        );

        warehouse.displayProducts();
    }

    // =====================================
    // SHOW ALL CLIENTS
    // =====================================

    private void showClients() {

        System.out.println(
            "\n===== ALL CLIENTS ====="
        );

        warehouse.displayClients();
    }

    // =====================================
    // SHOW CLIENTS WITH OUTSTANDING BALANCE
    // =====================================

    // A negative balance represents money the client still owes.
    private void showClientsWithOutstandingBalance() {

        System.out.println(
            "\n===== CLIENTS WITH OUTSTANDING BALANCE ====="
        );

        Iterator<Client> clients =
                ClientList.instance().getClients();

        boolean found = false;

        while (clients.hasNext()) {

            Client client = clients.next();

            // Negative balance means client owes money
            if (client.getBalance() < 0) {

                found = true;

                System.out.println(
                    "Client ID: " + client.getID()
                );

                System.out.println(
                    "Name: " + client.getName()
                );

                System.out.printf(
                    "Outstanding Amount: $%.2f%n",
                    -client.getBalance()
                );

                System.out.println("------------------");
            }
        }

        if (!found) {

            System.out.println(
                "No clients have outstanding balances."
            );
        }
    }

    // =====================================
    // RECORD CLIENT PAYMENT
    // =====================================

    // Apply a validated payment to the selected client's balance.
    private void recordPayment() {

        System.out.println(
            "\n===== RECORD PAYMENT ====="
        );


        String clientID =
                WarehouseContext.prompt("Enter Client ID: ").trim();

        Client client =
                warehouse.getClient(clientID);

        if (client == null) {

            System.out.println("Client not found.");
            return;
        }


        double amount;

        try {

            amount = Double.parseDouble(
                WarehouseContext.prompt("Enter Payment Amount: ").trim()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                "Invalid payment amount."
            );
            return;
        }

        if (!Double.isFinite(amount) || amount <= 0) {

            System.out.println(
                "Payment must be a positive, finite amount."
            );
            return;
        }

        warehouse.processClientPayment(
            client,
            amount
        );

        System.out.println(
            "Payment recorded successfully."
        );

        System.out.printf(
            "Current Balance: $%.2f%n",
            client.getBalance()
        );
    }

    // =====================================
    // BECOME CLIENT
    // =====================================

    // Temporarily switch roles without changing the original login role.
    private boolean becomeClient(String clientID) {

        Client client =
                warehouse.getClient(clientID);

        if (client == null) {

            System.out.println(
                "Invalid Client ID."
            );

            return false;
        }

        WarehouseContext context =
                WarehouseContext.instance();

        context.setClientID(clientID);

        // Preserve the original login role.
        // Do not change loginRole here.

        context.changeState(
            WarehouseContext.TO_CLIENT
        );

        System.out.println(
            "Now acting as Client: "
            + client.getName()
        );

        return true;
    }

    // =====================================
    // LOGOUT
    // =====================================

    private void logout() {

        WarehouseContext.instance().logout();

        System.out.println(
            "Logged out of Clerk Menu."
        );
    }

    // =====================================
    // RUN CLERK STATE
    // =====================================

    @Override
    String title() {
        return "Clerk Menu";
    }

    @Override
    List<Action> actions() {
        return List.of(
            new Action("Add Client", this::addClient),
            new Action("Show Products", this::showProducts),
            new Action("Show All Clients", this::showClients),
            new Action("Show Clients With Outstanding Balance", this::showClientsWithOutstandingBalance),
            new Action("Record Client Payment", this::recordPayment),
            new Action("Become Client", () -> becomeClient(WarehouseContext.prompt("Enter Client ID: ").trim())),
            new Action("Logout", this::logout)
        );
    }
}
