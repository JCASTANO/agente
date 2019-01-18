package co.com.agente;

import java.io.File;
import java.net.URLDecoder;

import com.sun.tools.attach.VirtualMachine;

public class Load {

	private static final String WINDOWS = "w";

	public static void main(String[] args) throws Exception {
		cargarAgente(args);
	}

	public static void cargarAgente(String[] args) throws Exception {
		String path = Load.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String decodedPath = URLDecoder.decode(path, "UTF-8");
		
		String agentFilePath = decodedPath;
		File agentFile = new File(WINDOWS.equals(args[1]) ? agentFilePath.substring(1)+"/agentearus.jar" : agentFilePath);
		System.out.println("[Agent] " + agentFile.getPath());
		try {
			String jvmPid = args[0];
			System.out.println("[Agent] Attaching to target JVM with PID: " + jvmPid);
			VirtualMachine jvm = VirtualMachine.attach(jvmPid);
			jvm.loadAgent(agentFile.getAbsolutePath());
			jvm.detach();
			System.out.println("[Agent] Attached to target JVM and loaded Java agent successfully");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
