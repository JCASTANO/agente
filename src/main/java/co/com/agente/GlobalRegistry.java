package co.com.agente;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

public class GlobalRegistry {
	
	private static final int PORT_GRAPHITE = 31003;
	private static final String HOST_GRAPHITE = "10.0.1.93";
//	private static final int PORT_GRAPHITE = 2003;
//	private static final String HOST_GRAPHITE = "graphite-service.prometheus";
	
	static MetricRegistry metricRegistry;

	static {
		metricRegistry = new MetricRegistry();

		final Graphite graphite = new Graphite(new InetSocketAddress(HOST_GRAPHITE, PORT_GRAPHITE));
		final GraphiteReporter reporter = GraphiteReporter.forRegistry(metricRegistry)
				.convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build(graphite);

		reporter.start(10, TimeUnit.SECONDS);

		ConsoleReporter reporter1 = ConsoleReporter.forRegistry(metricRegistry).convertRatesTo(TimeUnit.SECONDS)
				.convertDurationsTo(TimeUnit.MILLISECONDS).build();
		reporter1.start(30, TimeUnit.SECONDS);
	}

	private GlobalRegistry() {
	}

	public static MetricRegistry registry() {
		return metricRegistry;
	}
}
