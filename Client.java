import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

public class Client {

   public static boolean running = true;
   public static int tickCount = 0;
   public static Camera camera = new Camera(160, 120);
   public static int[] keyInputs = new int[0x100];
   public static String spriteFolder = "sprites";
   public static String[] spriteFilenames;
   public static Image[] sprites;
   public static ArrayList<SpritePackage> spritesToDraw;
   public static Hitbox[] hitboxes;
   public static int roomNum;
   public static Level currentLevel;
   public static final int[] DIMENSIONS = {320, 240};

   public static void main(String[] args) throws FileNotFoundException {
      DisplayCanvas canvas = new DisplayCanvas(DIMENSIONS[0], DIMENSIONS[1], 2, 2, "Platformance");
      File folder = new File(spriteFolder);
      File[] spriteFiles = folder.listFiles();
      sprites = new Image[spriteFiles.length];
      spriteFilenames = new String[spriteFiles.length];
      for (int i = 0; i < spriteFiles.length; i++) {
         sprites[i] = ((new ImageIcon(spriteFolder + "/" + spriteFiles[i].getName())).getImage());
         spriteFilenames[i] = spriteFiles[i].getName();
      }
      currentLevel = createLevels()[0];
      spritesToDraw = new ArrayList<SpritePackage>();
      roomNum = 0;
      hitboxes = new Hitbox[0];
      
      while (running) {
         if (System.nanoTime() % (50000/3) == 0) {
            if(!tick(canvas, keyInputs)) {
               running = false;
            }
         }
      }
   }
   
   //runs 60 times per second; main game loop
   public static boolean tick(DisplayCanvas canvas, int[] keyInputs) {
      tickCount++;
      updateKeyInputs(canvas, keyInputs);
      ArrayList<Hitbox> newHitboxes = new ArrayList<Hitbox>();
      Graphics g = canvas.getGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, canvas.getDimensions()[0], canvas.getDimensions()[1]);
      if (keyInputs[38] == 2) {
         g.setColor(Color.BLUE);
      }
      else if (keyInputs[38] == 1) {
         g.setColor(Color.GREEN);
      }
      else if (keyInputs[38] == -1) {
         g.setColor(Color.RED);
      }
      else {
         g.setColor(Color.BLACK);
      }
      //drawSprite(canvas, g, "java.png", 16, 16, camera);
      //drawSprite(canvas, g, new SpritePackage("java.png", 16, 16), camera);
      //camera.updateCamera(0, 0);
      canvas.refresh();
      
      Room currentRoom = currentLevel.getRoom(roomNum);
      Item[] itemArray = currentRoom.getItems();
      
      //Item loop
      for (int i = 0; i < itemArray.length; i++) {
         if (itemArray[i].getType() == "Player") {
            Player player = (Player) itemArray[i];
            
            boolean canJump = false;
            
            if (player.getHitbox() >= 0) {
               Hitbox hb = hitboxes[player.getHitbox()];
               int[] ret = hb.collide(hitboxes, player.getHitbox());
               if (ret[1] > player.getY()) {
                  player.setJumpTick(-255);
               }
               player.incrementX(ret[0] - player.getX());
               player.incrementY(ret[1] - player.getY());
               if (ret[4] == -1) {
                  canJump = true;
               }
               player.setLastCollision(new int[] {ret[3], ret[4]});
            }
            
            if (keyInputs[39] >= 1) {
               player.incrementX((keyInputs[16] >= 1) ? 2 : 1);
            }
            if (keyInputs[37] >= 1) {
               player.incrementX((keyInputs[16] >= 1) ? -2 : -1);
            }
            if (keyInputs[39] == 2) {
               player.face(true);
            }
            if (keyInputs[37] == 2) {
               player.face(false);
            }
            if (keyInputs[39] >= 1 && keyInputs[37] <= 0) {
               player.face(true);
            }
            if (keyInputs[37] >= 1 && keyInputs[39] <= 0) {
               player.face(false);
            }
            if (keyInputs[90] == 2 && canJump) {
               player.setJumpTick(tickCount);
            }
            if (keyInputs[90] == 1 && player.getJumpTick() + 128 > tickCount) {
               player.incrementY(-2);
            }
            if (keyInputs[90] <= 0) {
               player.setJumpTick(-255);
            }
            player.incrementY(1);
            camera.addGoal(player.getX(), player.getY());
            player.updateHitbox(newHitboxes.size());
            newHitboxes.add(new Hitbox(0, player.getX(), player.getY(), 32, 64));
         }
         else if (itemArray[i].getType() == "Tilemap") {
            Tilemap tilemap = (Tilemap) itemArray[i];
            Hitbox[] hboxesRet = tilemap.getHitboxes();
            for (int hbox = 0; hbox < hboxesRet.length; hbox++) {
               newHitboxes.add(hboxesRet[hbox]);
            }
         }
         else {
            
         }
         camera.averageGoals();
         camera.updateCamera();
         SpritePackage[] spritesRet = itemArray[i].getSprites();
         currentRoom = currentRoom;
         for (int sprt = 0; sprt < spritesRet.length; sprt++) {
            spritesToDraw.add(spritesRet[sprt]);
         }
      }
      
