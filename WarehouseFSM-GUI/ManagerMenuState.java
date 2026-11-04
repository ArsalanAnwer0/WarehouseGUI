import java.util.List;

// Provides inventory and shipment actions for managers.
public class ManagerMenuState extends WarehouseState {

    // =====================================
    // FIELDS
    // =====================================

    private static ManagerMenuState instance;

    private WarehouseBusiness warehouse;

    // private constructor

    private ManagerMenuState() {
        warehouse = WarehouseBusiness.instance();
    }

    // =====================================
    // SINGLETON INSTANCE
    // =====================================

    public static ManagerMenuState instance() {

        if (instance == null) {
            instance = new ManagerMenuState();
        }

        return instance;
    }

    // =====================================
    // DISPLAY MANAGER MENU
    // =====================================


    // =====================================
    // ADD PRODUCT
    // =====================================

    // Validate product details before passing them to the backend.
    private void addProduct() {

        System.out.println(
            "\n===== ADD PRODUCT ====="
        );

        String name = WarehouseContext.prompt("Enter Product Name: ").trim();

        if (name.isEmpty()) {
            System.out.println("Product name cannot be empty.");
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(
                WarehouseContext.prompt("Enter Quantity: ").trim()
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

        double salePrice;

        try {
            salePrice = Double.parseDouble(
                WarehouseContext.prompt("Enter Sale Price: ").trim()
            );
        } catch (NumberFormatException e) {
            System.out.println("Invalid price.");
            return;
        }

        if (!Double.isFinite(salePrice) ||
                salePrice < 0) {

            System.out.println(
                "Price must be a valid non-negative number."
            );
            return;
        }

        // Use existing Request singleton
        Request request = Request.instance();

        request.setProductName(name);
        request.setQuantity(quantity);
        request.setSalePrice(salePrice);

        // Call existing backend
        Result result = warehouse.addProduct(request);

        if (result.getResultCode()
                == Result.OPERATION_COMPLETED) {

            System.out.println(
                "Product added successfully."
            );

            System.out.println(
                "Product ID: " + result.getProductID()
            );

            System.out.println(
                "Product Name: " + result.getProductName()
            );

            System.out.println(
                "Quantity: " + result.getQuantity()
            );

            System.out.printf(
                "Sale Price: $%.2f%n",
                result.getSalePrice()
            );

        } else if (result.getResultCode()
                == Result.DUPLICATE_PRODUCT) {

            System.out.println(
                "Product already exists."
            );

        } else {

            System.out.println(
                "Unable to add product."
            );
        }
    }

    // =====================================
    // DISPLAY PRODUCT WAITLIST
    // =====================================

    private void showProductWaitlist() {

        System.out.println(
            "\n===== PRODUCT WAITLIST ====="
        );

        String productID =
                WarehouseContext.prompt("Enter Product ID: ").trim();

        Product product =
                Inventory.instance()
                         .findProductByID(productID);

        if (product == null) {

            System.out.println(
                "Product not found."
            );
            return;
        }

        warehouse.viewProductWaitlist(product);
    }

    // =====================================
    // RECEIVE SHIPMENT
    // =====================================

    // Receive stock and let the backend fulfill pending waitlist entries.
    private void receiveShipment() {

        System.out.println(
            "\n===== RECEIVE SHIPMENT ====="
        );

        String productID =
                WarehouseContext.prompt("Enter Product ID: ").trim();

        Product product =
                Inventory.instance()
                         .findProductByID(productID);

        if (product == null) {

            System.out.println(
                "Product not found."
            );
            return;
        }

        int quantity;

        try {

            quantity = Integer.parseInt(
                WarehouseContext.prompt("Enter Shipment Quantity: ").trim()
            );

        } catch (NumberFormatException e) {

            System.out.println(
                "Invalid shipment quantity."
            );
            return;
        }

        if (quantity <= 0) {

            System.out.println(
                "Shipment quantity must be greater than zero."
            );
            return;
        }

        // Existing backend method is spelled
        // "recieveShipment" in Project 1.
        warehouse.recieveShipment(
            product,
            quantity
        );

        System.out.println(
            "Shipment processed."
        );

        System.out.println(
            "Updated Quantity: " + product.getQuantity()
        );
    }

    // =====================================
    // BECOME CLERK
    // =====================================

    private void becomeClerk() {

        WarehouseContext context =
                WarehouseContext.instance();

        // Preserve original login role.
        // Do not change loginRole.

        context.changeState(
            WarehouseContext.TO_CLERK
        );

        System.out.println(
            "Switched to Clerk Menu."
        );
    }

    // =====================================
    // LOGOUT
    // =====================================

    private void logout() {

        WarehouseContext.instance().logout();

        System.out.println(
            "Logged out of Manager Menu."
        );
    }

    // =====================================
    // RUN MANAGER STATE
    // =====================================

    @Override
    String title() {
        return "Manager Menu";
    }

    @Override
    List<Action> actions() {
        return List.of(
            new Action("Add Product", this::addProduct),
            new Action("Display Product Waitlist", this::showProductWaitlist),
            new Action("Receive Shipment", this::receiveShipment),
            new Action("Become Clerk", this::becomeClerk),
            new Action("Logout", this::logout)
        );
    }
}
