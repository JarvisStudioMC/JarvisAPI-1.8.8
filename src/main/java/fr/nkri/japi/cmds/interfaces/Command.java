package fr.nkri.japi.cmds.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String permissionNode() default "";
    String[] name() default {};
    boolean isConsole() default false;
}
