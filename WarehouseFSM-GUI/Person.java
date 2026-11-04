// Shared identity and contact information for clients and operators.
public abstract class Person {

    private static int idCounter = 1;

    private String personID;
    private String name;
    private String address;

    public Person(String name, String address) {
        this.personID = "P" + idCounter++;
        this.name = name;
        this.address = address;
    }

    public String getID() {
        return personID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}