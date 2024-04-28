package ChessCore;

public class Memento {
   private final ChessBoard mementoBoard;
   private final Player player;
   private final GameStatus mementoStatus;
   private final Move lastMove;

   public Memento(ChessBoard mementoBoard, Player player, GameStatus mementoStatus, Move lastMove) {
      this.mementoBoard = new ChessBoard(mementoBoard);
      this.player = player;
      this.mementoStatus = mementoStatus;
      this.lastMove = lastMove;
   }

   public ChessBoard getMementoBoard() {
      return this.mementoBoard;
   }

   public Player getPlayer() {
      return this.player;
   }

   public GameStatus getMementoStatus() {
      return this.mementoStatus;
   }

   public Move getLastMove() {
      return this.lastMove;
   }
}