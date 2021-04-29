public class test {
    public static void main(String[] args) {
        GenericHashMap<String, String> testTable = new GenericHashMap<>();
        testTable.put("Varun","Bob123");
        testTable.put("Meera","Bob123");
        testTable.put("Yo", "yo&892");
        testTable.put("Larry","Yakki1231");
        testTable.printValues();


    }
}
