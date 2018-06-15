import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;

public class InputHandler implements KeyListener {
   boolean[] keyInputs;
   
   public InputHandler() {
      keyInputs = new boolean[0x100];
   }
   
   @Override
   public void keyTyped(KeyEvent e) {
   }
   @Override
   public void keyPressed(KeyEvent e) {
      //System.out.println("pressed " + e.getKeyCode());
      keyInputs[e.getKeyCode()] = true;
   }
   @Override
   public void keyReleased(KeyEvent e) {
      //System.out.println("released " + e.getKeyCode());
      keyInputs[e.getKeyCode()] = false;
   }
   public boolean[] getKeyArray() {
      return keyInputs;
   }
}