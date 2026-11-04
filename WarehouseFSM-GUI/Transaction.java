import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Records a client's purchase date, line items, and total cost.
public class Transaction {

    private double totalCost;
    private String date;
    private String clientID;
    private record ProductBought(String productID, int quantity) {}
    private List<ProductBought> productIDs = new LinkedList<ProductBought>();

    public Transaction(String clientID) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        this.date = LocalDateTime.now().format(customFormatter);
        this.clientID = clientID;
    }

    public double getTotalCost() {
        return totalCost;
    }
    public String getDate() {
        return date;
    }
    public String getClientID() {
        return clientID;
    }
    public List<ProductBought> getProductIDs() {
        return productIDs;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public void setProductIDs(List<ProductBought> productIDs) {
        this.productIDs = productIDs;
    }


// Add a purchased line and include its cost in the transaction total.
public void addProductToTransaction(double unitPrice, String productID, int quantity) {
    this.totalCost += unitPrice * quantity;
    ProductBought currentProductBought = new ProductBought(productID, quantity);
    productIDs.add(currentProductBought);
}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n===== TRANSACTION =====\n");
        stringBuilder.append("Total Cost: $").append(totalCost).append("\n");
        stringBuilder.append("ClientID: ").append(clientID).append("\n");
        stringBuilder.append("Date: ").append(date).append("\n");
        stringBuilder.append("Products: ").append("\n----------\n");
        for (ProductBought productBought : productIDs) {
            stringBuilder.append("ProductID: ").append(productBought.productID).append("\n");
            stringBuilder.append("Quantity: ").append(productBought.quantity).append("\n----------\n");
        }
        return stringBuilder.toString();
    }
}