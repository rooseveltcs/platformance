import java.util.ArrayList;

public class Camera implements Position {
   private double x;
   private double y;
   private int speed;
   private int goalX;
   private int goalY;
   private ArrayList<int[]> goals;
   private double zoomFactor;
   
   public Camera(int initialX, int initialY) {
      x = goalX = initialX;
      y = goalY = initialY;
      zoomFactor = 1;
      speed = 1;
      goals = new ArrayList<int[]>();
   }
   
   //moves camera toward given goal by speed
   public void updateCamera(int gX, int gY) {
      goalX = gX;
      goalY = gY;
      if (Math.sqrt(Math.pow(x - goalX, 2) + Math.pow(y - goalY, 2)) <= speed) {
         x = goalX;
         y = goalY;
      }
      else {
         double angle = Math.atan((goalY - y) / (goalX - x));
         double changeX = (goalX > x) ? speed * Math.cos(angle) : -speed * Math.cos(angle);
         double changeY = (goalY > y) ? speed * Math.sin(angle) : -speed * Math.sin(angle);
         if (gX > x) {
            changeX = Math.abs(changeX);
         }
         else {
            changeX = -Math.abs(changeX);
         }
         if (gY > y) {
            changeY = Math.abs(changeY);
         }
         else {
            changeY = -Math.abs(changeY);
         }
         x += changeX;
         y += changeY;
      }
   }
   
   //moves camera toward existing goal by speed
   public void updateCamera() {
      updateCamera(goalX, goalY);
   }
   
   public void addGoal(int gX, int gY) {
      goals.add(new int[] {gX, gY});
   }
   
   public void averageGoals() {
      if (goals.size() > 0) {
         int sumX = 0;
         int sumY = 0;
         for (int i = 0; i < goals.size(); i++) {
            sumX += goals.get(i)[0];
            sumY += goals.get(i)[1];
         }
         goalX = sumX / goals.size();
         goalY = sumY / goals.size();
         goals.clear();
      }
   }
   
   //returns camera's current center coordinates
   public int[] getCoords() {
      return new int[] {(int) x, (int) y};
   }
   
   //returns camera's zoom level
   public double getZoom() {
      return zoomFactor;
   }
}