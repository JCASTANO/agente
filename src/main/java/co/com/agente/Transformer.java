package co.com.agente;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class Transformer implements ClassFileTransformer {

    private Class<?> targetClass;
    private ClassLoader targetClassLoader;
    
    private List<String> methods;

    public Transformer(Class<?> clazz, ClassLoader targetClassLoader, List<String> methods) {
        this.targetClass = clazz;
        this.targetClassLoader = targetClassLoader;
        this.methods = methods;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;

        String finalTargetClassName = this.targetClass.getName().replaceAll("\\.", "/"); //replace . with /
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (loader.equals(targetClassLoader)) {
            try {
            	
                ClassPool classPool = ClassPool.getDefault();
                ClassClassPath ccpath = new ClassClassPath(targetClass);
                classPool.insertClassPath(ccpath);
                CtClass ctClass = classPool.get(targetClass.getName());
                
                CtClass ctClassSimpleMetric = classPool.get("co.com.agente.SimpleMetric");
                if(!ctClass.isInterface()) {
                    for (int i = 0; i < methods.size(); i++) {
                        
                    	String method = methods.get(i);
                    	System.out.println("[Agent] Transforming class " + className + " method " + method);
                        
                    	agregarLogMetodo(ctClassSimpleMetric,ctClass, className, method);
    				}
                    byteCode = ctClass.toBytecode();
                    ctClass.detach();
                }
                
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        return byteCode;
    }

	private void agregarLogMetodo(CtClass ctClassSimpleMetric, CtClass ctClass, String className, String metodo) throws NotFoundException, CannotCompileException {
		CtMethod ctMethod = ctClass.getDeclaredMethod(metodo);
		ctMethod.addLocalVariable("xagentSimpleMetric", ctClassSimpleMetric);
		ctMethod.insertBefore("xagentSimpleMetric = co.com.agente.ServicioMedirTiempoXagent.init(\""+className+"\",\""+metodo+"\");");
		ctMethod.insertAfter("co.com.agente.ServicioMedirTiempoXagent.end(xagentSimpleMetric);");
	}
}
