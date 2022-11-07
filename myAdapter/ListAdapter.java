package myAdapter;

import java.util.NoSuchElementException;
/**
 * Class that implements {@link myAdapter.HList} and is the implementation of the sublist of
 * {@link myAdapter.ListAdapter}<br>
 * <p>
 * <p>
 * This class is only a representation of the main list bounded by two index
 * @author Andrea Bruttomesso
 * @see myAdapter.HList Interface: HList
 */



public class ListAdapter implements HList, HCollection {
    private int from, to; 
    private Vector list;
    private ListAdapter father;
    boolean isFather, isSon;

   /**
    * Creates a new object ListAdapter, by default the size of the list is set to 0 and the
    * indexes go from 0 to 0. This list doesn't any father or son and contains
    * an empty vector.
    */
    public ListAdapter() {
        isFather = false;
        isSon = false;
        from = 0;
        to = 0;
        father = null;
        list = new Vector();
    }
    /**
     * Creates a new object ListAdapter and inserts the elements contained in the parameter
     * collection into this new object. By default this new object isn't nor a father or a son
     * of something else. The size and the vector are copied from the parameter collection
     * 
     * @param coll the Collection from which the new ListAdapter copies the elements
     */
    public ListAdapter(HCollection coll){
        if(coll == null) throw new NullPointerException();
        isFather = false;
        isSon = false;
        father = null;
        list = new Vector();
        HIterator it = coll.iterator();
        while(it.hasNext()){
            list.addElement(it.next());
        }
        from = 0;
        to = coll.size();
    }

    /**
     * Private constructor used only to create a sublist. This object inherits
     * the Vector list of the fatherList but can only access the elements contained
     * between fromIndex and toIndex. This constructor shouldn't be used for any other
     * purpose than creating a sublist.
     * 
     * See {@link #sublist()} for more
     * 
     * @param fatherList    list that backs this sublist, a reference to this object will always be held
     *                      from the son list
     * @param fromIndex     first index at which the sublist has access in the father list
     * @param toIndex       first available index after the elements contained in the sublist
     */
    private ListAdapter(ListAdapter fatherList, int fromIndex, int toIndex){
        isSon = true;
        isFather = false;
        from = fromIndex;
        to = toIndex;
        list = fatherList.list;
        father = fatherList;
    }

    /**
     * Recursive method used for sublists. This increases the father size of a sublist
     * and the invokes it self on the father list in order to manage recursive sublists
     */
    private void addToFather(){
        if(isSon){
            father.to++;
            father.addToFather();
        }
    }

    
    @Override
    public void add(int index, Object obj){
        if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
        list.insertElementAt(obj, from+index);
        to++;
        addToFather();
    }

    @Override
    public boolean add(Object obj){
        list.insertElementAt(obj, to);
        to++;
        addToFather();
        return true;
    }

    @Override
    public boolean addAll(HCollection coll){
        if(coll == null) throw new NullPointerException();
        HIterator it = coll.iterator();
        while(it.hasNext()) add(it.next());
        return true;
    }
    

    @Override
    public boolean addAll(int index, HCollection coll){
        if(coll == null) throw new NullPointerException();
        if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
        HIterator it = coll.iterator();
        for(int i=0; i<coll.size(); i++) add(index+i, it.next());
        return true;
    }

    @Override
    public void clear(){
        for(int i=from; i<to; i++){
            list.removeElementAt(from);
        } 
        if(isSon){
            father.to = father.to - (to-from);
        }
        
        to = from;
    }

    @Override
    public boolean contains(Object obj){
        int i = list.indexOf(obj, from);
        return (i<to && i!=-1);
    }

    @Override
    public boolean containsAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();

