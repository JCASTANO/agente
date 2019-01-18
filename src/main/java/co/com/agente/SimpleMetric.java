package co.com.agente;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

public class SimpleMetric {

    private Counter counter;
    private Timer timer;
    private Timer.Context context;
    private Meter requests;

    public SimpleMetric(String name){
        this.requests = GlobalRegistry.registry().meter(name + ".request");
        this.timer = GlobalRegistry.registry().timer(name + ".time");
        this.counter = GlobalRegistry.registry().counter(name + ".counter");
    }
    
    public void init() {
    	this.context = timer.time();
    	this.counter.inc();
    	this.requests.mark();
    }

    public void end() {
    	this.context.stop();
    }
}
