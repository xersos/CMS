package be.zwaldeck.zcms.core.piece;

import be.zwaldeck.zcms.piece.api.Piece;
import be.zwaldeck.zcms.utils.reflection.ConstructorUtils;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

@Slf4j
public class PieceScanner {

    public static void scan(String... basePackages) {
        for (var basePackage : basePackages) {
            scan(basePackage);
        }
    }

    public static void scan(String basePackage) {
        var reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Piece.class)
                .stream()
                .filter(PieceScanner::checkConstructor)
                .forEach(PieceScanner::addToRegistry);
    }

    private static boolean checkConstructor(Class<?> clazz) {
        var id = clazz.getAnnotation(Piece.class).id();
        log.info("Checking piece with id {}", id);
        if (!ConstructorUtils.hasNoArgConstructor(clazz)) {
            throw new PieceRegistryException("Can't load piece with id '" + id + "'. It needs to have a no arg constructor!");
        }

        return true;
    }

    private static void addToRegistry(Class<?> clazz) {
        var id = clazz.getAnnotation(Piece.class).id();
        var config = new PieceConfig(clazz.getAnnotation(Piece.class), clazz);
        PieceRegistry.getInstance().put(config);
        log.info("Added piece with id {}", id);
    }
}
