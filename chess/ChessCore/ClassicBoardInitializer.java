package ChessCore;

import ChessCore.Pieces.*;

public final class ClassicBoardInitializer implements BoardInitializer {
    private static final BoardInitializer instance = new ClassicBoardInitializer();

    private ClassicBoardInitializer() {
    }

    public static BoardInitializer getInstance() {
        return instance;
    }

    @Override
    public Piece[][] initialize() {
        PieceFactory piecef = new PieceFactory();
        Piece[][] initialState = {
            {piecef.pieceFactory("Rook", Player.WHITE), piecef.pieceFactory("Knight", Player.WHITE), piecef.pieceFactory("Bishop", Player.WHITE), piecef.pieceFactory("Queen", Player.WHITE), piecef.pieceFactory("King", Player.WHITE), piecef.pieceFactory("Bishop", Player.WHITE), piecef.pieceFactory("Knight", Player.WHITE), piecef.pieceFactory("Rook", Player.WHITE)},
            {piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE), piecef.pieceFactory("Pawn", Player.WHITE)},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK), piecef.pieceFactory("Pawn", Player.BLACK)},
            {piecef.pieceFactory("Rook", Player.BLACK), piecef.pieceFactory("Knight", Player.BLACK), piecef.pieceFactory("Bishop", Player.BLACK), piecef.pieceFactory("Queen", Player.BLACK), piecef.pieceFactory("King", Player.BLACK), piecef.pieceFactory("Bishop", Player.BLACK), piecef.pieceFactory("Knight", Player.BLACK), piecef.pieceFactory("Rook", Player.BLACK)}
        };
        return initialState;
    }
}
