import java.util.List;

// Provides account, wishlist, and ordering actions for the active client.
public class ClientMenuState extends WarehouseState {

    // FIELDS


    private static ClientMenuState instance;

    private WarehouseBusiness warehouse;


    // private constructor


    private ClientMenuState() {
        warehouse = WarehouseBusiness.instance();
    }


    // SINGLETON INSTANCE
  

    public static ClientMenuState instance() {

        if (instance == null) {
            instance = new ClientMenuState();
        }

        return instance;
    }

    // DISPLAY CLIENT MENU


    // GET CURRENT CLIENT


    // Resolve the client selected by the context for this session.
    private Client getCurrentClient() {

        String clientID =
            WarehouseContext.instance().getClientID();
        if (clientID == null) {
            System.out.println("No client selected.");
            return null;
        }
        Client client = warehouse.getClient(clientID);
        if (client == null) {
            System.out.println("Client not found.");
        }
        return client;
    }


    // SHOW CLIENT DETAILS

    private void showClientDetails() {
        Client client = getCurrentClient();
        if (client == null) {
            return;
        }

        System.out.println(
            "\n===== CLIENT DETAILS ====="
        );

        System.out.println("ID: " + client.getID());
        System.out.println("Name: " + client.getName());
        System.out.println(
            "Address: " + client.getAddress()
        );
        System.out.println(
            "Balance: $" + client.getBalance()
        );
    }


    // SHOW PRODUCTS

    private void showProducts() {

        // Reuse existing warehouse backend
        warehouse.displayProducts();
    }

    // SHOW TRANSACTIONS

    private void showTransactions() {

        Client client = getCurrentClient();

        if (client == null) {
            return;
        }

        System.out.println(
            "\n===== CLIENT TRANSACTIONS ====="
        );

        warehouse.viewClientInvoices(client);
    }


    // ADD PRODUCT TO WISHLIST


    // Validate the product and quantity before updating the wishlist.
    private void addToWishlist() {

        Client client = getCurrentClient();

        if (client == null) {
            return;
        }


        String productID =
            WarehouseContext.prompt("Enter Product ID: ").trim();

        Product product =
            warehouse.getInventory()
                    .findProductByID(productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }


        int quantity;

        try {
            quantity = Integer.parseInt(
                WarehouseContext.prompt("Enter quantity: ").trim()
            );
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity <= 0) {
            System.out.println(
                "Quantity must be greater than zero."
            );
            return;
        }

        // Call existing warehouse backend
        boolean added = warehouse.addProductWishlist(
            client,
            product,
            quantity
        );

        System.out.println(added
            ? "Product added to wishlist."
            : "Unable to update wishlist. Check the quantity and try again.");
    }


    // DISPLAY WISHLIST


    private void showWishlist() {

        Client client = getCurrentClient();

        if (client == null) {
            return;
        }

        warehouse.displayWishlist(client);
    }


    // PLACE ORDER


    // Delegate purchase and stock/waitlist processing to the backend.
    private void placeOrder() {

        Client client = getCurrentClient();

        if (client == null) {
            return;
        }

        System.out.println(
            "\n===== PLACE ORDER ====="
        );

        warehouse.processClientOrder(client);
    }


    // LOGOUT


    private void logout() {

        WarehouseContext.instance().logout();

        System.out.println("Logged out of Client Menu.");
    }

    // RUN CLIENT STATE
    @Override
    String title() {
        return "Client Menu";
    }

    @Override
    List<Action> actions() {
        return List.of(
            new Action("Show Client Details", this::showClientDetails),
            new Action("Show Products", this::showProducts),
            new Action("Show Transactions", this::showTransactions),
            new Action("Add Product to Wishlist", this::addToWishlist),
            new Action("Display Wishlist", this::showWishlist),
            new Action("Place Order", this::placeOrder),
            new Action("Logout", this::logout)
        );
    }
}
