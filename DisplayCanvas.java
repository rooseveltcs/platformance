import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;

public class DisplayCanvas {
   
   private JPanel panel;
   private JFrame frame;
   private Graphics g;
   private InputHandler inputListener;
   private final int[] RESOLUTION;
   
   public DisplayCanvas(int resX, int resY, int widthMultiplier, int heightMultiplier, String windowName) {
      RESOLUTION = new int[] {resX, resY};
      frame = new JFrame(windowName);
      frame.setSize(resX * widthMultiplier, resY * heightMultiplier);
      panel = new JPanel(new FlowLayout());
      panel.setPreferredSize(new Dimension(resX * widthMultiplier, resY * heightMultiplier));
      BufferedImage img = new BufferedImage(resX * widthMultiplier, resY * heightMultiplier, BufferedImage.TYPE_INT_ARGB);
      g = img.getGraphics();
      JLabel label = new JLabel();
      label.setIcon(new ImageIcon(img));
      panel.add(label);
      frame.add(panel);
      frame.setLayout(new FlowLayout());
      label.setBackground(Color.BLACK);
      frame.setBackground(Color.BLACK);
      panel.setBackground(Color.BLACK);
      frame.setResizable(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
      frame.setFocusable(true);
      inputListener = new InputHandler();
      frame.addKeyListener(inputListener);
   }
   /*public DisplayCanvas(int width, int height, String windowName) {
      DisplayCanvas(width, height, 1, 1, windowName);
   }*/
   
   public Graphics getGraphics() {
      return g;
   }
   
   //returns size of panel as an int[]
   public int[] getDimensions() {
      return new int[] {(int) panel.getWidth(), (int) panel.getHeight()};
   }
   
   //returns boolean array of all keyboard input
   public boolean[] getKeyInputs() {
      return inputListener.getKeyArray();
   }
   
   //changes name for window to given name
   public void setWindowName(String windowName) {
      frame.setTitle(windowName);
   }
   
   //repaints the JFrame
   public void refresh() {
      frame.repaint();
   }
   public void zoomIn() {
      panel.setSize(new Dimension(panel.getWidth() + RESOLUTION[0], panel.getHeight() + RESOLUTION[1]));
   }
   public void zoomOut() {
      if (panel.getWidth() > RESOLUTION[0]) {
         panel.setSize(new Dimension(panel.getWidth() + RESOLUTION[0], panel.getHeight() + RESOLUTION[1]));
      }
   }
   public void zoomIn(int count) {
      for (int i = 0; i < count; i++) {
         zoomIn();
      }
   }
   public void zoomOut(int count) {
      for (int i = 0; i < count; i++) {
         zoomOut();
      }
   }
   public void zoomTo(int zoom) {
      panel.setSize(new Dimension(RESOLUTION[0] * zoom, RESOLUTION[1] * zoom));
   }
}