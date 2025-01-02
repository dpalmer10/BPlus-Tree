import java.util.ArrayList;

public class BTNodeInternal extends BTNode
{
   public ArrayList<BTNode> children;

   public BTNodeInternal()
   {
      this.keys = new ArrayList<>();
      this.children = new ArrayList<>();
      this.isLeaf = false;
      this.parent = null;
   }

   public void insert(String key, BPlusTree tree)
   {
      int index = findInsertIndex(key);

      if(this.parent == null && this.children.size() == 0){
         this.children.add(tree.root);
      }

      if(this.children.get(index).keys.size() > SIZE){
         this.keys.add(index, key);
         if(this.children.size() == this.keys.size() && this.children.size() <= 4){
            if(this.children.get(index).isLeaf){
               this.children.add(index +1, new BTNodeLeaf());
               BTNodeLeaf prev = (BTNodeLeaf) this.children.get(index);
               BTNodeLeaf cur = (BTNodeLeaf) this.children.get(index +1);
               cur.nextLeaf = prev.nextLeaf;
               prev.nextLeaf = cur;
            }
            index++;
            this.children.get(index).parent = this;
         }
      }

      this.children.get(index).insert(key, tree);

   }

   public void printLeavesInSequence()
   {
      this.children.get(0).printLeavesInSequence();
   }

   public void printStructureWKeys(int tabs)
   {
      int i = keys.size() - 1;
      for(; i >= 0; i--){
         children.get(i+1).printStructureWKeys(tabs + 1);
         for (int j = 0; j < tabs; j++) {
            System.out.print("\t\t");
         }
         System.out.println(keys.get(i));
      }
      children.get(i+1).printStructureWKeys(tabs + 1);
   }

   public int findInsertIndex(String word) {
      int index = 0;
      for(; index < keys.size(); index++){
         if(keys.get(index).compareTo(word) > 0)
            break;
      }
      return index;
   }

   public Boolean rangeSearch(String startWord, String endWord)
   {
      int index = findInsertIndex(startWord);
      return this.children.get(index).rangeSearch(startWord, endWord);
   }

   public Boolean searchWord(String word)
   {
      int index = findInsertIndex(word);
      return this.children.get(index).searchWord(word);
   }

   public void splitInternal(){
      if(this.keys.size() > SIZE && this.children.size() > SIZE + 1){

         // create sibling node
         BTNodeInternal sibling = new BTNodeInternal();

         //give it proper value
         sibling.keys.add(this.keys.get(3));

         //move left child over
         sibling.children.add(this.children.get(3));
         this.children.get(3).parent = sibling;

         //move right child over
         sibling.children.add(this.children.get(4));
         this.children.get(4).parent = sibling;

         //remove children
         this.children.remove(3);
         this.children.remove(3);

         if(this.parent == null) {
            this.parent = new BTNodeInternal();
            this.parent.children.add(this);
            this.parent.keys.add( this.keys.get(2));
         }
         // parent already has values
         else{
            int parIndex = this.parent.findInsertIndex(this.keys.get(2));
            this.parent.keys.add(parIndex, this.keys.get(2));
         }

         int index = this.parent.findInsertIndex(sibling.keys.get(0));
         this.parent.children.add(index, sibling);
         sibling.parent = this.parent;





         //delete keys
         this.keys.remove(2);
         this.keys.remove(2);

         this.parent.splitInternal();
      }
   }

}