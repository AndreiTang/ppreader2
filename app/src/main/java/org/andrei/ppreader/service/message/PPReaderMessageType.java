package org.andrei.ppreader.service.message;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(value=METHOD)
@Retention(value=RUNTIME)
public @interface PPReaderMessageType {

    public String type();

}
