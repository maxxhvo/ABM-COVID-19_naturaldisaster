package utils;

import java.util.*;

import sim.util.Indexed;

import java.lang.reflect.*;



public class BagPlus implements java.util.Collection, java.io.Serializable, Cloneable, Indexed
{
    private static final long serialVersionUID = 1;
    public SelfRankList objArray;//objs structure
    public Object[] objs = objArray.getArray();//objs array
    public int numObjs = objArray.getSize();
    
    
    public BagPlus() { 
        objArray = new SelfRankList(1);
    }
    
    public BagPlus( int capacity){
        objArray = new SelfRankList(capacity);
    }
    
    public BagPlus( BagPlus other){
        if(other==null) { objArray = new SelfRankList(1);}
        else{
            numObjs = other.objArray.getSize();
            objArray = new SelfRankList(numObjs);
            System.arraycopy(other.objArray.getArray(), 0, objArray.getArray(), 0, numObjs);
        }
    }
    
    public BagPlus( Object[] other){ this(); if(other!=null) addAll(other);}
    
    public BagPlus( Collection other){this(); if(other!=null) addAll(other);}
    
    public int size(){
        return objArray.getSize();
    }
    
    public boolean isEmpty(){
        return objArray.isEmpty();
    }
    
    public boolean addAll( Collection other){
        if(other instanceof BagPlus) return addAll((BagPlus)other);
        return addAll(objArray.getSize(), other.toArray());
    }
    
    public boolean addAll( int index,  Collection other){
        if(other instanceof BagPlus) return addAll(index, (BagPlus)other);
        return addAll(index, other.toArray());
    }
    
    public boolean addAll( Object[] other){ return addAll(numObjs, other);}
    
    public boolean addAll( int index,  Object[] other)
    {
        if (index > objArray.getSize())
        throw new ArrayIndexOutOfBoundsException(index);
        
        if (other.length == 0) return false;
        // make BagPlus big enough
        if (objArray.getSize()+other.length > objArray.getArray().length)
        resize(objArray.getSize()+other.length);
        if (index != objArray.getSize())   // scoot over elements if we're inserting in the middle
        System.arraycopy(objArray.getArray(),index,objArray.getArray(),index+other.length,objArray.getSize() - index);
        System.arraycopy(other,0,objArray.getArray(),index,other.length);
        numObjs += other.length;
        return true;
    }
    
    public boolean addAll( BagPlus other) { return addAll(objArray.getSize(),other); }
    
    public boolean addAll( int index,  BagPlus other)
    {
        
        if (index > objArray.getSize()) 
        throw new ArrayIndexOutOfBoundsException(index);
        // throwArrayIndexOutOfBoundsException(index);
        if (other.objArray.getSize() <= 0) return false;
        // make BagPlus big enough
        if (objArray.getSize()+other.objArray.getSize() > objArray.getArray().length)
        resize(objArray.getSize()+other.objArray.getSize());
        if (index != objArray.getSize())    // scoot over elements if we're inserting in the middle
        System.arraycopy(objArray.getArray(),index,objArray.getArray(),index+other.size(),objArray.getSize() - index);
        System.arraycopy(other.objArray.getArray(),0,objArray.getArray(),index,other.objArray.getSize());
        numObjs += other.objArray.getSize();
        return true;
    }
    
    public Object clone() throws CloneNotSupportedException{
         BagPlus b = (BagPlus)(super.clone());
        b.objs = (Object[])objs.clone();
        return b;
    }
    
    public void resize(int newSize){
        if(objArray.getSize() >= newSize){
            return;
        }
        if(objArray.getSize() * 2 > newSize){
            newSize = objArray.getSize()*2;
        }
        objArray = new SelfRankList(newSize);
    }
    
    public void shrink(int newLen){
        if(newLen < objArray.getSize()) newLen = numObjs;
        if(newLen >= objArray.getArray().length) return;
        objArray = new SelfRankList(newLen);
    }
    
    public Object top(){
        if(objArray.getSize() <= 0) return null;
        else return objArray.getArray()[objArray.getSize()-1];
    }
    
