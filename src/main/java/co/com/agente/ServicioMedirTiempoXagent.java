package co.com.agente;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;

public class ServicioMedirTiempoXagent {
	
//	private static final String REGEX_PUNTO = "\\.";
//	private static final String SLASH = "/";
//	private static final String GUION = "-";
	private static final String PUNTO = ".";
	private static final String PID_NAME_DEFAULT = "PidNameDefault";
	private static final String NODO_NAME_DEFAULT = "NodoNameDefault";

	private ServicioMedirTiempoXagent() {}

//	public static SimpleMetric init(String fullClassName,String methodName) {
//		SimpleMetric simpleMetric = new SimpleMetric(getHostName() + PUNTO + reemplazar(fullClassName) + GUION + methodName);
//		simpleMetric.init();
//		return simpleMetric;
//	}
	
//	private static String reemplazar(String texto) {
//		return texto.replaceAll(REGEX_PUNTO, GUION).replaceAll(SLASH, GUION);
//	}
	
	public static SimpleMetric init(String fullClassName,String methodName) {
		SimpleMetric simpleMetric = new SimpleMetric(getHostName() + PUNTO + fullClassName + PUNTO + methodName);
		simpleMetric.init();
		return simpleMetric;
	}

	private static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName() + getPid();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NODO_NAME_DEFAULT;
	}
	
	private static String getPid() {
		
		try {
			return "Pid" + ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PID_NAME_DEFAULT;
	}
	
	public static void end(SimpleMetric simpleMetric) {
		simpleMetric.end();
	}
}
