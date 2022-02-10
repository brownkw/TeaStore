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
        t.scheduleAtFixedRate(new OrderprocessorTask(), 0, 60000);
    }

    public boolean IsServiceRunning() {
        return _isRunning;
    }
}
