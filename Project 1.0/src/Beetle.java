public class Beetle extends Creature{
    
    private int turnsSurvived;
    private boolean hadMoved;
    private int breedCounter;
    
    //accessor functions
    public boolean isHadMoved() {
        return hadMoved;
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
    
    public void setHadMoved(boolean hadMoved) {
        this.hadMoved = hadMoved;
    }
    public void incrementerBreedCounter(){
        this.breedCounter++;
    }
    public void resetBreedCounter(){
        this.breedCounter = 0;
    }
    public int getBreedCounter(){
        return this.breedCounter;
    }

    public Beetle() {
        this.turnsSurvived = 0;
        this.breedCounter = 0;
        this.hadMoved = false;
    }
    //Beetle Move function is divided into 3 parts. One for edge cases, regular movements, and MTOA case
    
    
    //findFarthestEdgeMove is used to fond the farthest edge relative to the beetle
    public String findFarthestEdgeMove(int ND, int ED, int SD, int WD, int northEdge, int eastEdge, int southEdge, int westEdge){

        if(ND== 0 && ED == 0 && SD == 0 && WD == 0){ //checks if all of beetle found directions are 0, else exit function
            int[] arr = {northEdge,eastEdge,southEdge,westEdge}; //store edge cases into array
            int farthestEdge = 0; //arr[0]
            int farthestEdgeIndex = 0;
            //find largest number in array, this corresponds to the largest edge case resulting in the desired direction for the beetle
            for(int i = 0; i < arr.length; i++){
                if(arr[i] > farthestEdge){ 
                    farthestEdge = arr[i];
                    farthestEdgeIndex = i; //save index value of the farthestEdgeIndex 
                }
            }
            //case through farthestEdgeIndex
            //increment the breed counter as well as the turns survived. This info will be used for their respective functions in the class. We don't do this the same way as in the ant class since we have
            //3 possible cases for the beetle to move
            switch(farthestEdgeIndex){
                case 0:
                    this.incrementerBreedCounter(); 
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "North"; 
                case 1:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "East";
                case 2:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "South";
                case 3:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "West";
                default: return "Error";   //theoretically will never enter, only for safety
            }
        }
        return "NEC"; //Not Edge Case, fails condition in if statement()
    }
    

    // This is a regular move function, only is used when failed edge case.
    @Override
    public String Move(int ND, int ED, int SD, int WD) {

        //find the number of zeros
        int[] distances = {ND, ED, SD, WD}; 
        int numberOfZeros = 0;
        for (int distance : distances) {
            if ((distance == 0)) {
                numberOfZeros++;
            }
        }
        //if there is only 1 ant detected, you can use this function, else exit
        if(numberOfZeros == 3){
            int closestAnt = 11;
            int closestAntIndex = 0;
            //move towards the direction of that found ant
            for(int i = 0; i < distances.length; i++){
                if((distances[i] != 0) && (distances[i] < closestAnt)){ //desired ant is the closest ant to the beetle
                    closestAnt = distances[i];
                    closestAntIndex = i; //save index position
                }
            }
            //case the index position
            //same idea in the the previous move function written above (farthestEdgeCase())
            switch(closestAntIndex){
                case 0:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "North"; 
                case 1:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "East"; 
                case 2:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "South"; 
                case 3:
                    this.incrementerBreedCounter();
                    this.incrementTurnsSurvived();
                    hadMoved = true;
                    return "West"; 
                default: return "Error";
            }
        }
        return "MTOA"; //More than one ant was found
    }
    public String moreThanOneAnt(int ND, int ED, int SD, int WD, int northSwarm, int eastSwarm, int southSwarm, int westSwarm){
        //This is the only possible move function left so we can just increment here, 
        this.incrementTurnsSurvived();
        this.incrementerBreedCounter();
        setHadMoved(true);
        
        int[] distances = {ND, ED, SD, WD}; //store into array to be traversed
        int[] tiePos = new int[4]; //tie positions will contain the values of directions that have the same distances
        int closestAnt = 11; 
        //first find the smallest ant distance
        for (int distance : distances) {
            if (distance != 0 && (distance < closestAnt)) {
                closestAnt = distance;
            }
        } 
        //uses the closestAnt to see if their any other ants that share the same distance to the beetle
        for(int i = 0 ; i < distances.length; i++){
            if(distances[i] == closestAnt){
                tiePos[i] = closestAnt; //save index into tie position   
            }else{
                tiePos[i] = -1;// tie was not found
            }
        }
        int [] swarms = {northSwarm,eastSwarm, southSwarm, westSwarm};
        
        int maxSwarm = -1;
        int bestDirection = -1;
        for(int i = 0; i < tiePos.length; i++){
            if(tiePos[i] != -1 && swarms[i] > maxSwarm){ //check the tie distances to see who has the most neighbors around them. If the tie position does not have a -1 and the swarm value is bigger thant the set value
                maxSwarm = swarms[i]; //save the max swarm value
                bestDirection = i; //save the index of the bestSwarmValue
            }
        }
        //case the bestDirection and move in that direction of the corresponding index
        switch(bestDirection){
            case 0: return "North";
            case 1: return "East";
            case 2: return "South";
            case 3: return "West";
            default: return "Error";
        }
    }
    //Same breed concept and algorithm as the ant. Since we reset the turns survived in the starve function, we use the breedCounter variable to see if you can breed after the specified turns (8 turns)
    @Override
    public String Breed(boolean N, boolean E, boolean S, boolean W) {
        if(getBreedCounter() >= 8){ //doing >= just in case something goes wrong
            this.resetBreedCounter();
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
        return "NB"; //No Breed
    }
    //the starve function is a boolean that is used to see if a beetle object should be deleted in main. The getTurnsSurvived counter is reset everytime an ant is eaten, reset occurs in main. 
    public boolean Starve(){
        return getTurnsSurvived() >= 5; 
    }
}
