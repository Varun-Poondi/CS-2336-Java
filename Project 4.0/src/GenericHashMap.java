import java.util.ArrayList;
import java.util.Collections;

/* Name: Varun Poondi
 * Net ID: VMP190003
 * Prof: Jason Smith
 * Date: 4/27/2021
 * */

public class GenericHashMap<K, V>{
    
    private int currentPrime = 11; //the prime number will eventually increase if we need to rehash

    //Arraylists needed for the hashtable
    public ArrayList<K> keyTable = new ArrayList<>(currentPrime);
    public ArrayList<V> valueTable = new ArrayList<>(currentPrime);
    
    public GenericHashMap() { //constructor
        initArr(keyTable,valueTable,currentPrime);
    }
    
    public void initArr(ArrayList<K> k, ArrayList<V> v, int size){ //initializes the arrayList as null. So that we can have an array with the initialized size
        for (int i = 0; i < size; i++) {
            k.add(null);
        }
        for (int i = 0; i < size; i++) {
            v.add(null);
        }
    }
    
    public void setCurrentPrime(int currentPrime) {
        this.currentPrime = currentPrime;
    }
    
    //key 1 and key 2 are generated with these getters and are used in the put, rehash, and get function
    private int getKey1(K key){
        return Math.abs(key.hashCode() % currentPrime);
    }
    private int getKey2(K key){
        return Math.abs(7 - key.hashCode() % 7);
    }
    
    public void put(K key, V value){
        double loadFactor = loadFactor(valueTable); //get the load factor
        if(loadFactor >= 0.5){ 
            rehash(); //you have to rehash if the load factor is greater than or equal to is 0.5
        }
        //generate the key values
        int key1 = getKey1(key);
        int key2 = getKey2(key);

        int i = 0;
        while(keyTable.get((key1 + i * key2) % currentPrime) != null){ //find a spot to insert
            i++; //increment if the current index is occupied
        }
        int index = (key1 + i * key2) % currentPrime; //debug purpose
        keyTable.set(index, key); //replace the null value with the new key
        valueTable.set(index,value); //replace the null value with the new value
    }
    private void rehash(){
        int newSize = nextPrime(keyTable.size() * 2); //get the new size that is a prime number
        //create new arrayLists so that they can be updated
        ArrayList<V> newValueTable = new ArrayList<>(newSize);
        ArrayList<K> newKeyTable = new ArrayList<>(newSize);
        initArr(newKeyTable,newValueTable,newSize); //initialize both arrayLists with the newSize
        int key1;
        int key2;
        for(int i = 0; i < keyTable.size(); i++){ //traverse through the keyTable arrayList
            K key = keyTable.get(i);
            V value = valueTable.get(i);
            if(key != null){ //if the key is not null
                //generate the keys
                key1 = getKey1(key);
                key2 = getKey2(key);
                
                int k = 0;
                while(newKeyTable.get((key1 + k * key2) % currentPrime) != null){ //find a spot to insert into the new arrayLists
                    k++;
                }
                int index = (key1 + k * key2) % currentPrime; //debug purpose
                newKeyTable.set(index, key); //update the newKeyTable
                newValueTable.set(index,value); //update the newValueTable
            }
        }
        // update the key and value tables
        keyTable = newKeyTable; 
        valueTable = newValueTable;
    }
    
    public V get(K key){
        //get the key values
        int key1 = getKey1(key);
        int key2 = getKey2(key);
        //you have constant time complexity since we are just decoding the key and finding it's respected value
        int i = 0;
        while(true){
            int index = (key1 + i * key2) % currentPrime;  //get the index
            K tempKey = keyTable.get(index); //get the key from the arrayList
            if(tempKey == null){ //if the key is null, return null
                return null;
            }else if(tempKey.equals(key)){ //else return the value from the valueTable
                return valueTable.get(index);
            }
            i++; //increment the counter
        }
    }
    private int nextPrime(int length){
        int counter = length;
        boolean primeFound = false;
        while (!primeFound) { //traverse until you find a number that isPrime. The isPrime function will handle.
            counter++;
            if (isPrime(counter)) {
                primeFound = true; //set boolean to true to exit loop
            }
        }
        setCurrentPrime(counter);
        return counter;
    }
    //we start at 2 since we know 1 and 2 are prime numbers, 
    //we traverse all the way to the sqrt(number) since by that point, we should be able to determine if the number is prime or not.
    //we add plus 1 since we need to account for cases such as 4 which would be become 2 when sqrt()'ed. 
    private boolean isPrime(int number){ 
        for(int i = 2; i < Math.sqrt(number) + 1; i++){
            if(number % i == 0){
                return false;
            }
        }
        return true;
    }
    private double loadFactor(ArrayList<V> table){ //goes through the arrayList and counts the number of buckets that aren't empty and divides the number by the size of the arrayList
        double factor = 0;
        for (V v : table) {
            if (v != null) {
                factor++;
            }
        }
        factor /= table.size();
        return factor;
    }
    public void printValues(){ //goes through the values table and prints out all the values
        for (V v : valueTable) {
            System.out.println(v);
        }
    }
}
