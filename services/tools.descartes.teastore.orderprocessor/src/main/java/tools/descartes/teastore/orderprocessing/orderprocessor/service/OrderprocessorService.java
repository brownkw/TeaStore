package tools.descartes.teastore.orderprocessing.orderprocessor.service;

import java.util.ArrayList;
import java.util.Timer;
import java.lang.ref.SoftReference;

import org.springframework.stereotype.Service;

import tools.descartes.teastore.orderprocessing.orderprocessor.OrderprocessorTask;

@Service
public class OrderprocessorService {

    public static ArrayList<SoftReference<byte[]>> Stuff = null;    
    private boolean _isRunning;

    public void StartService() {       
        
        _isRunning = true;
        
        if (OrderprocessorService.Stuff == null) {
            OrderprocessorService.Stuff = new ArrayList<>();
        }

        Timer t = new Timer();
        int numSeconds = 60;
        if (System.getenv("PROCESSING_RATE_SECONDS") != null) {
            try {
                numSeconds = Integer.parseInt(System.getenv("PROCESSING_RATE_SECONDS"));
            } catch (NumberFormatException nfe) {
                numSeconds = 60;
            }
        }
        t.scheduleAtFixedRate(new OrderprocessorTask(), 0, numSeconds * 1000);
    }

    public boolean IsServiceRunning() {
        return _isRunning;
    }
}
