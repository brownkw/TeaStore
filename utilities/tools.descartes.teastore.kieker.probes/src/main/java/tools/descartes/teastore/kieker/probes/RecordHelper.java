package tools.descartes.teastore.kieker.probes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;
import tools.descartes.teastore.entities.ImageSize;
import tools.descartes.teastore.entities.message.SessionBlob;
import tools.descartes.teastore.kieker.probes.records.OperationExecutionWithParametersRecord;

import java.util.function.Function;

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

  public static void recordOperation(String methodSignature, Function<Boolean, RecordHelperParameters> consumer) {
    if (!CTRLINST.isMonitoringEnabled() || !CTRLINST.isProbeActivated(methodSignature)) {   //TODO: signature starts with "/"?
      consumer.apply(false);   // if execution is not monitored --> param = false
      return;
    }

    // collect data
    final boolean entrypoint;
    final String hostname = VMNAME;
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
    // execution of the called method
    RecordHelperParameters params = null;
    try {
      params = consumer.apply(true);
    } finally {
      // measure after
      final long tout = TIME.getTime();
      // get parameters

      String flag = System.getenv("LOG_PARAMETERS");
      if (flag != null && (flag.equals("true") || flag.equals("TRUE"))) {
        logWithParameters(methodSignature, sessionId, traceId, tin, tout, hostname, eoi, ess, params);
      } else {
        logWithoutParameters(methodSignature, sessionId, traceId, tin, tout, hostname, eoi, ess);
      }

      // cleanup
      if (entrypoint) {
        CFREGISTRY.unsetThreadLocalTraceId();
        CFREGISTRY.unsetThreadLocalEOI();
        CFREGISTRY.unsetThreadLocalESS();
      } else {
        CFREGISTRY.storeThreadLocalESS(ess); // next operation is ess
      }
    }
  }

  private static void logWithParameters(String signature, String sessionId, long traceId, long tin, long tout, String hostname, int eoi, int ess, RecordHelperParameters params) {
    if (params == null) {
      logWithoutParameters(signature, sessionId, traceId, tin, tout, hostname, eoi, ess);
    } else {
      CTRLINST.newMonitoringRecord(new OperationExecutionWithParametersRecord(signature, sessionId,
              traceId, tin, tout, hostname, eoi, ess, params.getParamNames(), params.getParamValues(), params.getReturnType(), params.getReturnValue()));
    }
  }

  private static void logWithoutParameters(String signature, String sessionId, long traceId, long tin, long tout, String hostname, int eoi, int ess) {
    CTRLINST.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
  }
}
