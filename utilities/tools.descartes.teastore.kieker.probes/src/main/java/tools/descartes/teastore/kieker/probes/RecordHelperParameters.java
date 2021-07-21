package tools.descartes.teastore.kieker.probes;

public class RecordHelperParameters {
    private final String returnType;
    private final String returnValue;
    private final String[] paramNames;
    private final String[] paramValues;

    public RecordHelperParameters(String returnType, String returnValue, String[] paramNames, String[] paramValues) {
        if (returnType == null || returnValue == null || paramNames == null || paramValues == null) {
            throw new IllegalArgumentException("no null values allowed in RecordHelperParameters constructor!");
        } else if (paramNames.length != paramValues.length) {
            throw new IllegalArgumentException("parameter names and values must be of same size!");
        }
        this.returnType = returnType;
        this.returnValue = returnValue;
        this.paramNames = paramNames;
        this.paramValues = paramValues;
    }

    public String[] getParamValues() {
        return paramValues;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public String getReturnType() {
        return returnType;
    }
}
