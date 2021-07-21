package tools.descartes.teastore.kieker.probes;

import java.util.HashMap;

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

    public Record(String signature, String returnType, String sessionId, long traceId, long tin, String hostname, int eoi, int ess, boolean entrypoint) {
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

    public String[] getParamValues() {
        return params.values().toArray(new String[0]);
    }

    public String[] getParamNames() {
        return params.keySet().toArray(new String[0]);
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getSignature() {
        return signature;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getTraceId() {
        return traceId;
    }

    public long getTin() {
        return tin;
    }

    public String getHostname() {
        return hostname;
    }

    public int getEoi() {
        return eoi;
    }

    public int getEss() {
        return ess;
    }

    public boolean isEntrypoint() {
        return entrypoint;
    }

    public boolean hasParams() {
        return params.size() > 0;
    }

    public void addParam(String name, String value) {
        params.put(name, value);
    }
}
