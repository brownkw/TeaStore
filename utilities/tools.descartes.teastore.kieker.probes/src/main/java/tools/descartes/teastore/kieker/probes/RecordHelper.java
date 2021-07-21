package tools.descartes.teastore.kieker.probes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;
import tools.descartes.teastore.kieker.probes.records.OperationExecutionWithParametersRecord;

/**
 * Probe to log execution times plus parameter values with Kieker.
 *
 * @author Johannes Grohmann
 *
 */

public class RecordHelper {
  private static final Logger LOG = LoggerFactory.getLogger(RecordHelper.class);
  private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
  private static final ITimeSource TIME = CTRLINST.getTimeSource();
  private static final String VMNAME = CTRLINST.getHostname();
  private static final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
  private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

  public static Record createRecord(String methodSignature) {
    return createRecord(methodSignature, "void");
  }

  public static Record createRecord(String methodSignature, String returnType) {
    if (!CTRLINST.isMonitoringEnabled() || !CTRLINST.isProbeActivated(methodSignature)) {   //TODO: signature starts with "/"?
      return null;
    }
    // collect data
    final boolean entrypoint;
    final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
    final int eoi; // this is executionOrderIndex-th execution in this trace
    final int ess; // this is the height in the dynamic call tree of this execution
    long traceId = CFREGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
    if (traceId == -1) {
      entrypoint = true;
      traceId = CFREGISTRY.getAndStoreUniqueThreadLocalTraceId();
      CFREGISTRY.storeThreadLocalEOI(0);
      CFREGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
      eoi = 0;
      ess = 0;
    } else {
      entrypoint = false;
      eoi = CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
      ess = CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
      if ((eoi == -1) || (ess == -1)) {
        LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
        CTRLINST.terminateMonitoring();
      }
    }
    // measure before
    final long tin = TIME.getTime();
    return new Record(methodSignature, returnType, sessionId, traceId, tin, VMNAME, eoi, ess, entrypoint);
  }

  public static void finishRecord(Record record) {
    finishRecord(record, "");
  }

  public static void finishRecord(Record record, String returnValue) {
    record.setReturnValue(returnValue);
    // measure after
    final long tout = TIME.getTime();
    // get parameters
    String flag = System.getenv("LOG_PARAMETERS");
    if (flag != null && (flag.equals("true") || flag.equals("TRUE"))) {
      logWithParameters(record, tout);
    } else {
      logWithoutParameters(record, tout);
    }
    // cleanup
    if (record.isEntrypoint()) {
      CFREGISTRY.unsetThreadLocalTraceId();
      CFREGISTRY.unsetThreadLocalEOI();
      CFREGISTRY.unsetThreadLocalESS();
    } else {
      CFREGISTRY.storeThreadLocalESS(record.getEss()); // next operation is ess
    }
  }

  private static void logWithParameters(Record record, long tout) {
    if (record.hasParams()) {
      CTRLINST.newMonitoringRecord(new OperationExecutionWithParametersRecord(record.getSignature(), record.getSessionId(),
              record.getTraceId(), record.getTin(), tout, record.getHostname(), record.getEoi(), record.getEss(), record.getParamNames(), record.getParamValues(), record.getReturnType(), record.getReturnValue()));
    } else {
      logWithoutParameters(record, tout);
    }
  }

  private static void logWithoutParameters(Record record, long tout) {
    CTRLINST.newMonitoringRecord(new OperationExecutionRecord(record.getSignature(), record.getSessionId(), record.getTraceId(), record.getTin(), tout, record.getHostname(), record.getEoi(), record.getEss()));
  }
}
