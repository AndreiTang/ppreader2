package org.andrei.ppreader.service.message;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;

public class PPReaderMessageTool {

    public static Method getMessageMethod(IPPReaderMessage msg,Object that){
        Class<?> cl = that.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderMessageType ct = m.getAnnotation(PPReaderMessageType.class);
            if(ct != null && ct.type().compareTo(msg.type()) == 0){
               return m;
            }
        }
        return null;
    }

    public static void collectInteresting(Object that, HashSet<String> methods){
        Class<?> cl = that.getClass();
        for(Method m : cl.getDeclaredMethods()){
            PPReaderMessageType ct = m.getAnnotation(PPReaderMessageType.class);
            if(ct != null){
                methods.add(ct.type());
            }
        }
    }

}
