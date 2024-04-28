
package ChessCore;

import ChessCore.Pieces.Bishop;
import ChessCore.Pieces.King;
import ChessCore.Pieces.Knight;
import ChessCore.Pieces.Pawn;
import ChessCore.Pieces.Piece;
import ChessCore.Pieces.Queen;
import ChessCore.Pieces.Rook;

/**
 *
 * @author Malak
 */
public class PieceFactory {
    public Piece pieceFactory(String pieceName, Player Color) {
    switch (pieceName) {
        case "Pawn":
            return new Pawn(Color);
        case "Rook":
            return new Rook(Color);
        case "Bishop":
            return new Bishop(Color);
        case "King":
            return new King(Color);
        case "Queen":
            return new Queen(Color);
        case "Knight":
            return new Knight(Color);
        default:
            return null;
    }
}
    
}
