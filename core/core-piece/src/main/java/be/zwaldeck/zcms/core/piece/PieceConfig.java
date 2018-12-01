package be.zwaldeck.zcms.core.piece;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PieceConfig {

    @NonNull
    private final Piece piece;

    @NonNull
    private final Class clazz;
}