      //draw loop
      for (int sprt = 0; sprt < spritesToDraw.size(); sprt++) {
         drawSprite(canvas, g, spritesToDraw.get(sprt), camera);
      }
      /*for (int i = 0; i < hitboxes.length; i++) {
         
      }*/
      spritesToDraw.clear();
      drawSprite(canvas, g, "test.png", 0 ,0, camera);
      
      hitboxes = newHitboxes.toArray(new Hitbox[0]);
      
      return true;
   }
   
   //updates keyInput array based on changes in what's being pressed
   public static void updateKeyInputs(DisplayCanvas canvas, int[] keyInputs) {
      boolean[] currentInputs = canvas.getKeyInputs();
      for (int i = 0; i < keyInputs.length; i++) {
         if (keyInputs[i] <= 0 && currentInputs[i]) {
            keyInputs[i] = 2;
         }
         else if (keyInputs[i] >= 1 && currentInputs[i]) {
            keyInputs[i] = 1;
         }
         else if (keyInputs[i] >= 1 && !currentInputs[i]) {
            keyInputs[i] = -1;
         }
         else {
            keyInputs[i] = 0;
         }
      }
   }
   
   //draws sprite given camera positioning
   public static void drawSprite(DisplayCanvas canvas, Graphics g, String filename, int x, int y, int width, int height, Camera cam) {
      int[] camDisplace = cam.getCoords();
      if (Math.abs(camDisplace[0] - x) < (width + DIMENSIONS[0]) / 2 && Math.abs(camDisplace[1] - y) < (height + DIMENSIONS[1]) / 2) {
         camDisplace[0] = DIMENSIONS[0] / 2 - camDisplace[0];
         camDisplace[1] = DIMENSIONS[1] / 2 - camDisplace[1];
         Image img = sprite(filename);
         double[] zoomMultiplier = new double[] {canvas.getDimensions()[0] / DIMENSIONS[0] * cam.getZoom(), canvas.getDimensions()[1] / DIMENSIONS[1] * cam.getZoom()};
         int realX = (int) ((x + camDisplace[0] - Math.abs(width / 2)) * zoomMultiplier[0]);
         if (width < 0) {
            realX += width * -zoomMultiplier[0];
         }
         int realY = (int) ((y + camDisplace[1] - Math.abs(height / 2)) * zoomMultiplier[1]);
         if (height < 0) {
            realY += height * -zoomMultiplier[1];
         }
         g.drawImage(img, realX, realY, (int) (width * zoomMultiplier[0]), (int) (height * zoomMultiplier[1]), null);
      }
   }
   
   //draws sprite with default width & height
   public static void drawSprite(DisplayCanvas canvas, Graphics g, String filename, int x, int y, Camera cam) {
      drawSprite(canvas, g, filename, x, y, sprite(filename).getWidth(null), sprite(filename).getHeight(null), cam);
   }
   public static void drawSprite(DisplayCanvas canvas, Graphics g, SpritePackage sprt, Camera cam) {
      drawSprite(canvas, g, sprt.getFilename(), sprt.getCoords()[0], sprt.getCoords()[1], sprite(sprt.getFilename()).getWidth(null) * sprt.getWidthMultiplier(), sprite(sprt.getFilename()).getHeight(null) * sprt.getHeightMultiplier(), cam);
   }
   
   //returns an image object for a given filename from the sprites folder
   public static Image sprite(String filename) {
      for (int i = 0; i < spriteFilenames.length; i++) {
         if (spriteFilenames[i].equals(filename)) {
            return sprites[i];
         }
      }
      return sprite("null.png");
   }
   
   public static Level[] createLevels() throws FileNotFoundException {
      Level[] ret = new Level[] {
         new Level(new Room[] {
            new Room(new Item[] {
               new Tilemap(128, -16, new String[][] {
                  new String[] {"0014", "0006", "0006", "0006", "0006", "0006", "0006", "0006", "0004", "0004", "0004", "0005"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0010", "0002", "0002", "0003"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0010", "0002", "0002", "0003"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0010", "0002", "0002", "0003"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0010", "0002", "0002", "0003"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0016", "0008", "0008", "0003"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0015", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0015", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0013", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0011", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0017", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0000", "0000", "0000", "0000", "0018", "0000", "0000", "0000", "0000", "0000", "0000", "0011"},
                  new String[] {"0014", "0006", "0006", "0006", "0006", "0006", "0006", "0006", "0006", "0006", "0006", "0009"},

               }),
               new Player(16 - 128, -48)
            })
         })
      };
      return ret;
   }
}