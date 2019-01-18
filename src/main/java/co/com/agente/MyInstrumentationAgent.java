package co.com.agente;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@SuppressWarnings("rawtypes")
public class MyInstrumentationAgent {

	private static final String ARCHIVO_ORIGEN = "clases.txt";

	public static void premain(String agentArgs, Instrumentation instrumentation) {

		System.out.println("[Agent] In premain method " + ARCHIVO_ORIGEN);
		HashMap<Class, List<String>> clases = getDetailsToAnalys(ARCHIVO_ORIGEN, instrumentation);
		System.out.println("[Agent] clases para analizar " + clases.size());

		for (Map.Entry<Class, List<String>> entry : clases.entrySet()) {
			Class clazz = entry.getKey();
			List<String> methods = entry.getValue();

			transformClass(clazz, methods, instrumentation);
		}
	}

	public static void agentmain(String agentArgs, Instrumentation instrumentation) {

		System.out.println("[Agent] In agentmain method " + ARCHIVO_ORIGEN);
		HashMap<Class, List<String>> clases = getDetailsToAnalys(ARCHIVO_ORIGEN, instrumentation);
		System.out.println("[Agent] clases para analizar " + clases.size());

		for (Map.Entry<Class, List<String>> entry : clases.entrySet()) {
			Class clazz = entry.getKey();
			List<String> methods = entry.getValue();

			transformClass(clazz, methods, instrumentation);
		}
	}

	private static void transformClass(Class clazz, List<String> methods, Instrumentation instrumentation) {
		ClassLoader targetClassLoader = null;
		try {
			targetClassLoader = clazz.getClassLoader();
			transform(clazz, methods, targetClassLoader, instrumentation);
			return;
		} catch (Exception ex) {
			System.out.println("[Agent] Class [{}] not found with Class.forName");
		}
	}

	private static void transform(Class<?> clazz, List<String> methods, ClassLoader classLoader,
			Instrumentation instrumentation) {
		Transformer transformer = new Transformer(clazz, classLoader, methods);
		instrumentation.addTransformer(transformer, true);
		try {
			instrumentation.retransformClasses(clazz);
		} catch (Exception ex) {
			throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
		}
	}

	private static HashMap<Class, List<String>> getDetailsToAnalys(String fileName, Instrumentation instrumentation) {

		HashMap<String, List<String>> clasesYmetodos = obtenerClasesYmetodos(fileName);

		System.out.println("[Agent] clases y metodos " + clasesYmetodos);

		HashMap<Class, List<String>> resultado = new HashMap<>();
		
		try {
			List<String> metodos = clasesYmetodos.get("co.com.suaporte.ValidadorRegistrosDos");
			Class clazz = Class.forName("co.com.suaporte.ValidadorRegistrosDos");
			resultado.put(clazz, metodos);
		} catch(Exception e) {
			
		}
		
		Class[] clasesPosiblesParaInstrumentar = instrumentation.getAllLoadedClasses();
		for (int i = 0; i < clasesPosiblesParaInstrumentar.length; i++) {
			Class clazz = clasesPosiblesParaInstrumentar[i];
			try {
				if (clazz.getCanonicalName() != null && clasesYmetodos.containsKey(clazz.getCanonicalName())) {
					System.out.println("[Agent] className: " + clazz.getCanonicalName());

					List<String> metodosParaInstrumentar = clasesYmetodos.get(clazz.getCanonicalName());

					resultado.put(clazz, metodosParaInstrumentar);
				}
			} catch (Error e) {
				
			}
		}
		return resultado;

	}

	private static HashMap<String, List<String>> obtenerClasesYmetodos(String fileName) {
		HashMap<String, List<String>> archivoClases = new HashMap<>();

		// Get file from resources folder
		InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String clase = line.split(";")[0];
				List<String> metodos = Arrays.asList(line.split(";")[1].split(","));
				archivoClases.put(clase, metodos);
			}

		} catch (Exception e) {
			throw new RuntimeException("[Agent] Error obteniendo las clases y metodos", e);
		}
		System.out.println("[Agent] " + archivoClases);
		return archivoClases;
	}

}