    public Object pop(){
        objArray.remove(objArray.getSize()-1);
        return objArray.getArray()[objArray.getSize()-1];
    }
    
    public boolean add( Object obj){
        objArray.insert(obj);
        return true;
    }
    
    void doubleCapacityPlusOne(){
        objArray = new SelfRankList(objArray.getSize()*2+1);
    }
    
    public boolean contains( Object o){//search
        return objArray.search(o);
    }
    
    public boolean containsAll( Collection c){
         Iterator iterator = c.iterator();
        while(iterator.hasNext()){
            if(!contains(iterator.next())) return false;
        }
        return true;
    }
    
    public Object get( int index){
        return objArray.getArray()[index];
    }
    
    public Object set( int index,  Object element){
        if (index>=objArray.getSize()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
         Object ret = objArray.getArray()[index];
        objArray.getArray()[index] = element;
        return ret;
    }
    
    public Object removeNondestructively(int index)
    {
        if (index>=objArray.getSize()) // || index < 0)
        // throwArrayIndexOutOfBoundsException(index);
        throw new ArrayIndexOutOfBoundsException(index);
        Object ret = objArray.getArray()[index];
        if (index < objArray.getSize() - 1)  // it's not the topmost object, must swap down
        System.arraycopy(objArray.getArray(), index+1, objArray.getArray(), index, objArray.getSize() - index - 1);
        objArray.getArray()[objArray.getSize()-1] = null;  // let GC
        numObjs--;
        return ret;
    }
    
    /** Removes the object, shifting the other objects down. */
    public boolean removeNondestructively(Object o)
    {
        int numObjs = this.objArray.getSize();
        Object[] objs = this.objArray.getArray();
        for(int x=0;x<numObjs;x++)
        if (o==null ?  objs[x]==null :  o==objs[x] || o.equals(objs[x])) 
        {
            removeNondestructively(x);
            return true;
        }
        return false;
    }
    
    public boolean removeAll( Collection c){
        boolean flag = false;
         Iterator iterator = c.iterator();
        while(iterator.hasNext())
        if (remove(iterator.next())) flag = true;
        return flag;
    }
    
    public boolean remove( Object o){
        int index = 0;
        for(int i=0; i<objArray.getSize(); i++){
            if(objArray.getArray()[i] == o){
                index = i;
                break;
            }
        }
        objArray.remove(index);
        if(objArray.search(o)==false){
            return true;
        }
        else { return false;}
    }
    
    public boolean removeMultiply( Object o)
    {
         int numObjs = this.numObjs;
         Object[] objs = this.objs;
        boolean flag = false;
        for(int x=0;x<numObjs;x++)
        if (o==null ?  objs[x]==null :  o==objs[x] || o.equals(objs[x])) 
        {
            flag = true;
            remove(x);
            x--;  // to check the next item swapped in...
        }
        return flag;
    }
    
    public void remove( int index){
        objArray.remove(index);
    }
    
    public void clear(){
         int len = numObjs;
         Object[] o = objs;
        
        for(int i=0; i<len; i++){
            o[i] = null;
            numObjs = 0;
        }
    }
    
    public Object[] toArray(){
         Object[] o = new Object[objArray.getSize()];
        System.arraycopy(objs, 0, o, 0, objArray.getSize());
        return o;
    }
    
    public Object[] toArray(Object[] o)
    {
        if (o.length < objArray.getSize())  // will throw a null pointer exception (properly) if o is null
        o = (Object[])(Array.newInstance(o.getClass().getComponentType(), objArray.getSize()));
        else if (o.length > objArray.getSize())
        o[objArray.getSize()] = null;
        System.arraycopy(objArray.getArray(),0,o,0,objArray.getSize());
        return o;
    } 
    
    public void copyIntoArray( int fromStart,  Object[] to,  int toStart,  int len)
    {
        System.arraycopy(objArray.getArray(), fromStart, to, toStart, len);
    }
    
    public Iterator iterator()
    {
        return new BagPlusIterator(this);
    }
    
    /** Always returns null.  This method is to adhere to Indexed. */
    public Class componentType()
    {
        return null;
    }
    
    /** Sorts the BagPlus according to the provided comparator */
    public void sort( Comparator c) 
    {
        Arrays.sort(objArray.getArray(), 0, objArray.getSize(), c);
    }
    
    /** Sorts the BagPlus under the assumption that all objects stored within are Comparable. */
    public void sort() 
    {
        Arrays.sort(objArray.getArray(), 0, objArray.getSize());
    }
    
    public void fill( Object o)
    {
        // teeny bit faster
         Object[] objs = this.objArray.getArray();
         int numObjs = this.objArray.getSize();
        
        for(int x=0; x < numObjs; x++)
        objs[x] = o;
    }
    
    public void shuffle( Random random)
    {
        // teeny bit faster
         Object[] objs = this.objArray.getArray();
         int numObjs = this.objArray.getSize();
        Object obj;
        int rand;
        
        for(int x=numObjs-1; x >= 1 ; x--)
        {
            rand = random.nextInt(x+1);
            obj = objs[x];
            objs[x] = objs[rand];
            objs[rand] = obj;
        }
    }
    
    /** Shuffles (randomizes the order of) the BagPlus */
    public void shuffle( ec.util.MersenneTwisterFast random)
    {
        // teeny bit faster
         Object[] objs = this.objArray.getArray();
         int numObjs = this.objArray.getSize();
        Object obj;
        int rand;
        
        for(int x=numObjs-1; x >= 1 ; x--)
        {
            rand = random.nextInt(x+1);
            obj = objs[x];
            objs[x] = objs[rand];
            objs[rand] = obj;
        }
    }
    
    /** Reverses order of the elements in the BagPlus */
    public void reverse()
    {
        // teeny bit faster
         Object[] objs = this.objArray.getArray();
         int numObjs = this.objArray.getSize();
         int l = numObjs / 2;
        Object obj;
        for(int x=0; x < l; x++)
        {
            obj = objs[x];
            objs[x] = objs[numObjs - x - 1];
            objs[numObjs - x - 1] = obj;
        }
    }
    
    static class BagPlusIterator implements Iterator, java.io.Serializable
    {
        private static final long serialVersionUID = 1;
        
        int obj = 0;
        BagPlus BagPlus;
        boolean canRemove = false;
        
        public BagPlusIterator( BagPlus BagPlus) { this.BagPlus = BagPlus; }
        
        public boolean hasNext()
        {
            return (obj < BagPlus.objArray.getSize());
        }
        public Object next()
        {
            if (obj >= BagPlus.objArray.getSize()) throw new NoSuchElementException("No More Elements");
            canRemove = true;
            return BagPlus.objArray.getArray()[obj++];
        }
        public void remove()
        {
            if (!canRemove) throw new IllegalStateException("remove() before next(), or remove() called twice");
            // more consistent with the following line than 'obj > BagPlus.numObjs' would be...
            if (obj - 1 >=  BagPlus.objArray.getSize()) throw new NoSuchElementException("No More Elements");
            BagPlus.removeNondestructively(obj-1);
            obj--;
            canRemove = false;
        }
    }
    
    @Override
    public boolean retainAll(Collection c)
    {
        boolean flag = false;
        for(int x=0;x<objArray.getSize();x++)
        if (!c.contains(objArray.getArray()[x]))
        {
            flag = true;
            remove(x);
            x--; // consider the newly-swapped-in item
        }
        return flag;
    }
    
    @Override
    public Object setValue(int index, Object element) {
        if (index>=objArray.getSize()) // || index < 0)
            throw new ArrayIndexOutOfBoundsException(index);
        // throwArrayIndexOutOfBoundsException(index);
        Object returnval = objArray.getArray()[index];
        objArray.getArray()[index] = element;
        return returnval;
    }
    
    @Override
    public Object getValue(int index) throws IndexOutOfBoundsException {
        if (index>=objArray.getSize()) // || index < 0)
            throw new ArrayIndexOutOfBoundsException(index);
        //throwArrayIndexOutOfBoundsException(index);
        return objArray.getArray()[index];
    }
    
    
}


