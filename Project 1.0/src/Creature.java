abstract class Creature {
    public abstract String Move(int ND, int ED, int SD, int WD);  
    public abstract String Breed(boolean N, boolean E, boolean S, boolean W);
}
//Both functions are abstract and are used in both Ant and Beetle class