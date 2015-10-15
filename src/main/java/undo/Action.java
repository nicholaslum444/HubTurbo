package undo;

public class Action {

    public enum Type {LABEL}
    public enum Operation {ADD, REMOVE}

    public final Type type;
    public final String value;
    public final Operation operation;

    public Action(Type type, String value, Operation operation) {
        this.type = type;
        this.value = value;
        this.operation = operation;
    }

    public Operation getInverseOperation() {
        if (operation == Operation.ADD) {
            return Operation.REMOVE;
        } else {
            return Operation.ADD;
        }
    }

    public Action getInverse() {
        return new Action(type, value, getInverseOperation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return type == action.type &&
                value.equals(action.value) &&
                operation == action.operation;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + operation.hashCode();
        return result;
    }

}
