import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Stores products and provides inventory lookup and display operations.
public class Inventory {

    private static Inventory inventory;

    private List<Product> products;

    private Inventory() {
        products = new LinkedList<Product>();
    }

    public static Inventory instance() {

        if (inventory == null) {
            inventory = new Inventory();
        }

        return inventory;
    }

    public Product findProduct(String name) {

        for (Product product : products) {

            if (product.getName().equals(name)) {
                return product;
            }
        }

        return null;
    }

    public Product findProductByID(String productID) {

        for (Product product : products) {

            if (product.getProductID().equals(productID)) {
                return product;
            }
        }

        return null;
    }

    public boolean insertProduct(Product product) {

        if (findProduct(product.getName()) != null) {
            return false;
        }

        products.add(product);
        return true;
    }

    public Iterator<Product> getProducts() {
        return products.iterator();
    }

    public String viewProducts() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder productsString = new StringBuilder();
        productsString.append("\n===== INVENTORY =====\n");
        for (Product product : products) {
            productsString.append("Name: ").append(product.getName()).append("\n");
            productsString.append("ID: ").append(product.getProductID()).append("\n");
            productsString.append("SalePrice: ").append(product.getSalePrice()).append("\n");
            productsString.append("Quantity: ").append(product.getQuantity()).append("\n----------\n");
        }
        return productsString.toString();
    }
}