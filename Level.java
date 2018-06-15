public class Level {
   private Room[] rooms;
   public Level(Room[] r) {
      rooms = r;
   }
   public Room getRoom(int roomNum) {
      return rooms[roomNum];
   }
}