import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Stores clients and supports lookup by ID or name.
public class ClientList {

    private static ClientList clientList;

    private List<Client> clients;

    private ClientList() {
        clients = new LinkedList<Client>();
    }

    public static ClientList instance() {

        if (clientList == null) {
            clientList = new ClientList();
        }

        return clientList;
    }

    public Client getClient(String clientID) {

        for (Client client : clients) {

            if (client.getID().equals(clientID)) {
                return client;
            }
        }

        return null;
    }

    public Client findClient(String name) {

        for (Client client : clients) {

            if (client.getName().equals(name)) {
                return client;
            }
        }

        return null;
    }

    public boolean insertClient(Client client) {

        if (getClient(client.getID()) != null) {
            return false;
        }

        clients.add(client);
        return true;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    public String viewClients() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder clientString = new StringBuilder();
        clientString.append("\n===== CLIENTS =====\n");
        for (Client client : clients) {
            clientString.append("Name: ").append(client.getName()).append("\n");
            clientString.append("ID: ").append(client.getID()).append("\n");
            clientString.append("Address: ").append(client.getAddress()).append("\n");
            clientString.append("Balance: $").append(client.getBalance()).append("\n----------\n");
        }
        return clientString.toString();
    }
}