package be.zwaldeck.zcms.core.piece;

import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;

public final class PieceRegistry {

    @Getter
    private static final PieceRegistry instance = new PieceRegistry();

    private HashMap<String, PieceConfig> pieces;
    private boolean frozen;


    private PieceRegistry() {
        pieces = new HashMap<>();
        frozen = false;
    }

    public void put(@NonNull PieceConfig config) {
        var id = config.getPiece().id();

        if (frozen) {
            throw new PieceRegistryException("Can't add piece with id '" + id + "'. The registry is frozen");
        }

        if (pieces.containsKey(id)) {
            throw new PieceRegistryException("Can't add piece with id '" + id + "'. The registry already contains a piece with this id!");
        }

        pieces.put(id, config);
    }

    public PieceConfig get(String id) {
        return pieces.get(id);
    }

    public void freeze() {
        frozen = true;
    }
}
