package org.arep;
import org.arep.Annotation.Component;
import org.arep.Annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
public class ComponentLoader {
    private  static Map<String, Method> servicios = new HashMap<>();
    public static void main(String[] args)throws ClassNotFoundException,InvocationTargetException, IllegalAccessException{
        cargarComponentes(args);
        System.out.println(ejecutar("/hello", "?name=pedro&sn=perez"));
    }
    public static String  ejecutar(String ruta, String param) throws InvocationTargetException, IllegalAccessException {
        return servicios.get(ruta).invoke(null,param)+"";
    }
    public static  void cargarComponentes(String[] args) throws ClassNotFoundException {
        for (String arg:args){
            Class<?> c = Class.forName(arg);
            if(c.isAnnotationPresent(Component.class)){
                Method[] metodos= c.getDeclaredMethods();
                for(Method m:metodos){
                    if(m.isAnnotationPresent(RequestMapping.class)){
                        //extraer el valor del parametro
                        String ruta =m.getAnnotation(RequestMapping.class).value();
                        //extraer el nombre del metodo
                        System.out.println("cargando metodo:"+m.getName());
                        //crear la lista de tipos del nombre del metodo
                        servicios.put(ruta,m);
                        //obtener el metodo
                        //agregar metodo

                    }
                }
            }
        }
    }
}
