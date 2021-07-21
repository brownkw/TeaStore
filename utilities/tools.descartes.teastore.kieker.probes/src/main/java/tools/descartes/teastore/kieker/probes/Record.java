package tools.descartes.teastore.kieker.probes;

import java.util.HashMap;

/**
 * Data Transfer class for RecordHelper.
 *
 * @author Simon Trapp
 *
 */

public class Record {
    private final String returnType;
    private String returnValue;
    private final HashMap<String, String> params = new HashMap<>();

    private final String signature;
    private final String sessionId;
    private final long traceId;
    private final long tin;
    private final String hostname;
    private final int eoi;
    private final int ess;
    private final boolean entrypoint;

    /**
     * Create a new data record for a Kieker probe.
     * @param signature Calling method signature.
     * @param returnType Calling method return data type.
     * @param sessionId Kieker session id.
     * @param traceId Kieker trace id.
     * @param tin Start time of calling function.
     * @param hostname Name of host machine.
     * @param eoi Kieker EOI.
     * @param ess Kieker ESS.
     * @param entrypoint Check if this is new Kieker trace.
     */
    public Record(String signature, String returnType, String sessionId, long traceId, long tin,
                  String hostname, int eoi, int ess, boolean entrypoint) {
        this.signature = signature;
        this.sessionId = sessionId;
        this.traceId = traceId;
        this.tin = tin;
        this.hostname = hostname;
        this.eoi = eoi;
        this.ess = ess;
        this.entrypoint = entrypoint;
        this.returnType = returnType;
    }

    /**
     * Return calling function parameter values.
     * @return Values.
     */
    public String[] getParamValues() {
        return params.values().toArray(new String[0]);
    }

    /**
     * Return calling function parameter names.
     * @return Names.
     */
    public String[] getParamNames() {
        return params.keySet().toArray(new String[0]);
    }

    /**
     * Gets calling function return value.
     * @return Value.
     */
    public String getReturnValue() {
        return returnValue;
    }

    /**
     * Change calling function return value.
     * @param returnValue New return value.
     */
    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * td.
     * @return ret.
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * td.
     * @return td.
     */
    public String getSignature() {
        return signature;
    }

    /**
     * td.
     * @return td.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * td.
     * @return td.
     */
    public long getTraceId() {
        return traceId;
    }

    /**
     * td.
     * @return td.
     */
    public long getTin() {
        return tin;
    }

    /**
     * td.
     * @return td.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * td.
     * @return td.
     */
    public int getEoi() {
        return eoi;
    }

    /**
     * td.
     * @return td.
     */
    public int getEss() {
        return ess;
    }

    /**
     * td.
     * @return td.
     */
    public boolean isEntrypoint() {
        return entrypoint;
    }

    /**
     * td.
     * @return td.
     */
    public boolean hasParams() {
        return params.size() > 0;
    }

    /**
     * td.
     * @param name td.
     * @param value td.
     */
    public void addParam(String name, String value) {
        params.put(name, value);
    }
}
