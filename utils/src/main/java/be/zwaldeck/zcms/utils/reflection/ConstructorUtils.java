package be.zwaldeck.zcms.utils.reflection;

import java.util.stream.Stream;

public final class ConstructorUtils {

    private ConstructorUtils() {
    }

    public static boolean hasNoArgConstructor(Class<?> clazz) {
        return Stream.of(clazz.getConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }


}
