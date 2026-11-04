import java.util.List;
import java.util.Locale;

public abstract class WarehouseState {
    record Action(String label, Runnable handler) {}

    abstract String title();

    abstract List<Action> actions();

    public void run() {
        List<Action> menu = actions();
        System.out.println("\n===== " + title().toUpperCase(Locale.ROOT) + " =====");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).label());
        }

        String choice = WarehouseContext.prompt("Enter choice: ").trim();
        for (int i = 0; i < menu.size(); i++) {
            if (choice.equals(Integer.toString(i + 1))) {
                menu.get(i).handler().run();
                return;
            }
        }
        System.out.println("Invalid choice. Please try again.");
    }
}
