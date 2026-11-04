// Associates one product with the quantity requested by a client.
public class WishListItem {

    private Product product;
    private int quantity;

    public WishListItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return product.getName();
    }

    public String getProductID() {
        return product.getProductID();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}