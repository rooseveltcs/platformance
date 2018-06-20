public class Player implements Position, Item {
   
   private double x;
   private double y;
   private boolean facing;
   private int lastHitbox;
   private int jumpTick;
   private int[] lastCollision;
   public boolean delete;
   
   public Player(int initialX, int initialY) {
      x = initialX;
      y = initialY;
      facing = true;
      delete = false;
      jumpTick = -255;
      lastHitbox = -1;
      lastCollision = new int[] {0, 0};
   }
   
   public int[] getCoords() {
      return new int[] {(int) x, (int) y};
   }
   public int getX() {
      return (int) x;
   }
   public int getY() {
      return (int) y;
   }
   
   public String getType() {
      return "Player";
   }
   
   public SpritePackage[] getSprites() {
      return new SpritePackage[] {new SpritePackage("playerWalk1.png", (int) x, (int) y, facing ? 1 : -1, 1)};
   }
   
   public void face (boolean dir) {
      facing = dir;
   }
   
   public void incrementX (double amount) {
      x += amount;
   }
   public void incrementX (int amount) {
      x += amount;
   }
   public void incrementY (double amount) {
      y += amount;
   }
   public void incrementY (int amount) {
      y += amount;
   }
   
   public int updateHitbox(int index) {
      lastHitbox = index;
      return lastHitbox;
   }
   
   public int getHitbox() {
      return lastHitbox;
   }
   
   public boolean shouldDelete() {
      return delete;
   }
   
   public void setJumpTick(int tick) {
      jumpTick = tick;
   }
   
   public int getJumpTick() {
      return jumpTick;
   }
   
   public void setLastCollision(int[] coll) {
      lastCollision = coll;
   }
   
   public int[] getLastCollsion() {
      return lastCollision;
   }
}