package be.zwaldeck.zcms.core.piece;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Piece {

    String id();
    String name();
    String group() default "default";
    boolean isContainer() default false;
}
