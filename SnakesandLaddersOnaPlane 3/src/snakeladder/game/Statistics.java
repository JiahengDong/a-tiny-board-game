package snakeladder.game;

import java.util.HashMap;

public class Statistics {
    private HashMap<Integer, Integer> rollingResults = new HashMap<Integer, Integer>();
    private HashMap<String, Integer> traversals = new HashMap<String, Integer>();
    private String playerIndex;
    private int numberOfDice = 0;
    Statistics(int numberOfDice, String playerIndex){
        this.numberOfDice = numberOfDice;
        this.playerIndex = playerIndex;
        for(int i = numberOfDice; i<=numberOfDice*6; i++){
            rollingResults.put(i, 0);
        }
        traversals.put("up", 0);
        traversals.put("down", 0);

    }
    public void recordRollingResults(int key){
        rollingResults.put(key, rollingResults.get(key) + 1);

    }

    public void updateTraversals(String upOrDown){

        traversals.put(upOrDown, traversals.get(upOrDown) + 1);

    }

    public void printRollingResults(){
        String output = playerIndex + " rolled: ";
        for(Integer key: rollingResults.keySet()){
            if(key == numberOfDice){
                output += (key+"-"+rollingResults.get(key));
            }else{
                output += (", "+key+"-"+rollingResults.get(key));
            }

        }
        System.out.println(output);
    }

    public void printTraversals(){
        String output = playerIndex + " traversed: "+ "up-"+ traversals.get("up") +"," + "down-" + traversals.get("down");
        System.out.println(output);
    }
}
