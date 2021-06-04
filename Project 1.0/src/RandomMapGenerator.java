import java.util.Random;

public class RandomMapGenerator {
    private final char ant;
    private final char beetle;
    private final char[][] grid = new char[10][10];
    public RandomMapGenerator(char ant, char beetle) {
        this.ant = ant;
        this.beetle = beetle;
    }
    public char[][] generateGrid(){
        Random rn = new Random();
        int seed;
        int min = 1;
        int max = 10;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                seed = rn.nextInt((max - min) + 1) + min;
                switch(seed){
                    case 1: // write ant 
                    case 2:
                    case 3:
                    case 4:
                        grid[i][j] = ant;
                        break;
                    case 5:
                    case 6:
                    case 7:// write space
                    case 8:
                    case 9:
                        grid[i][j] = ' ';
                        break;
                    case 10: // write beetle
                        grid[i][j] = beetle;
                        break;
                    default:
                        break;
                }
            }
        }
        return grid;
    }
}
