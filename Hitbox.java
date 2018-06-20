public class Hitbox implements Position {
   
   private String type;
   private int shape;
   private int x;
   private int y;
   private int width;
   private int height;
   
   public Hitbox (String iniType, int iniShape, int iniX, int iniY, int iniW, int iniH) {
      type = iniType;
      shape = iniShape;
      x = iniX;
      y = iniY;
      width = iniW;
      height = iniH;
   }
   public Hitbox (String iniType, int iniX, int iniY, int iniW, int iniH) {
      this(iniType, 0, iniX, iniY, iniW, iniH);
   }
   public Hitbox (int iniType, int iniX, int iniY, int iniW, int iniH) {
      this(intToLengthString(iniType, 4), iniX, iniY, iniW, iniH);
   }
   
   public int getX() {
      return x;
   }
   public int getY() {
      return y;
   }
   public int[] getCoords() {
      return new int[] {x, y};
   }
   public int getWidth() {
      return width;
   }
   public int getHeight() {
      return height;
   }
   public int[] getDimensions() {
      return new int[] {width, height};
   }
   public int getType() {
      return Integer.parseInt(type.substring(0, Math.min(4, type.length())));
   }
   
   public void setCoords (int[] coords) {
      x = coords[0];
      y = coords[1];
   }
   public void incrementX (int amount) {
      x += amount;
   }
   public void incrementY (int amount) {
      y += amount;
   }
   
   private static String intToLengthString (int num, int length) {
      String ret = num + "";
      if (ret.length() > length) {
         ret = ret.substring(ret.length() - length, ret.length());
      }
      while (ret.length() < length) {
         ret = '0' + ret;
      }
      return ret;
   }
   
   public int[] collide (Hitbox[] testWith, int except) {
      Hitbox hb = new Hitbox(type, x, y, width, height); //copy of this
      int damageSum = 0;
      int newX = x;
      int newY = y;
      int horizColl = 0;
      int vertColl = 0;
      for (int i = 0; i < testWith.length; i++) {
         if (i != except) {
            if (Math.abs(testWith[i].getX() - hb.getX()) < hb.getWidth() / 2 + testWith[i].getWidth() / 2
             && Math.abs(testWith[i].getY() - hb.getY()) < hb.getHeight() / 2 + testWith[i].getHeight() / 2) {
               //System.out.println("collision!");
               if (testWith[i].getType() == 2) {
                  if (Math.abs(testWith[i].getX() - hb.getX()) > Math.abs(testWith[i].getY() - hb.getY())) {
                     if (hb.getX() < testWith[i].getX()) {
                        newX = testWith[i].getX() - (hb.getWidth() / 2 + testWith[i].getWidth() / 2);
                        horizColl = -1;
                     }
                     else {
                        newX = testWith[i].getX() + (hb.getWidth() / 2 + testWith[i].getWidth() / 2);
                        horizColl = 1;
                     }
                  }
                  else {
                     if (hb.getY() < testWith[i].getY()) {
                        newY = testWith[i].getY() - (hb.getHeight() / 2 + testWith[i].getHeight() / 2);
                        vertColl = -1;
                     }
                     else {
                        newY = testWith[i].getY() + (hb.getHeight() / 2 + testWith[i].getHeight() / 2);
                        vertColl = 1;
                     }
                  }
               }
            }
         }
      }
      return new int[] {newX, newY, damageSum, horizColl, vertColl};
   }
}