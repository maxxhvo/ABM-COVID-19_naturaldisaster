package utils;


public class SelfRankList {//a variation of self organizing list
    private Object[] array;
    private int[] count;//tracks mostly used element, move to front
    private int size;

    public SelfRankList(final int listSize){
        array = new Object[listSize];
        count = new int[listSize];
        size = 0;//used elements number
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull(){
        return size == array.length;
    }

    public void makeEmpty(){
        final int len = array.length;
        array = new Object[len];
        count = new int[len];
        size = 0;
    }

    public int getSize(){
        return size;
    }

    public Object[] getArray(){
        return array;
    }

    public void insert(final Object val){
        if(isFull()){
            System.out.println("Array is full");
            return;
        }
        array[size] = val;
        count[size] = 0;
        size++;
    }

    public void remove(int pos){
        pos--;
        if(pos < 0 || pos >= size){
            System.out.println("Invalid position");
            return;
        }
        for(int i=pos; i<size-1; i++){
            array[i] = array[i+1];
            count[i]= count[i+1];
        }
        size--;
    }

    public boolean search(final Object element){
        boolean searchResult = false;
        int pos= -1;
        for(int i=0; i<size; i++){
            if(array[i] == element){
                searchResult = true;
                pos = i;
                break;
            }
        }
        if(searchResult){
            count[pos]++;
            final int c = count[pos];
            for(int i=0; i<pos; i++){
                if(count[pos] > count[i]){
                    for(int j=pos; j>i; j--){
                        array[j] = array[j-1];
                        count[j] = count[j-1];
                    }
                    array[i] = element;
                    count[i] = c;
                    break;
                }
            }
        }
        return searchResult;
    }
}