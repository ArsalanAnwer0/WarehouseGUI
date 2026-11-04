import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// Stores operators and supports lookup by generated ID.
public class OperatorList {

    private static OperatorList operatorList;

    private List<Operator> operators;

    private OperatorList() {
        operators = new LinkedList<Operator>();
    }

    public static OperatorList instance() {

        if (operatorList == null) {
            operatorList = new OperatorList();
        }

        return operatorList;
    }

    public Operator getOperator(String operatorID) {

        for (Operator operator : operators) {

            if (operator.getID().equals(operatorID)) {
                return operator;
            }
        }

        return null;
    }

    public boolean insertOperator(Operator operator) {

        if (getOperator(operator.getID()) != null) {
            return false;
        }

        operators.add(operator);
        return true;
    }

    public Iterator<Operator> getOperators() {
        return operators.iterator();
    }
}