public class Ant extends Creature{
   
    private int turnsSurvived;
    private boolean hadMoved;
    
    //accessor functions
    public boolean isHadMoved() {
        return hadMoved;
    }

    public void setHadMoved(boolean hadMoved) {
        this.hadMoved = hadMoved;
    }

    public int getTurnsSurvived() {
        return turnsSurvived;
    }
    public void incrementTurnsSurvived() {
        this.turnsSurvived++;
    }
    public void resetTurnsSurvived(){
        this.turnsSurvived = 0;
    }
    public Ant() {
        this.turnsSurvived = 0;
        this.hadMoved = false;
    }
    @Override
    public String Move(int ND, int ED, int SD, int WD) {  //Take in the directional values of each ant
        this.incrementTurnsSurvived();
        this.setHadMoved(true);
        
        if(ND== 0 && ED == 0 && SD == 0 && WD == 0){return "NM";} //if no beetle was found, No Move
        if(ND != 0 && ED != 0 && SD != 0 && WD != 0){ //if beetles are found in all directions
            int[] availableDirections = {ND, ED, SD, WD}; //store directions in array and find the farthest beetle
            int bestDirection = availableDirections[0];
            int bestIndex = 0;
            for(int i = 0; i < availableDirections.length; i++){
                if(availableDirections[i] > bestDirection){  //if the available direction at index i is farther than the set best direction
                    bestDirection = availableDirections[i]; //save this as the best direction
                    bestIndex = i; //save index value
                }
            }
            switch(bestIndex){ //use best index position to return value
                case 0: return "North";
                case 1: return "East";
                case 2: return "South";
                case 3: return "West";
                default: return "NM";
            }
        }
        int[] distances = {ND, ED, SD, WD};
        int numberOfZeros = 0;
        for (int distance : distances) {
            if ((distance == 0)) {
                numberOfZeros++;
            }
        }
        if(numberOfZeros == 3) { //if there is only 1 beetle detected
            int closestBeetle = 11; //set arbitrary value that will be reset on first loop
            int closestBeetleIndex = 0;
            for (int i = 0; i < distances.length; i++) {
                if ((distances[i] != 0) && (distances[i] < closestBeetle)) {  //ignore the 0s and just see the other directional values, if directional value of the beetle is less then the set value,
                    closestBeetle = distances[i]; //save the closest beetle distance
                    closestBeetleIndex = i; //save the index of the closest beetle
                }
            }
            switch (closestBeetleIndex) { //use index and move in the opposite direction away from the beetle
                case 0: return "South"; //Move opposite direction of North
                case 1: return "West"; //Move opposite direction of East
                case 2: return "North"; //Move opposite direction of South
                case 3: return "East"; //Move opposite direction of West
                default: return "NM";
            }
        }else if(numberOfZeros == 2) { //This else will handle cases where 2 beetles are detected
            
            
            //special cases which handles beetles in both horizontal and vertical directions. These beetles have tied distances
            if(ND != 0 && SD != 0 && ED == 0 && WD == 0){
                if(ND == SD) {
                    return "East";
                }
            }
            if(ND == 0 && SD == 0 && ED != 0 && WD != 0){
                if(ED == WD){
                    return "North";
                }
            }
            
            

            //We need to create a array with the opposite direction values in the corresponding index of the distance array. We do this because we have to Prioritize NESW after 2 beetles are found
            //this array order will help up obtain the correct movement when we find the movement based on the order of precedence
            int[] specialDistances = {SD, WD, ND, ED}; 
            int[] tiePos = new int[4]; //tie positions will contain the values of directions that have the same distances
            int closestBeetle = 11;
            //first find the smallest beetle distance  //(4,0,4,0) NESW -> (4,0,4,0);
            for (int distance : specialDistances) {
                if (distance != 0 && (distance < closestBeetle)) {
                    closestBeetle = distance;
                }
            }
            //uses the closestBeetle to see if their any other beetles that share the same distance to the ant
            for (int i = 0; i < specialDistances.length; i++) {
                if (specialDistances[i] == closestBeetle) {
                    tiePos[i] = closestBeetle; //save index into tie position   
                } else {
                    tiePos[i] = -1;// tie was not found
                }
            }
            //iterates through the tie position to find the first occurrence of -1, this means that a beetle was not in that direction and you could go there. 
            int bestDirection = 0;
            for (int i = 0; i < tiePos.length; i++) {
                if (tiePos[i] != -1 && tiePos[i] == closestBeetle) {
                    bestDirection = i;
                    break;
                }
            }
            switch (bestDirection) {
                case 0: return "North";
                case 1: return "East";
                case 2: return "South";
                case 3: return "West";
                default: return "NM";
            }
        }else if(numberOfZeros == 1){ //this is when 3 beetles are found
            int bestMove = 0; //the best move would be to move in the direction with no beetles
            for(int i = 0; i < distances.length; i++){
                if(distances[i] == 0){ //so we find the index position of found 0 and move in that direction
                    bestMove = i;
                }
            }
            switch(bestMove){
                case 0: return "North";
                case 1: return "East";
                case 2: return "South";
                case 3: return "West";
                default: return "NM";
            }
        }
        return "NM";
    }
    /*
    * Breed function is used to check if you can breed in a particular direction
    * Information is for function is gathered in main, NESW preference
    * Reset the turns survived so that you are able to check if you are able to breed every three turns
    * */
    @Override
    public String Breed(boolean N, boolean E, boolean S, boolean W) { 
        if(getTurnsSurvived() >= 3){
            this.resetTurnsSurvived();
            if(N) {
                return "North";
            }
            if(E) {
                return "East";
            }
            if(S) {
                return "South";
            }
            if(W) {
                return "West";
            }
        }
        return "NB"; //you have no available moves since all directions are occupied (NESW = false)
    }
}
