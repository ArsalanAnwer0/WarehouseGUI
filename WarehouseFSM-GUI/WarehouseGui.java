import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class WarehouseGui {
    private final WarehouseContext context = WarehouseContext.instance();
    private final JFrame frame = new JFrame("Warehouse");
    private final JLabel title = new JLabel();
    private final JPanel menu = new JPanel(new GridLayout(0, 2, 8, 8));
    private final JTextArea output = new JTextArea(16, 64);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            new WarehouseGui().show();
        });
    }

    private void show() {
        WarehouseContext.setInputDialog(this::ask);

        JPanel header = new JPanel(new BorderLayout(0, 8));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        header.add(title, BorderLayout.NORTH);
        header.add(menu, BorderLayout.CENTER);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(header, BorderLayout.NORTH);
        content.add(new JScrollPane(output), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(content);
        showMenu(context.activeState());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showMenu(WarehouseState state) {
        if (state == null) {
            frame.dispose();
            return;
        }

        title.setText(state.title());
        menu.removeAll();
        for (WarehouseState.Action action : state.actions()) {
            JButton button = new JButton(action.label());
            button.addActionListener(event -> runAction(state, action.handler()));
            menu.add(button);
        }
        menu.revalidate();
        menu.repaint();
    }

    private void runAction(WarehouseState state, Runnable action) {
        ByteArrayOutputStream messages = new ByteArrayOutputStream();
        PrintStream previousOutput = System.out;

        try (PrintStream actionOutput = new PrintStream(messages, true, StandardCharsets.UTF_8)) {
            System.setOut(actionOutput);
            action.run();
        } catch (WarehouseContext.InputClosedException exception) {
            output.setText("Cancelled.");
            return;
        } finally {
            System.setOut(previousOutput);
        }

        output.setText(messages.toString(StandardCharsets.UTF_8).stripLeading());
        output.setCaretPosition(0);
        if (context.activeState() != state) {
            showMenu(context.activeState());
        }
    }

    private String ask(String message, boolean password) {
        if (!password) {
            String value = JOptionPane.showInputDialog(frame, message.strip());
            if (value == null) {
                throw new WarehouseContext.InputClosedException();
            }
            return value;
        }

        JPasswordField field = new JPasswordField(20);
        int choice = JOptionPane.showConfirmDialog(frame, new Object[] {message.strip(), field},
                title.getText(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            throw new WarehouseContext.InputClosedException();
        }

        char[] value = field.getPassword();
        try {
            return new String(value);
        } finally {
            Arrays.fill(value, '\0');
        }
    }
}
