package be.zwaldeck.zcms.core.piece;

import be.zwaldeck.zcms.piece.api.Piece;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PieceConfig {

    public Piece getPiece() {
        return piece;
    }

    public Class getClazz() {
        return clazz;
    }

    @NonNull
    private final Piece piece;

    @NonNull
    private final Class clazz;
}
