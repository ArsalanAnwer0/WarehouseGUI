// Returns an operation status together with any generated or matched data.
public class Result {

    public static final int OPERATION_COMPLETED = 1;
    public static final int OPERATION_FAILED = 2;
    public static final int CLIENT_NOT_FOUND = 3;
    public static final int PRODUCT_NOT_FOUND = 4;
    public static final int DUPLICATE_PRODUCT = 5;
    public static final int OPERATOR_NOT_FOUND = 6;

    private int resultCode;

    private String clientID;
    private String clientName;

    private String operatorID;

    private String productID;
    private String productName;

    private int quantity;
    private double salePrice;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}