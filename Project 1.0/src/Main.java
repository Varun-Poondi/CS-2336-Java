/*
* NAME: Varun Poondi
* NET ID: VMP190003
* PROF: Jason Smith
* Class: CS 2336.001 
*/


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        
        char [][] convert = new char[10][10]; //gird of char
        Creature [][] creatures = new Creature[10][10]; //grid of Creature Objects
        
        Scanner sc1 = new Scanner(System.in); //sc1 reads in userInput
       
        int numberOfTurns;
        String fileName;
        char antChar;
        char beetleChar;
        
       //use scanner to retrieve userInput and store into variables
        System.out.print("Enter File Name: ");
        fileName = sc1.next();
        System.out.print("Enter Ant Character: ");
        antChar = sc1.next().charAt(0);
        System.out.print("Enter Beetle Character: ");
        beetleChar = sc1.next().charAt(0);
        System.out.print("Enter Number Of Turns: ");
        numberOfTurns = sc1.nextInt();


        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName))); //sc used to read the fileName
        readFile(sc, convert); //pass in Scanner sc and regular char grid
        charToCreatureGird(creatures, convert); //convert char grid to Creature grid
        
        int counter = 0; //turn counter
        while(counter < numberOfTurns){ 
            counter++;
            System.out.println("TURN " + (counter));
            
            moveBeetles(creatures);
            moveAnts(creatures);
            
            starveBeetle(creatures);
            if(counter%3==0) {
                antBreed(creatures);
            }
            if(counter%8==0) {
                beetleBreed(creatures);
            }
            creatureToCharGrid(creatures, convert, antChar, beetleChar); 
            printCharGrid(convert);
            System.out.println();
            
        }
        //Close all scanners after info after grids have been printed
        sc.close();
        sc1.close();
        System.out.println("Game is Complete!");
    }
    
    public static void readFile(Scanner sc, char[][] convert){
        while(sc.hasNextLine()) { //while the file has a nextLine
            for (int i=0; i<convert.length; i++) { 
                String line = sc.nextLine(); //read all lines including possibly empty lines
                for (int j=0; j<convert[i].length; j++) {
                    convert[i][j] = line.charAt(j); //save char at index[i][j] to convert[i][j]. 
                }
            }
        }
    }
    public static void printCharGrid(char[][] convert){ 
        for (char[] chars : convert) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }
    /*
    * Used to convert between the char grid and Creature grid. 
    * If there is an 'a' located in char grid at index[i][j], create a Ant object in Creature grid at index[i][j]
    * If there is an 'B' located in char grid at index[i][j], create a Beetle object in Creature grid at index[i][j]
    * If there is an ' ' located in char grid at index[i][j], set null in Creature grid at index[i][j]
    * indexes will be aligned since both Creature and char grids are both parallel 2D arrays
    * */
    public static void charToCreatureGird(Creature[][] creatures, char[][] convert){
        for(int i = 0; i < convert.length; i++){
            for(int j = 0; j < convert[i].length; j++){
                if(convert[i][j] == 'a'){ 
                    creatures[i][j] = new Ant(); 
                }else if (convert[i][j] == 'B'){
                    creatures[i][j] = new Beetle();
                }else if (convert[i][j] == ' '){
                    creatures[i][j] = null;
                }
            }
        }
    }
     /*
     * Used to convert between the Creature grid and char grid.
     * If there is an Ant object located in Creature grid at index[i][j], set the user's specified ant char in the char grid at index[i][j]
     * If there is an Beetle object located in Creature grid at index[i][j], set the user's specified beetle char in the char grid at index[i][j]
     * If it is null in the Creature grid at index[i][j], set ' ' in the char grid at index[i][j]
     * */
    public static void creatureToCharGrid(Creature[][] creatures, char[][] convert, char antChar, char beetleChar){
        for(int i = 0; i < creatures.length; i++){
            for(int j = 0; j < creatures[i].length; j++){
                if(creatures[i][j] instanceof Ant){
                    convert[i][j] = antChar;
                }else if (creatures[i][j] instanceof  Beetle){
                    convert[i][j] = beetleChar;
                }else if (creatures[i][j] == null){
                    convert[i][j] = ' ';
                }
            }
        }
    }
    /*
    * Used to check the number of ant neighbors around a ant detected by a Beetle. 
    * Use to in the case where a more than 1 ants distance is tied with other ants relative to the Beetle's position
    * */
    public static int antNeighbors(Creature[][] grid, int x, int y){ //Pass in the creatures grid, x = columns, y = rows
        int neighbors = 0;
        //---------------------------------------------------------------
        if(x-1 >= 0 && y-1 >= 0 && grid[x-1][y-1] instanceof Ant){//check NW position
            neighbors++;
        }
        if(x-1 >= 0 && grid[x-1][y] instanceof Ant){ //check N position
            neighbors++;
        }
        if(x-1 >= 0 && y+1 <= 9 && grid[x-1][y+1] instanceof Ant){ //check NE position
            neighbors++;
        }
        //---------------------------------------------------------------
        if(y-1 >= 0 && grid[x][y-1] instanceof Ant){ //check W position
            neighbors++;
        }
        if(y+1 <= 9 && grid[x][y+1] instanceof Ant){ //check E position
            neighbors++;
        }
        //---------------------------------------------------------------
        if(x+1 <= 9 && y-1 >= 0 && grid[x+1][y-1] instanceof Ant){//check SW position
            neighbors++;
        }
        if(x+1 <= 9 && grid[x+1][y] instanceof Ant){ //check S position
            neighbors++;
        }
        if(x+1 <= 9 && y+1 <= 9 && grid[x+1][y+1] instanceof Ant){ //check SE position
            neighbors++;
        }
        //---------------------------------------------------------------
        return neighbors; //return the neighbors around the ant
        
    }
    public static void moveAnts(Creature[][] creatures){
        for(int i = 0; i < creatures.length; i++){ //col
            for(int j = 0; j < creatures[i].length; j++){ //row
                if(creatures[j][i] instanceof Ant) {
                    if (!((Ant) creatures[j][i]).isHadMoved()) { //if the ant at [j][i] has not been moved for that turn, move the ant
                        //The distance to where beetle is found relative to the ant at [j][i]
                        //if the variables remain at 0, that means that a beetle was not found in that direction
                        int northDirection = 0;
                        int eastDirection = 0;
                        int southDirection = 0;
                        int westDirection = 0;
                        

                        for (int k = j; k >= 0; k--) { //Check North Direction
                            if (creatures[k][i] instanceof Beetle) {
                                northDirection = j-k; //find the relative direction by subtracting the the row where the ant [j] currently is to the where the beetle was found in the North direction
                                break;
                            }
                        }
                        for (int k = i; k <= 9; k++) { //Check East Direction
                            if (creatures[j][k] instanceof Beetle) {
                                eastDirection = k-i; //find the relative direction by subtracting the the column where the ant [i] currently is to the where the beetle was found in the East direction
                                break;
                            }
                        }
                        for (int k = j; k <= 9; k++) { //Check South Direction
                            if (creatures[k][i] instanceof Beetle) {
                                southDirection = k-j; //find the relative direction by subtracting the the column where the ant [i] currently is to the where the beetle was found in the South direction
                                break;
                            }
                        }
                        for (int k = i; k >= 0; k--) { //Check West Direction
                            if (creatures[j][k] instanceof Beetle) {
                                westDirection = i-k; //find the relative direction by subtracting the the row where the ant currently [j] is to the where the beetle was found in the West direction
                                break;
                            }
                        }
                        
                        String move = creatures[j][i].Move(northDirection, eastDirection, southDirection, westDirection);
                        switch(move){
                            case "North":
                                if (j > 0 && creatures[j - 1][i] == null) {
                                    creatures[j - 1][i] = creatures[j][i];
                                    creatures[j][i] = null;
                                    break;
                                }
                                break;
                            case "East":
                                if (i < 9 && creatures[j][i + 1] == null) {
                                    creatures[j][i + 1] = creatures[j][i];
                                    creatures[j][i] = null;
                                    break;
                                }
                                break;
                            case "South":
                                if (j < 9 && creatures[j + 1][i] == null) {
                                    creatures[j + 1][i] = creatures[j][i];
                                    creatures[j][i] = null;
                                    break;
                                }
                                break;
                            case "West":
                                if (i > 0 && creatures[j][i - 1] == null) {
                                    creatures[j][i - 1] = creatures[j][i];
                                    creatures[j][i] = null;
                                    break;
                                }
                                break;
                            default:
                                break;
                        }
                        
                    }
                    
                }
                
            }
        } 
        //After checking and moving all ant positions, set hasMoved to false so that you are able to move them again next turn
        for(int i = 0; i < creatures.length; i++){
            for(int j = 0; j < creatures[0].length; j++){
                if(creatures[j][i] instanceof Ant){
                    ((Ant) creatures[j][i]).setHadMoved(false);
                }
            }
        }
    }
    public static void moveBeetles(Creature[][] creatures){
        for(int i = 0; i < creatures.length; i++){ //cols
            for(int j = 0; j < creatures[i].length; j++){ //rows 
                if(creatures[j][i] instanceof Beetle){
                    if(!((Beetle) creatures[j][i]).isHadMoved()){ //if the Beetle has not been moved yet, them check for movement

                        //The distance to where ant is found relative to the beetle at [j][i]
                        //if the variables remain at 0, that means that an ant was not found in that direction 
                        int antFoundNorthDirection = 0;
                        int antFoundEastDirection = 0;
                        int antFoundSouthDirection = 0;
                        int antFoundWestDirection = 0;
                        
                        //Used to increment to find the edges around a beetle, only used when there is not ant found in any direction (EDGE case) 
                        int northEdge = 0;
                        int eastEdge = 0;
                        int southEdge = 0;
                        int westEdge = 0;
                        
                        //Used to increment to get the number of neighbors around an ant found (MTOA case) 
                        int antsAroundNorthAnt = 0;
                        int antsAroundEastAnt = 0;
                        int antsAroundSouthAnt = 0;
                        int antsAroundWestAnt = 0;
                        
                        
                        //if an ant was found in that direction, find the number of Neighbors around that specific ant
                        for (int k = j; k >= 0; k--) { //Check North Direction
                            if (creatures[k][i] instanceof Ant) { //y,x
                                antFoundNorthDirection = j - k;
                                antsAroundNorthAnt = antNeighbors(creatures, k, i);
                                break;
                            }
                            northEdge++;
                        }
                        for (int k = i; k <= 9; k++) { //Check East Direction
                            if (creatures[j][k] instanceof Ant) { //x,y
                                antFoundEastDirection = k - i ;
                                antsAroundEastAnt = antNeighbors(creatures,j,k);
                                break;
                            }
                            eastEdge++;
                        }
                        for (int k = j; k <= 9; k++) { //Check South Direction
                            if (creatures[k][i] instanceof Ant) {
                                antFoundSouthDirection = k - j ;
                                antsAroundSouthAnt = antNeighbors(creatures,k,i);
                                break;
                            }
                            southEdge++;
                        }
                        for (int k = i; k >= 0; k--) { //Check West Direction
                            if (creatures[j][k] instanceof Ant) {
                                antFoundWestDirection = i - k;
                                antsAroundWestAnt = antNeighbors(creatures, j, k);
                                break;
                            }
                            westEdge++;
                        }
                        
                        //Pass in relative directions along with edge values to find if its an edge case
                        assert creatures[j][i] instanceof Beetle;
                        String edgeCase = ((Beetle) creatures[j][i]).findFarthestEdgeMove(antFoundNorthDirection, antFoundEastDirection, antFoundSouthDirection, antFoundWestDirection, northEdge,eastEdge,southEdge,westEdge);
                       
                        //case the edge case
                        //Bounds check, and move as long as the next position is null or contains an Ant object. Garbage collections will do the rest
                        switch(edgeCase){
                            case "North":
                                if (j > 0 && (creatures[j - 1][i] == null || creatures[j - 1][i] instanceof Ant)) {
                                    creatures[j - 1][i] = creatures[j][i];
                                    creatures[j][i] = null;
                                }
                                break;
                            case "East":
                                if(i < 9 && (creatures[j][i + 1] == null || creatures[j][i+1] instanceof Ant)){
                                    creatures[j][i + 1] = creatures[j][i];
                                    creatures[j][i] = null;
                                }
                                break;
                            case "South":
                                if(j < 9 && (creatures[j + 1][i] == null || creatures[j+1][i] instanceof Ant)){
                                    creatures[j+1][i] = creatures[j][i];
                                    creatures[j][i] = null;
                                }
                                break;
                            case "West":
                                if(i > 0 && (creatures[j][i - 1] == null || creatures[j][i-1] instanceof Ant)){
                                    creatures[j][i - 1] = creatures[j][i];
                                    creatures[j][i] = null;
                                }
                                break;
                            case "NEC": //Not Edge Case
                                
                                //NEC occurs when any of the the directions does not equal 0, meaning a beetle was found
                                String regMove = creatures[j][i].Move(antFoundNorthDirection, antFoundEastDirection, antFoundSouthDirection, antFoundWestDirection);
                                
                                //Same logic to as the ant to move to the next position (Refer to comments in antMove)
                                switch(regMove){ 
                                    case "North":
                                        if (j > 0 && (creatures[j - 1][i] == null || creatures[j - 1][i] instanceof Ant)) {
                                            if(creatures[j - 1][i] instanceof Ant){  //If an ant was found at the position to move,
                                                ((Beetle) creatures[j][i]).resetTurnsSurvived(); //reset the turns survived, meaning you have eaten before 5 turns (check beetle class for logic)
                                            }
                                            creatures[j - 1][i] = creatures[j][i];
                                            creatures[j][i] = null;
                                        }
                                        break;
                                    case "East":
                                        if(i < 9 && (creatures[j][i + 1] == null || creatures[j][i+1] instanceof Ant)){
                                            if(creatures[j][i + 1] instanceof Ant){
                                                ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                            }
                                            creatures[j][i + 1] = creatures[j][i];
                                            creatures[j][i] = null;
                                        }
                                        break;
                                    case "South":
                                        if(j < 9 && (creatures[j + 1][i] == null || creatures[j+1][i] instanceof Ant)){
                                            if(creatures[j + 1][i] instanceof Ant){
                                                ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                            }
                                            creatures[j+1][i] = creatures[j][i];
                                            creatures[j][i] = null;
                                        }
                                        break;
                                    case "West":
                                        if(i > 0 && (creatures[j][i - 1] == null || creatures[j][i-1] instanceof Ant)){
                                            if(creatures[j][i-1] instanceof Ant){
                                                ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                            }
                                            creatures[j][i - 1] = creatures[j][i];
                                            creatures[j][i] = null;
                                        }
                                        break;
                                    case "MTOA": //More than One Ant Was Found
                                        String MOTA = ((Beetle) creatures[j][i]).moreThanOneAnt(antFoundNorthDirection, antFoundEastDirection, antFoundSouthDirection, antFoundWestDirection,antsAroundNorthAnt,antsAroundEastAnt,antsAroundSouthAnt, antsAroundWestAnt);
                                        //MTOA is cased when you have more than 1 ant detected in orthogonal directions. You pass in the ant neighbors of each direction of possible ants found along with the ants found in those directions
                                        switch(MOTA) {
                                            //Same logic as the previous switch
                                            case "North":
                                                if (j > 0 && (creatures[j - 1][i] == null || creatures[j - 1][i] instanceof Ant)) {
                                                    if(creatures[j - 1][i] instanceof Ant){
                                                        ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                                    }
                                                    creatures[j - 1][i] = creatures[j][i];
                                                    creatures[j][i] = null;
                                                }
                                                break;
                                            case "East":
                                                if (i < 9 && (creatures[j][i + 1] == null || creatures[j][i+1] instanceof Ant)) {
                                                    if(creatures[j][i + 1] instanceof Ant){
                                                        ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                                    }
                                                    creatures[j][i + 1] = creatures[j][i];
                                                    creatures[j][i] = null;
                                                }
                                                break;
                                            case "South":
                                                if (j < 9 && (creatures[j + 1][i] == null || creatures[j+1][i] instanceof Ant)) {
                                                    if(creatures[j + 1][i] instanceof Ant){
                                                        ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                                    }
                                                    creatures[j + 1][i] = creatures[j][i];
                                                    creatures[j][i] = null;
                                                }
                                                break;
                                            case "West":
                                                if (i > 0 && (creatures[j][i - 1] == null || creatures[j][i-1] instanceof Ant)) {
                                                    if(creatures[j][i-1] instanceof Ant){
                                                        ((Beetle) creatures[j][i]).resetTurnsSurvived();
                                                    }
                                                    creatures[j][i - 1] = creatures[j][i];
                                                    creatures[j][i] = null;
                                                }
                                                break;
                                        }//switch() moreThanOneAnt
                                        break;
                                }//switch() regMove
                                break;
                        }//switch() edgeCase
                    }
                }
            }
        }
        //set all beetles found in the creature array to hasMoved to false. 
        for(int i = 0; i < creatures.length; i++){
            for(int j = 0; j < creatures[0].length; j++){
                if(creatures[j][i] instanceof Beetle){
                    ((Beetle) creatures[j][i]).setHadMoved(false);
                }
            }
        }
    }
    
    //starve function only applies to beetles. Traverse through Creature grid, and check if the beetle has eaten after 5 turns, of not, set the beetle position in the grid to null, deleting the beetle
    public static void starveBeetle(Creature[][] creatures){
        for(int i = 0; i < creatures.length; i++){
            for(int j = 0; j < creatures[0].length; j++){
                if(creatures[j][i] instanceof Beetle){ 
                    if(((Beetle) creatures[j][i]).Starve()){ 
                        creatures[j][i] = null;
                    }
                }
            }
        }
    }
    public static void antBreed(Creature[][] creatures){
        for(int i = 0; i < creatures.length; i++){
            for(int j = 0; j < creatures[0].length; j++){
                if(creatures[j][i] instanceof Ant){
                    
                    //Used to check if there is an empty spot in the orthogonal directions
                    boolean hasMoveNorth = false;
                    boolean hasMoveEast = false;
                    boolean hasMoveSouth = false;
                    boolean hasMoveWest = false;
                    
                    //Logic used from the antMove function to see if there is an null spot in the next desired position
                    if (j > 0 && creatures[j - 1][i] == null) {
                       hasMoveNorth = true;
                    }
                
                    if (i < 9 && creatures[j][i + 1] == null) {
                        hasMoveEast = true;
                    }
                    
                    if (j < 9 && creatures[j + 1][i] == null) {
                        hasMoveSouth = true;
                    }
                    
                    if (i > 0 && creatures[j][i - 1] == null) {
                        hasMoveWest = true;
                    }
                    String move = creatures[j][i].Breed(hasMoveNorth,hasMoveEast,hasMoveSouth,hasMoveWest); //case the movement. Gives a NESW preference to breed 
                    //after finding the desired position to breed, create an ant object in that direction. 
                    switch(move){
                        case "North":
                            creatures[j - 1][i] = new Ant();
                            break;
                        case "East":
                            creatures[j][i + 1] = new Ant();
                            break;
                        case "South":
                            creatures[j+1][i] = new Ant();
                            break;
                        case "West":
                            creatures[j][i - 1] = new Ant();
                            break;
                        case "NM":
                            break;
                    }
                }
            }
        }
    }
    
    //Same logic as the antBreed function written above, only difference is that instead of breeding ants, we are breeding beetles. 
    public static void beetleBreed(Creature[][] creatures){
        for(int i = 0; i < creatures.length; i++){ 
            for(int j = 0; j < creatures[0].length; j++){ 
                if(creatures[j][i] instanceof Beetle){  
                    
                    boolean hasMoveNorth = false;
                    boolean hasMoveEast = false;
                    boolean hasMoveSouth = false;
                    boolean hasMoveWest = false;
                    
                    if (j > 0 && creatures[j - 1][i] == null) {
                        hasMoveNorth = true;
                    }
                    if (i < 9 && creatures[j][i + 1] == null) {
                        hasMoveEast = true;
                    }
                    if (j < 9 && creatures[j + 1][i] == null) {
                        hasMoveSouth = true;
                    }
                    if (i > 0 && creatures[j][i - 1] == null) {
                        hasMoveWest = true;
                    }
                    
                    String move = creatures[j][i].Breed(hasMoveNorth,hasMoveEast,hasMoveSouth,hasMoveWest);
                    switch(move){
                        case "North":
                            creatures[j - 1][i] = new Beetle();
                            break;
                        case "East":
                            creatures[j][i + 1] = new Beetle();
                            break;
                        case "South":
                            creatures[j+1][i] = new Beetle();
                            break;
                        case "West":
                            creatures[j][i - 1] = new Beetle();
                            break;
                        case "NM":
                            break;
                    }
                }
            }
        }
    }
}
