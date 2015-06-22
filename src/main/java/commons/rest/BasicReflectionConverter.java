package commons.rest;

import javax.ejb.Stateless;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * This converter may be used as easy drop-in code,
 * but as reflection, which this class uses, is computationally expensive
 * it is generally more sensible to write your own converter by hand
 *
 * I have performance tested this class, and after a few million method calls,
 * it really becomes a bottleneck.
 * For small amounts of data however, this is really convenient!
 *
 * The exact stats:
 * ~700 million method calls (read and write added) take 333ms on a AMD FX6300 @ 4GHz
 * ~700 million normal converter method calls take up somewhere between 8 and 12ms on the same CPU,
 * so this really is a magnitude of performance
 *
 * @param <E> Entity to convert
 * @param <R> Representation to convert
 */
@Stateless
public class BasicReflectionConverter<E, R> implements Converter<E, R> {

    private Map<Method, Method> methodMapER = new HashMap<>();
    private Map<Method, Method> methodMapRE = new HashMap<>();
    private boolean mappingGenerated;

    public R convertTo(E e, R r) {
        if (e == null || r == null) return null;
        if (!mappingGenerated) generateMapping(e, r);

        try {
            for (Method read : methodMapER.keySet()) {
                Method write = methodMapER.get(read);
                write.invoke(r, read.invoke(e));
            }
            return r;
        } catch (InvocationTargetException | IllegalAccessException ie) {
            ie.printStackTrace();
            return null;
        }
    }

    public E convertFrom(R r, E e) {
        if (e == null || r == null) return null;
        if (!mappingGenerated) generateMapping(e, r);

        try {
            for (Method read : methodMapRE.keySet()) {
                Method write = methodMapRE.get(read);
                write.invoke(e, read.invoke(r));
            }
            return e;
        } catch (InvocationTargetException | IllegalAccessException ie) {
            ie.printStackTrace();
            return null;
        }
    }

    private void generateMapping(E e, R r) {
        try {
            PropertyDescriptor[] rPropertyDescriptors = Introspector.getBeanInfo(r.getClass()).getPropertyDescriptors();
            PropertyDescriptor[] ePropertyDescriptors = Introspector.getBeanInfo(e.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor eprop : ePropertyDescriptors) {
                for (PropertyDescriptor rprop : rPropertyDescriptors) {
                    if (rprop.getName() == eprop.getName()) {
                        if (eprop.getReadMethod() != null && rprop.getWriteMethod() != null) {
                            methodMapER.put(eprop.getReadMethod(), rprop.getWriteMethod());
                        }
                        if (rprop.getReadMethod() != null && eprop.getWriteMethod() != null) {
                            methodMapRE.put(rprop.getReadMethod(), eprop.getWriteMethod());
                        }
                    }
                }
            }
            mappingGenerated = true;
        } catch (IntrospectionException ie) {
            ie.printStackTrace();
        }
    }

}
