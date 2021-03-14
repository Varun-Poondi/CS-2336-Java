public class StringDebugger {
    public static void main(String[] args) {
        
        //debugString("Varun ",93,31,31,31,31,31);
        debugString("Peach ", 9,2,2,2,2,2);
        debugString("Rosalina ",11,4,2,0,2, 0);
        debugString("Toad ", 12,2,2,4,0,1);
        debugString("Wario ", 13,4,1,3,1,0);
        debugString("Yoshi ",9,2,2,4,2,2);
        
    }
    public static void debugString(String name, int atBats, int hits, int walks, int strikeouts, int hitByPitch, int sacrifices){
        StringBuilder result = new StringBuilder();
        int i;
        result.append(name);
        for(i = 0; i < hits; i++){
            result.append("H");
        }
        for(i = 0; i < strikeouts; i++){
            result.append("K");
        }
        for(i = 0; i < walks; i++){
            result.append("W");
        }
        for(i = 0; i < hitByPitch; i++){
            result.append("P");
        }
        for(i = 0; i < sacrifices; i++){
            result.append("S");
        }
        int numberOfOuts = atBats - strikeouts - hits;
        
        for(i = 0; i < numberOfOuts; i++){
            result.append("O");
        }
        System.out.println(result);
    }
}