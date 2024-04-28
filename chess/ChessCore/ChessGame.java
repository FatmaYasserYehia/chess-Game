 package ChessCore;

import ChessCore.ChessGame;
import ChessCore.Pieces.Bishop;
import ChessCore.Pieces.King;
import ChessCore.Pieces.Knight;
import ChessCore.Pieces.Pawn;
import ChessCore.Pieces.Piece;
import ChessCore.Pieces.Queen;
import ChessCore.Pieces.Rook;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class ChessGame {
   private ChessBoard board;
   private GameStatus gameStatus;
   private Player whoseTurn;
   private Move lastMove;
   private boolean canWhiteCastleKingSide;
   private boolean canWhiteCastleQueenSide;
   private boolean canBlackCastleKingSide;
   private boolean canBlackCastleQueenSide;
   private Stack<Memento> stackOfMoves;
    public static boolean temp = true;//bt3t awel  undo

   protected ChessGame(BoardInitializer boardInitializer) {
      this.gameStatus = GameStatus.IN_PROGRESS;
      this.whoseTurn = Player.WHITE;
      this.canWhiteCastleKingSide = true;
      this.canWhiteCastleQueenSide = true;
      this.canBlackCastleKingSide = true;
      this.canBlackCastleQueenSide = true;
      this.stackOfMoves = new Stack();
      this.board = new ChessBoard(boardInitializer.initialize());
      this.stackOfMoves.push(this.takeSnapshot());
   }

   public Memento takeSnapshot() {
     // System.out.println("inside takesnapshot");
      return new Memento(this.board, this.whoseTurn, this.gameStatus, this.lastMove);
   }

   public boolean isCanWhiteCastleKingSide() {
      return this.canWhiteCastleKingSide;
   }

   public boolean isCanWhiteCastleQueenSide() {
      return this.canWhiteCastleQueenSide;
   }

   public boolean isCanBlackCastleKingSide() {
      return this.canBlackCastleKingSide;
   }

   public boolean isCanBlackCastleQueenSide() {
      return this.canBlackCastleQueenSide;
   }

   protected boolean isValidMove(Move move) {
      if (this.isGameEnded()) {
         return false;
      } else {
         Piece pieceAtFrom = this.board.getPieceAtSquare(move.getFromSquare());
         if (pieceAtFrom != null && pieceAtFrom.getOwner() == this.whoseTurn && pieceAtFrom.isValidMove(move, this)) {
            Piece pieceAtTo = this.board.getPieceAtSquare(move.getToSquare());
            return pieceAtTo != null && pieceAtTo.getOwner() == this.whoseTurn ? false : this.isValidMoveCore(move);
         } else {
            return false;
         }
      }
   }

   public Move getLastMove() {
      return this.lastMove;
   }

   public Player getWhoseTurn() {
      return this.whoseTurn;
   }

   public ChessBoard getBoard() {
      return this.board;
   }

   protected abstract boolean isValidMoveCore(Move move);

   public boolean isTherePieceInBetween(Move move) {
      return this.board.isTherePieceInBetween(move);
   }

   public boolean hasPieceIn(Square square) {
      return this.board.getPieceAtSquare(square) != null;
   }

   public boolean hasPieceInSquareForPlayer(Square square, Player player) {
      Piece piece = this.board.getPieceAtSquare(square);
      return piece != null && piece.getOwner() == player;
   }

   public boolean makeMove(Move move) {
      if (!this.isValidMove(move)) {
         return false;
      } else {
         Square fromSquare = move.getFromSquare();
         Piece fromPiece = this.board.getPieceAtSquare(fromSquare);
         if (fromPiece instanceof King) {
            if (((Piece)fromPiece).getOwner() == Player.WHITE) {
               this.canWhiteCastleKingSide = false;
               this.canWhiteCastleQueenSide = false;
            } else {
               this.canBlackCastleKingSide = false;
               this.canBlackCastleQueenSide = false;
            }
         }

         if (fromPiece instanceof Rook) {
            if (((Piece)fromPiece).getOwner() == Player.WHITE) {
               if (fromSquare.getFile() == BoardFile.A && fromSquare.getRank() == BoardRank.FIRST) {
                  this.canWhiteCastleQueenSide = false;
               } else if (fromSquare.getFile() == BoardFile.H && fromSquare.getRank() == BoardRank.FIRST) {
                  this.canWhiteCastleKingSide = false;
               }
            } else if (fromSquare.getFile() == BoardFile.A && fromSquare.getRank() == BoardRank.EIGHTH) {
               this.canBlackCastleQueenSide = false;
            } else if (fromSquare.getFile() == BoardFile.H && fromSquare.getRank() == BoardRank.EIGHTH) {
               this.canBlackCastleKingSide = false;
            }
         }

         if (fromPiece instanceof Pawn && move.getAbsDeltaX() == 1 && !this.hasPieceIn(move.getToSquare())) {
            this.board.setPieceAtSquare(this.lastMove.getToSquare(), (Piece)null);
         }

         if (fromPiece instanceof Pawn) {
            BoardRank toSquareRank = move.getToSquare().getRank();
            if (toSquareRank == BoardRank.FIRST || toSquareRank == BoardRank.EIGHTH) {
               Player playerPromoting = toSquareRank == BoardRank.EIGHTH ? Player.WHITE : Player.BLACK;
               PawnPromotion promotion = move.getPawnPromotion();
               
               //pawnPromotion(move.getToSquare(), promotion);
//               switch (promotion) {
//                    case Queen:
//                        fromPiece = new Queen(playerPromoting);
//                        break;
//                    case Rook:
//                        fromPiece = new Rook(playerPromoting);
//                        break;
//                    case Knight:
//                        fromPiece = new Knight(playerPromoting);
//                        break;
//                    case Bishop:
//                        fromPiece = new Bishop(playerPromoting);
//                        break;
//                    case None:
//                        throw new RuntimeException("Pawn moving to last rank without promotion being set. This should NEVER happen!");
//                }
            }
         }

         if (fromPiece instanceof King && move.getAbsDeltaX() == 2) {
            Square toSquare = move.getToSquare();
            Piece rook;
            Square a8;
            Square d8;
            if (toSquare.getFile() == BoardFile.G && toSquare.getRank() == BoardRank.FIRST) {
               a8 = new Square(BoardFile.H, BoardRank.FIRST);
               d8 = new Square(BoardFile.F, BoardRank.FIRST);
               rook = this.board.getPieceAtSquare(a8);
               this.board.setPieceAtSquare(a8, (Piece)null);
               this.board.setPieceAtSquare(d8, rook);
            } else if (toSquare.getFile() == BoardFile.G && toSquare.getRank() == BoardRank.EIGHTH) {
               a8 = new Square(BoardFile.H, BoardRank.EIGHTH);
               d8 = new Square(BoardFile.F, BoardRank.EIGHTH);
               rook = this.board.getPieceAtSquare(a8);
               this.board.setPieceAtSquare(a8, (Piece)null);
               this.board.setPieceAtSquare(d8, rook);
            } else if (toSquare.getFile() == BoardFile.C && toSquare.getRank() == BoardRank.FIRST) {
               a8 = new Square(BoardFile.A, BoardRank.FIRST);
               d8 = new Square(BoardFile.D, BoardRank.FIRST);
               rook = this.board.getPieceAtSquare(a8);
               this.board.setPieceAtSquare(a8, (Piece)null);
               this.board.setPieceAtSquare(d8, rook);
            } else if (toSquare.getFile() == BoardFile.C && toSquare.getRank() == BoardRank.EIGHTH) {
               a8 = new Square(BoardFile.A, BoardRank.EIGHTH);
               d8 = new Square(BoardFile.D, BoardRank.EIGHTH);
               rook = this.board.getPieceAtSquare(a8);
               this.board.setPieceAtSquare(a8, (Piece)null);
               this.board.setPieceAtSquare(d8, rook);
            }
         }

         this.board.setPieceAtSquare(fromSquare, (Piece)null);
         this.board.setPieceAtSquare(move.getToSquare(), (Piece)fromPiece);
         this.whoseTurn = Utilities.revertPlayer(this.whoseTurn);
         this.lastMove = move;
         this.updateGameStatus();
         
         this.stackOfMoves.add(this.takeSnapshot());
         return true;
      }
   }

   private void updateGameStatus() {
      Player whoseTurn = this.getWhoseTurn();
      boolean isInCheck = Utilities.isInCheck(whoseTurn, this.getBoard());
      boolean hasAnyValidMoves = this.hasAnyValidMoves();
      if (isInCheck) {
         if (!hasAnyValidMoves && whoseTurn == Player.WHITE) {
            this.gameStatus = GameStatus.BLACK_WON;
         } else if (!hasAnyValidMoves && whoseTurn == Player.BLACK) {
            this.gameStatus = GameStatus.WHITE_WON;
         } else if (whoseTurn == Player.WHITE) {
            this.gameStatus = GameStatus.WHITE_UNDER_CHECK;
         } else {
            this.gameStatus = GameStatus.BLACK_UNDER_CHECK;
         }
      } else if (!hasAnyValidMoves) {
         this.gameStatus = GameStatus.STALEMATE;
      } else {
         this.gameStatus = GameStatus.IN_PROGRESS;
      }

      if (this.isInsufficientMaterial()) {
         this.gameStatus = GameStatus.INSUFFICIENT_MATERIAL;
      }

   }

   public GameStatus getGameStatus() {
      return this.gameStatus;
   }

   public boolean isGameEnded() {
      return this.gameStatus == GameStatus.WHITE_WON || this.gameStatus == GameStatus.BLACK_WON || this.gameStatus == GameStatus.STALEMATE || this.gameStatus == GameStatus.INSUFFICIENT_MATERIAL;
   }

   private boolean isInsufficientMaterial() {
      int whiteBishopCount = 0;
      int blackBishopCount = 0;
      int whiteKnightCount = 0;
      int blackKnightCount = 0;
      BoardFile[] var5 = BoardFile.values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BoardFile file = var5[var7];
         BoardRank[] var9 = BoardRank.values();
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            BoardRank rank = var9[var11];
            Piece p = this.getPieceAtSquare(new Square(file, rank));
            if (p != null && !(p instanceof King)) {
               if (p instanceof Bishop) {
                  if (p.getOwner() == Player.WHITE) {
                     ++whiteBishopCount;
                  } else {
                     ++blackBishopCount;
                  }
               } else {
                  if (!(p instanceof Knight)) {
                     return false;
                  }

                  if (p.getOwner() == Player.WHITE) {
                     ++whiteKnightCount;
                  } else {
                     ++blackKnightCount;
                  }
               }
            }
         }
      }

      boolean insufficientForWhite = whiteKnightCount + whiteBishopCount <= 1;
      boolean insufficientForBlack = blackKnightCount + blackBishopCount <= 1;
      return insufficientForWhite && insufficientForBlack;
   }

   private boolean hasAnyValidMoves() {
      BoardFile[] var1 = BoardFile.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         BoardFile file = var1[var3];
         BoardRank[] var5 = BoardRank.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            BoardRank rank = var5[var7];
            if (!this.getAllValidMovesFromSquare(new Square(file, rank)).isEmpty()) {
               return true;
            }
         }
      }

      return false;
   }

   public List<Square> getAllValidMovesFromSquare(Square square) {
      ArrayList<Square> validMoves = new ArrayList();
      BoardFile[] var3 = BoardFile.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BoardFile i = var3[var5];
         BoardRank[] var7 = BoardRank.values();
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BoardRank j = var7[var9];
            Square sq = new Square(i, j);
            if (this.isValidMove(new Move(square, sq, PawnPromotion.Queen))) {
               validMoves.add(sq);
            }
         }
      }

      return validMoves;
   }

   public Piece getPieceAtSquare(Square square) {
      return this.board.getPieceAtSquare(square);
   }
   
   public void pawnPromotion(Square pawn ,Square nextSquare,Piece promotedPiece){
        if (promotedPiece != null) {
        this.board.setPieceAtSquare(pawn, null);
        this.board.setPieceAtSquare(nextSquare, promotedPiece);
    }
   }
   
   public void undoMove() {
       if(temp){
          stackOfMoves.pop();
       }
       temp=false;
      if (!this.stackOfMoves.isEmpty()) {
         //System.out.println("inside undo move");
         this.restore((Memento)this.stackOfMoves.pop());
      } else {
         //System.out.println("inside undo move stack is emptyy");
      }

   }

   public void restore(Memento memento) {
      this.board = new ChessBoard(memento.getMementoBoard());
      this.gameStatus = memento.getMementoStatus();
      this.whoseTurn = memento.getPlayer();
      this.lastMove = memento.getLastMove();
      //System.out.println("inside restore");
   }
}