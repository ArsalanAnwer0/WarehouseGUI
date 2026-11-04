import java.util.LinkedList;
import java.util.Queue;

// Tracks a product's stock and queued client requests.
public class Product {

    private static int idCounter = 1;

    private String productID;
    private String name;
    private int quantity;
    private double salePrice;
    private record Waitlisted(Client client, int quantity) {}
    // Keep requests in arrival order for shipment fulfillment.
    Queue<Waitlisted> waitlist = new LinkedList<>();

    public Product(String name, int quantity, double salePrice) {

        this.productID = "PR" + idCounter++;
        this.name = name;
        this.quantity = quantity;
        this.salePrice = salePrice;
    }

    public String getProductID() {
        return productID;
    }

    public void viewWaitlist() {
        StringBuilder productsString = new StringBuilder();
        productsString.append("\n===== WAITLIST =====\n");
        productsString.append("Product: ").append(name).append("\n----------\n");
        for (Waitlisted waitlisted : waitlist) {
            productsString.append("ClientID: ").append(waitlisted.client.getID());
            productsString.append("\nQuantity: ").append(waitlisted.quantity).append("\n----------\n");
        }
        System.out.println(productsString.toString());
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void removeQuantity(int quantity) {
        this.quantity -= quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void addToWaitlist(Client client, int quantity) {
        Waitlisted waitlisted = new Waitlisted(client, quantity);
        waitlist.add(waitlisted);
    }

    // Add received stock, then use it to fulfill pending requests.
    public void shipment(int currentQuantity) {
        addQuantity(currentQuantity);
        if (waitlist.size() > 0) distributeProduct();
        System.out.println(toString());
    }

    // Charge fulfilled requests and retain any unfilled quantity at the queue front.
    public void distributeProduct() {
    while (!waitlist.isEmpty() && quantity > 0) {
        Waitlisted request = waitlist.peek();

        int fulfilledQuantity = Math.min(
            request.quantity(),
            quantity
        );

        Client client = request.client();

        Transaction transaction =
            new Transaction(client.getID());

        transaction.addProductToTransaction(
            salePrice,
            productID,
            fulfilledQuantity
        );

        removeQuantity(fulfilledQuantity);

        client.addTransaction(transaction);
        client.charge(transaction.getTotalCost());

        System.out.println(transaction);

        waitlist.remove();

        int remainingQuantity =
            request.quantity() - fulfilledQuantity;

        if (remainingQuantity > 0) {
            ((LinkedList<Waitlisted>) waitlist).addFirst(
                new Waitlisted(client, remainingQuantity)
            );
        }
    }

    if (waitlist.isEmpty()) {
        System.out.println("Waitlist fulfilled.");
    } else {
        System.out.println("Some requests are still waiting.");
        viewWaitlist();
    }
}

    @Override
    public String toString() {
        StringBuilder productsString = new StringBuilder();
        productsString.append("\n===== PRODUCT =====\n");
        productsString.append("Name: ").append(name).append("\n");
        productsString.append("ID: ").append(productID).append("\n");
        productsString.append("SalePrice: ").append(salePrice).append("\n");
        productsString.append("Quantity: ").append(quantity).append("\n----------\n");

        return productsString.toString();
    }
}