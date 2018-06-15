import java.util.*;

public class Room {
   private ArrayList<Item> items;
   private List list;
   private Background bg;
   public Room(Item[] itm) {
      items = arrayItemToArrayList(itm);
   }
   public Item[] getItems() {
      return items.toArray(new Item[0]);
   }
   private ArrayList<Item> arrayItemToArrayList(Item[] itmArr) {
      ArrayList<Item> ret = new ArrayList<Item>();
      for (int i = 0; i < itmArr.length; i++) {
         ret.add(itmArr[i]);
      }
      return ret;
   }
   public void garbageCollect() {
      int i = 0;
      while (i < items.size()) {
         if (items.get(i).shouldDelete()) {
            items.remove(i);
         }
         else {
            i++;
         }
      }
   }
}