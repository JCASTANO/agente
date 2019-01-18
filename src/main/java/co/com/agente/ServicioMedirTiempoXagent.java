package co.com.agente;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServicioMedirTiempoXagent {
	
	private ServicioMedirTiempoXagent() {}

	public static SimpleMetric init(String fullClassName,String methodName) {
		SimpleMetric simpleMetric = new SimpleMetric(getHostName() + "." + fullClassName + "." + methodName);
		simpleMetric.init();
		return simpleMetric;
	}

	private static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "NodoName";
	}
	
	public static void end(SimpleMetric simpleMetric) {
		simpleMetric.end();
	}
}
