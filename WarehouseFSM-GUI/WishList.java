
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Holds a client's requested quantities until they place an order.
public class WishList {

    private List<WishListItem> items;
    private Client client;

    public WishList(Client client) {
        items = new LinkedList<WishListItem>();
        this.client = client;
    }

    // Store the requested quantity without reserving inventory stock.
    public boolean addProduct(Product product, int quantity) {

        if (quantity <= 0) {
            return false;
        }

        WishListItem existing =
            findProduct(product.getProductID());

        if (existing != null) {
            existing.setQuantity(quantity);
            return true;
        }

        WishListItem item =
            new WishListItem(product, quantity);

        items.add(item);

        return true;
    }

    // Automatically purchase available quantities
    // and waitlist quantities that are unavailable.
    public void purchase() {

        if (items.isEmpty()) {
            System.out.println("No Items In Wishlist");
            return;
        }

        Transaction transaction = null;

        Iterator<WishListItem> iterator = items.iterator();

        while (iterator.hasNext()) {

            WishListItem item = iterator.next();
            Product product = item.getProduct();

            int requestedQuantity = item.getQuantity();

            System.out.println("\n===== PRODUCT =====");
            System.out.println("Product: " + product.getName());
            System.out.println("ID: " + product.getProductID());
            System.out.println("Requested quantity: " + requestedQuantity);
            System.out.println("Available stock: " + product.getQuantity());

            int purchasedQuantity = Math.min(
                requestedQuantity,
                product.getQuantity()
            );

            int waitlistedQuantity =
                requestedQuantity - purchasedQuantity;

            // Process purchased quantity.
            if (purchasedQuantity > 0) {

                if (transaction == null) {
                    transaction = new Transaction(client.getID());
                }

                transaction.addProductToTransaction(
                    product.getSalePrice(),
                    product.getProductID(),
                    purchasedQuantity
                );

                product.removeQuantity(purchasedQuantity);

                System.out.println(
                    "Purchased " + purchasedQuantity
                    + " of " + product.getName()
                );
            }

            // Process unavailable quantity.
            if (waitlistedQuantity > 0) {

                product.addToWaitlist(
                    client,
                    waitlistedQuantity
                );

                System.out.println(
                    "Waitlisted " + waitlistedQuantity
                    + " of " + product.getName()
                );
            }

            // Remove processed item from wishlist.
            iterator.remove();
        }

        // Save transaction and update client balance.
        if (transaction != null) {

            client.addTransaction(transaction);

            client.charge(transaction.getTotalCost());

            System.out.println("\n===== ORDER SUMMARY =====");
            System.out.println(transaction);
        }

        System.out.println("\nOrder processing completed.");
    }

    public WishListItem findProduct(String productID) {

        for (WishListItem item : items) {

            if (item.getProduct()
                    .getProductID()
                    .equals(productID)) {

                return item;
            }
        }

        return null;
    }

    public boolean updateQuantity(
            String productID,
            int quantity) {

        WishListItem item =
            findProduct(productID);

        if (item == null) {
            return false;
        }

        if (quantity <= 0) {
            return false;
        }

        item.setQuantity(quantity);
        return true;
    }

    public Iterator<WishListItem> getWishlistItems() {
        return items.iterator();
    }

    public String viewWishlist() {
        return toString();
    }

    @Override
    public String toString() {

        StringBuilder productsString = new StringBuilder();

        productsString.append("\n===== WISHLIST =====\n");

        for (WishListItem item : items) {

            Product product = item.getProduct();

            productsString.append("Name: ")
                .append(product.getName()).append("\n");

            productsString.append("ID: ")
                .append(product.getProductID()).append("\n");

            productsString.append("SalePrice: ")
                .append(product.getSalePrice()).append("\n");

            productsString.append("Quantity: ")
                .append(item.getQuantity()).append("\n");

            productsString.append("----------\n");
        }

        return productsString.toString();
    }
}
