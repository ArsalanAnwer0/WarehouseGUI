import java.util.List;

public class WishlistMenuState extends WarehouseState {

    private static WishlistMenuState instance;
    private final WarehouseBusiness warehouse;

    private WishlistMenuState() {
        warehouse = WarehouseBusiness.instance();
    }

    public static WishlistMenuState instance() {
        if (instance == null) {
            instance = new WishlistMenuState();
        }
        return instance;
    }

    private Client getCurrentClient() {
        String clientID = WarehouseContext.instance().getClientID();

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

    private void addToWishlist() {
        Client client = getCurrentClient();
        if (client == null) {
            return;
        }

        String productID =
                WarehouseContext.prompt("Enter Product ID: ").trim();

        Product product =
                warehouse.getInventory().findProductByID(productID);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(
                WarehouseContext.prompt("Enter quantity: ").trim()
            );
        } catch (NumberFormatException exception) {
            System.out.println("Invalid quantity.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        boolean added =
                warehouse.addProductWishlist(client, product, quantity);

        System.out.println(added
                ? "Product added to wishlist."
                : "Unable to update wishlist.");
    }

    private void showWishlist() {
        Client client = getCurrentClient();
        if (client != null) {
            warehouse.displayWishlist(client);
        }
    }

    private void placeOrder() {
        Client client = getCurrentClient();
        if (client != null) {
            System.out.println("\n===== PLACE ORDER =====");
            warehouse.processClientOrder(client);
        }
    }

    private void backToClientMenu() {
        WarehouseContext.instance().changeState(
                WarehouseContext.TO_CLIENT
        );
    }

    private void logout() {
        WarehouseContext.instance().logout();
    }

    @Override
    String title() {
        return "Wishlist Operations";
    }

    @Override
    List<Action> actions() {
        return List.of(
            new Action("Add Product to Wishlist", this::addToWishlist),
            new Action("Display Wishlist", this::showWishlist),
            new Action("Place Order", this::placeOrder),
            new Action("Back to Client Menu", this::backToClientMenu),
            new Action("Logout", this::logout)
        );
    }
}