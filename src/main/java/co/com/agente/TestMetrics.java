package co.com.agente;

public class TestMetrics {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("inicio");
		SimpleMetric simpleMetric = ServicioMedirTiempoXagent.init("co/com/sura.ServicioUsuario", "login");
		Thread.sleep(1000);
		ServicioMedirTiempoXagent.end(simpleMetric);
		System.out.println("fin");
	}
}