        HIterator it = coll.iterator();
        while(it.hasNext()){
            if(!contains(it.next())) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ListAdapter)) return false;
        ListAdapter tmp = (ListAdapter) obj;
        if(tmp.size() != size()) return false;

        Object[] a1, a2;
        a1 = tmp.toArray();
        a2 = toArray();
        for(int i=0; i<size(); i++) 
            if(a1[tmp.from + i] != a2[from + i]) return false;

        return true;
    }

    @Override
    public Object get(int index){
        if(index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        return list.elementAt(from+index);
    }    

    @Override
    public int hashCode(){
        int hashCode = 1;
        
        for(int i=from; i<to; i++){
            hashCode = 31 * hashCode + (list.elementAt(i) == null ? 0 : (list.elementAt(i)).hashCode());
        }

        return hashCode;
    }

    @Override
    public int indexOf(Object obj){
        for(int i = 0; i<size(); i++)
            if(list.elementAt(from+i) == obj) return i;

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public HIterator iterator() {
        return new Iterator();
    }

    @Override
    public int lastIndexOf(Object obj){
        int i = list.lastIndexOf(obj);
        if(i != -1) return i-from;
        return -1;
    }

    @Override
    public HListIterator listIterator(){
        return new ListIterator();
    }

    @Override
    public HListIterator listIterator(int index){
        if(index < 0 || index > size()) throw new IndexOutOfBoundsException();
        return new ListIterator(from+index);
    }

    /**
     * Recursive method used for sublists. This decreases the father size of a sublist
     * and the invokes it self on the father list in order to manage recursive sublists
     */
    private void removeToFather(){
        if(isSon){
            father.to--;
            father.removeToFather();
        }
    }

    @Override
    public Object remove(int index){
        if(index<0 || index>=size()) throw new IndexOutOfBoundsException();
        Object tmp = list.elementAt(from+index);
        list.removeElementAt(from+index);
        to--;
        removeToFather();
        return tmp;
    }

    @Override
    public boolean remove(Object obj){ 
        int i = list.indexOf(obj, from);
        if(i == -1) return false;
        list.removeElementAt(i);
        to--;
        removeToFather();
        return true;
    }

    @Override
    public boolean removeAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();

        boolean modified = false;
        HIterator it = coll.iterator();

        while(it.hasNext()){
            Object obj = it.next();
            while(remove(obj)) modified = true;
        }     

        return modified;
    }

    @Override
    public boolean retainAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();

        boolean modified = false;
        for(int i=from; i<to; i++){
            if(!coll.contains(list.elementAt(i))){
                remove(list.elementAt(i));
                modified = true;
                i--;
            }
        }

        return modified;
    }

    @Override
    public Object set(int index, Object obj){
        if(index<0 || index>=size()) throw new IndexOutOfBoundsException();
        Object tmp = list.elementAt(from+index);
        list.setElementAt(obj, from+index);
        return tmp;
    }

    @Override
    public int size(){ 
        if(to-from > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        return to-from;
    }

    @Override
    public HList subList(int fromIndex, int toIndex){
        if(fromIndex < 0 || toIndex > size()) throw new IndexOutOfBoundsException();
        isFather = true;
        ListAdapter sublist = new ListAdapter(this, from+fromIndex, from+toIndex);
        return sublist;
    }

    @Override
    public Object[] toArray(){
        Object[] tmp = new Object[size()];
        for(int i=0; i<size(); i++) tmp[i] = list.elementAt(from+i);
        return tmp;
    }

    @Override
    public Object[] toArray(Object arrayTarget[]){ //CLDC  NON PUÒ FARE COSE A RUNTIME 1.1
        if(arrayTarget == null) throw new NullPointerException();
    
        if(arrayTarget.length < size())   return toArray();
        
        for(int i=0; i<size(); i++) arrayTarget[i] = list.elementAt(from+i);
        for(int i=size(); i<arrayTarget.length; i++) arrayTarget[i] = null;

        return arrayTarget;
    }



    class Iterator implements HIterator{
        int curr_index;
        boolean call_next, call_remove;
        /**
         * Default constructor for an Iterator object. By default this places the
         * iterator at the first index of the list and sets to false all the
         * boolean parameters
         */
        public Iterator(){
            curr_index = from;
            call_next = false;
            call_remove = false;
        }

        @Override
        public boolean hasNext() {
            return (curr_index < to);
        }

        @Override
        public Object next() {
            if(!hasNext()) throw new NoSuchElementException();
            curr_index++;
            call_next = true;
            call_remove = false;
            return list.elementAt(curr_index-1);
        }

        @Override
        public void remove() {
            if(!call_next || call_remove) throw new IllegalStateException();
            curr_index--;
            list.removeElementAt(curr_index);
            to--;
            call_remove = true;                    
        }
    }

    class ListIterator extends Iterator implements HListIterator{

        boolean call_prev, call_add;
        /**
         * Default constructor for a ListIterator object. By default this places the
         * iterator at the first index of the list and sets to false all the
         * boolean parameters
         */
        public ListIterator(){
            curr_index = 0;
            call_next = false;
            call_remove = false;
            call_prev = false;
            call_add = false;
        }
        /**
         * By default this places the iterator at the index of the list specified by
         * fromIndex and sets to false all the boolean parameters
         * @param fromIndex
         * 
         * @throws IndexOutOfBoundException when the given index is negative or bigger
         *                                  than the list size
         */
        public ListIterator(int fromIndex){ 
            if(fromIndex < 0 || fromIndex > size()) throw new IndexOutOfBoundsException(); 
            curr_index = from + fromIndex;
            call_next = false;
            call_remove = false;
            call_prev = false;
            call_add = false;
        }

        @Override
        public Object next(){
            if(!hasNext()) throw new NoSuchElementException();
            curr_index++;
            call_next = true;
            call_prev = false;
            call_add = false;
            call_remove = false;
            return list.elementAt(curr_index-1);
        }

        @Override
        public boolean hasPrevious() {
            return (curr_index > from);
        }

        @Override
        public Object previous() {
            if(curr_index == 0) throw new NoSuchElementException();
            curr_index--;
            call_next = false;
            call_prev = true;
            call_remove = false;
            call_add = false;
            return list.elementAt(curr_index);
        }

        @Override
        public int nextIndex() {
            return curr_index+1;
        }

        @Override
        public int previousIndex() {
           return curr_index-1;
        }

        @Override
        public void remove() {
            if(!call_next && !call_prev) throw new IllegalStateException();
            if(call_add || call_remove) throw new IllegalStateException();
            if(call_next) curr_index--;
            list.removeElementAt(curr_index);
            call_remove = true;
            call_add = false;
            to--;              
        }
        
        @Override
        public void set(Object obj) {
            if(call_add || call_remove) throw new IllegalStateException();
            if(!call_next && !call_prev) throw new IllegalStateException();
            int i = curr_index;
            if(call_next) i--;
            list.setElementAt(obj, i);
        }

        @Override
        public void add(Object obj) {
            list.insertElementAt(obj,curr_index);
            curr_index++;
            to++;
            call_add = true;
            call_remove = false;
        }

    }

}
