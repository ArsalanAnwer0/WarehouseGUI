import java.util.LinkedList;
import java.util.List;

// Stores a customer's balance, wishlist, and purchase history.
public class Client extends Person {

    private double balance;
    private WishList wishlist;
    private List<Transaction> transactions = new LinkedList<>();

    public Client(String name, String address) {
        super(name, address);

        balance = 0.0;
        wishlist = new WishList(this);
    }

    public double getBalance() {
        return balance;
    }

    public WishList getWishlist() {
        return wishlist;
    }

    // Purchases lower the balance because negative means money owed.
    public void charge(double charge) {
        balance -= charge;
        balance = Math.round(balance * 100.0d) / 100.0d;
    }

    // Payments raise the balance toward zero.
    public void credit(double credit) {
        balance += credit;
        balance = Math.round(balance * 100.0d) / 100.0d;
        System.out.println("Paid $" + credit + " To " + getName() + "\nNew Balance: $" + balance);
    }

    public void addTransaction(Transaction currentTransaction) {
        transactions.add(currentTransaction);
    }

    public void viewTransactions() {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }
}