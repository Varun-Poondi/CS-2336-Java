public class StringDebugger {
    public static void main(String[] args){
        debugString("Luigi ", 93,31,31,31,31,31);
        debugString("Daisy",14	,5	,1,	2,	0,	0);
        debugString("Mario", 11, 3, 3,2,	1,0);
    }
    public static void debugString(String name, int atBats, int hits, int walks, int strikeouts, int hitByPitch, int sacrifices){
        String result = "";
        int i;
        result += name;
        for(i = 0; i < hits; i++){
            result += "H";
        }
        for(i = 0; i < strikeouts; i++){
            result += "K";
        }
        for(i = 0; i < walks; i++){
            result += "W";
        }
        for(i = 0; i < hitByPitch; i++){
            result += "P";
        }
        for(i = 0; i < sacrifices; i++){
            result += "S";
        }
        int numberOfOuts = atBats - strikeouts - hits;
        
        for(i = 0; i < numberOfOuts; i++){
            result += "O";
        }
        System.out.println(result);
    }
}