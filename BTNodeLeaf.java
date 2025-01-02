import java.util.ArrayList;

public class BTNodeLeaf extends BTNode
{
   public ArrayList<Integer> keyCounts;
   public BTNodeLeaf nextLeaf;

   public BTNodeLeaf()
   {
      this.keys = new ArrayList<>();
      this.keyCounts = new ArrayList<>();
      this.nextLeaf = null;
      this.isLeaf = true;
      this.parent = null;
   }

   public void insert(String word, BPlusTree tree)
   {
      // get the index
      int index = findInsertIndex(word);

      // check if index is already in node
      if(index < keys.size() && keys.get(index).equals(word))
      {
         keyCounts.set(index, keyCounts.get(index) + 1);
      }
      // check if node is full
      // enter new entry if not full
      else
      {
         keys.add(index, word);
         keyCounts.add(index, 1);
      }
      // node is full, split is required
      if(keys.size() > SIZE){
         // get parent
         if(this.parent == null){
            this.parent = new BTNodeInternal();
         }

         // split leaf node
         while(keys.size() > 2){
            this.parent.insert(keys.get(2), tree);
            keyCounts.set(2, keyCounts.get(2) - 1);
            if(keyCounts.get(2) == 0){
               keyCounts.remove(2);
               keys.remove(2);
            }
         }
         this.parent.splitInternal();
      }
   }

   public void printLeavesInSequence()
   {
      for (String key : this.keys) {
         System.out.print(key + " ");
      }
      System.out.println();
      if(nextLeaf != null){
         nextLeaf.printLeavesInSequence();
      }
   }

   public void printStructureWKeys(int tabs)
   {
      for(int i = keys.size() - 1; i >= 0; i--){
         for(int j = 0; j < tabs; j++){
            System.out.print("\t\t");
         }
         System.out.println(keys.get(i));
      }
   }

   public int findInsertIndex(String word) {
      int index = 0;
      for(; index < keys.size(); index++){
         if(keys.get(index).compareTo(word) >= 0)
            break;
      }
      return index;
   }

   public Boolean rangeSearch(String startWord, String endWord)
   {
      boolean valuePrinted = false;
      for(String key: this.keys){
         if(key.compareTo(startWord) >= 0 && key.compareTo(endWord) <= 0){
            System.out.print(key + " ");
            valuePrinted = true;
         }
      }
      if(valuePrinted){
         System.out.println();
         nextLeaf.rangeSearch(startWord, endWord);
      }

      return valuePrinted;

   }

   public Boolean searchWord(String word){
      for(String key : this.keys){
         if(key.compareTo(word) == 0)
            return true;
      }
      return false;
   }
}