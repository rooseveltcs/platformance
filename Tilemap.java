import java.util.*;
import java.io.*;

public class Tilemap implements Position, Item {
   private int x;
   private int y;
   private String[][] blocks;
   private final String[] blockFiles;
   private final int[] blockBehaviors;
   public boolean delete;
   
   public Tilemap(int initialX, int initialY, String[][] blcs) throws FileNotFoundException {
      ArrayList<String> albf = new ArrayList<String>();
      ArrayList<Integer> albb = new ArrayList<Integer>();
      Scanner scn = new Scanner(new File("blocks.dat"));
      while (scn.hasNextLine()) {
         int id = scn.nextInt();
         //albb.ensureCapacity(id);
         //albf.ensureCapacity(id);
         extendIntArrList(albb, id);
         extendStrArrList(albf, id);
         albb.set(id, scn.nextInt());
         albf.set(id, scn.next());
      }
      blockFiles = new String[albf.size()];
      for (int i = 0; i < albf.size(); i++) {
         if (albf.get(i) != "") {
            blockFiles[i] = albf.get(i);
         }
         else {
            blockFiles[i] = "null.png";
         }
      }
      blockBehaviors = new int[albb.size()];
      for (int i = 0; i < albb.size(); i++) {
         blockBehaviors[i] = albb.get(i);
      }
      x = initialX;
      y = initialY;
      blocks = blcs;
      delete = false;
   }
   
   public int[] getCoords() {
      return new int[] {x, y};
   }
   
   public String getType() {
      return "Tilemap";
   }
   
   public SpritePackage[] getSprites() {
      ArrayList<SpritePackage> sprts = new ArrayList<SpritePackage>();
      for (int x = 0; x < blocks.length; x++) {
         for (int y = 0; y < blocks[x].length; y++) {
            sprts.add(new SpritePackage(blockFiles[Integer.parseInt(blocks[x][y].substring(0, 4))], this.x + x * 32 + 16 - blocks.length * 16, this.y + y * 32 + 16 - blocks[0].length * 16));
         }
      }
      return sprts.toArray(new SpritePackage[0]);
   }
   
   public Hitbox[] getHitboxes() {
      ArrayList<Hitbox> hboxes = new ArrayList<Hitbox>();
      for (int x = 0; x < blocks.length; x++) {
         for (int y = 0; y < blocks[x].length; y++) {
            hboxes.add(new Hitbox(blockBehaviors[Integer.parseInt(blocks[x][y].substring(0, 4))], this.x + x * 32 + 16 - blocks.length * 16, this.y + y * 32 + 16 - blocks[0].length * 16, 32, 32));
         }
      }
      return hboxes.toArray(new Hitbox[0]);
   }
   
   public boolean shouldDelete() {
      return delete;
   }
   
   private void extendIntArrList(ArrayList<Integer> arrList, int length) {
      while (arrList.size() - 1< length) {
         arrList.add(0);
      }
   }
   private void extendStrArrList(ArrayList<String> arrList, int length) {
      while (arrList.size() - 1 < length) {
         arrList.add("");
      }
   }
}