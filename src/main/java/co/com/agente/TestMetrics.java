package co.com.agente;

public class TestMetrics {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("inicio");
		SimpleMetric simpleMetric = ServicioMedirTiempoXagent.init("co/com/ServicioUsuario", "login");
		Thread.sleep(11000);
		ServicioMedirTiempoXagent.end(simpleMetric);
		
		SimpleMetric simpleMetric2 = ServicioMedirTiempoXagent.init("co/com/ServicioUsuario", "login");
		Thread.sleep(11000);
		ServicioMedirTiempoXagent.end(simpleMetric2);
		Thread.sleep(10000);
		System.out.println("fin");
	}
}
