import java.awt.Image;

public class SpritePackage implements Position {
   
   public String filename;
   public int x;
   public int y;
   public int sizeMultiplierX;
   public int sizeMultiplierY;
   
   public SpritePackage(String iniFilename, int iniX, int iniY, int iniMulX, int iniMulY) {
      filename = iniFilename;
      x = iniX;
      y = iniY;
      sizeMultiplierX = iniMulX;
      sizeMultiplierY = iniMulY;
   }
   public SpritePackage(String iniFilename, int iniX, int iniY, int iniMul) {
      this(iniFilename, iniX, iniY, iniMul, iniMul);
   }
   public SpritePackage(String iniFilename, int iniX, int iniY) {
      this(iniFilename, iniX, iniY, 1);
   }
   
   public int[] getCoords() {
      return new int[] {(int) x, (int) y};
   }
   
   public String getFilename() {
      return filename;
   }
   
   public int[] getSizeMultipliers() {
      return new int[] {sizeMultiplierX, sizeMultiplierY};
   }
   
   public int getWidthMultiplier() {
      return sizeMultiplierX;
   }
   
   public int getHeightMultiplier() {
      return sizeMultiplierY;
   }
   
   public Image getImage() {
      return Client.sprite(filename);
   }
}