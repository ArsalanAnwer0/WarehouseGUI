// Coordinates warehouse operations between the menu states and data objects.
public class WarehouseBusiness {

    private static WarehouseBusiness warehouseBusiness;

    private ClientList clientList;
    private OperatorList operatorList;
    private Inventory inventory;

    private WarehouseBusiness() {

        clientList = ClientList.instance();
        operatorList = OperatorList.instance();
        inventory = Inventory.instance();

        // Create one default operator for our project.
        Operator operator =
            new Operator("Warehouse Operator", "Warehouse");

        operatorList.insertOperator(operator);

        System.out.println(
            "Default Operator ID: " + operator.getID()
        );
    }

    public static WarehouseBusiness instance() {

        if (warehouseBusiness == null) {
            warehouseBusiness =
                new WarehouseBusiness();
        }

        return warehouseBusiness;
    }


    // -------------------------
    // DISPLAY PRODUCTS
    // -------------------------

    public void displayProducts() {
        String products = inventory.viewProducts();
        System.out.println(products);
    }



    // -------------------------
    // DISPLAY CLIENTS
    // -------------------------

    public void displayClients() {
        String clients = clientList.viewClients();
        System.out.println(clients);
    }

    // -------------------------
    // DISPLAY WAITLIST
    // -------------------------

    public void displayWaitlist(Product product) {
        product.viewWaitlist();
    }

    // -------------------------
    // DISPLAY WAITLIST
    // -------------------------

    public void recieveShipment(Product currentProduct, int currentQuantity) {
        currentProduct.shipment(currentQuantity);
    }

    // -------------------------
    // DISPLAY CLIENTS
    // -------------------------

    public void displayWishlist(Client currentClient) {
        System.out.println(currentClient.getWishlist().viewWishlist());
    }

    // -------------------------
    // PROCESS CLIENT ORDER
    // -------------------------

    // Let the client's wishlist handle stock, transactions, and waitlisting.
    public void processClientOrder(Client currentClient) {
        currentClient.getWishlist().purchase();
    }

    // -------------------------
    // PROCESS CLIENT PAYMENT
    // -------------------------

    public void processClientPayment(Client currentClient, double amount) {
        currentClient.credit(amount);
    }

    // -------------------------
    // VIEW CLIENT INVOICES
    // -------------------------

    public void viewClientInvoices(Client currentClient) {
        currentClient.viewTransactions();
    }

    
    // -------------------------
    // VIEW CLIENT INVOICES
    // -------------------------

    public void viewProductWaitlist(Product currentProduct) {
        currentProduct.viewWaitlist();
    }
    // -------------------------
    // ADD PRODUCT TO CLIENT WISHLIST
    // -------------------------

    // Return the wishlist result so the menu can report failures accurately.
    public boolean addProductWishlist(Client currentClient, Product currentProduct, int quantity) {
        WishList currentWishlist = currentClient.getWishlist();
        return currentWishlist.addProduct(currentProduct, quantity);
    }

    // -------------------------
    // REMOVE PRODUCT FROM CLIENT WISHLIST
    // -------------------------

    // public void removeProductWishlist(String clientID, String productID, int quantity) {
    //     Client currentClient = clientList.getClient(clientID);
    //     if (currentClient == null) {
    //         System.out.println("Client Not Found");
    //     }
    //     else {
    //         WishList currentWishlist = currentClient.getWishlist();
    //         currentWishlist.removeProduct(productID);
    //     }
    // }

    // -------------------------
    // ADD CLIENT
    // -------------------------

    public Result addClient(Request request) {

        Result result = new Result();

        Client client = new Client(
            request.getClientName(),
            request.getAddress()
        );

        if (clientList.insertClient(client)) {

            result.setResultCode(
                Result.OPERATION_COMPLETED
            );

            result.setClientID(client.getID());
            result.setClientName(client.getName());

        } else {

            result.setResultCode(
                Result.OPERATION_FAILED
            );
        }

        return result;
    }


    // -------------------------
    // ADD PRODUCT
    // -------------------------

    public Result addProduct(Request request) {

        Result result = new Result();

        Product existing =
            inventory.findProduct(
                request.getProductName()
            );

        if (existing != null) {

            result.setResultCode(
                Result.DUPLICATE_PRODUCT
            );

            return result;
        }

        Product product = new Product(
            request.getProductName(),
            request.getQuantity(),
            request.getSalePrice()
        );

        if (inventory.insertProduct(product)) {

            result.setResultCode(
                Result.OPERATION_COMPLETED
            );

            result.setProductID(
                product.getProductID()
            );

            result.setProductName(
                product.getName()
            );

            result.setQuantity(
                product.getQuantity()
            );

            result.setSalePrice(
                product.getSalePrice()
            );

        } else {

            result.setResultCode(
                Result.OPERATION_FAILED
            );
        }

        return result;
    }


    // -------------------------
    // ADD PRODUCT TO WISHLIST
    // -------------------------

    // Convert request validation and wishlist insertion into a result code.
    public Result addProductWishlist(
            Request request) {

        Result result = new Result();

        Client client =
            clientList.getClient(
                request.getClientID()
            );

        if (client == null) {

            result.setResultCode(
                Result.CLIENT_NOT_FOUND
            );

            return result;
        }

        Product product =
            inventory.findProductByID(
                request.getProductID()
            );

        if (product == null) {

            result.setResultCode(
                Result.PRODUCT_NOT_FOUND
            );

            return result;
        }

        boolean added = client.getWishlist().addProduct(
            product,
            request.getQuantity()
        );

        result.setResultCode(
            added
                ? Result.OPERATION_COMPLETED
                : Result.OPERATION_FAILED
        );

        return result;
    }


    // -------------------------
    // FIND CLIENT
    // -------------------------

    public Client getClient(String clientID) {
        return clientList.getClient(clientID);
    }


    // -------------------------
    // FIND OPERATOR
    // -------------------------

    public Operator getOperator(String operatorID) {
        return operatorList.getOperator(operatorID);
    }


    // -------------------------
    // INVENTORY
    // -------------------------

    public Inventory getInventory() {
        return inventory;
    }
}