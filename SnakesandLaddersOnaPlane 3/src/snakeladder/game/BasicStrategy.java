package snakeladder.game;

public class BasicStrategy implements SwitchStrategy {
    @Override
    public boolean applyStrategy(NavigationPane np, GamePane gp) {
        //number of dice, nextPlayer, nextPlayer in the next round will display in which place
        //get the number of up connections and down connections
        //assume two auto players
        //lowest possible value -- do not down
        int numOfDice = np.getNumberOfDice();
        Puppet nextPlayer = gp.getNextPuppet();
        int minNum = numOfDice*1;
        int maxNum = numOfDice*6;

        int nextMinCellIndex = nextPlayer.getCellIndex() + minNum;
        int nextMaxCellindex = nextPlayer.getCellIndex() + maxNum;
        //total random

        int up = 0;
        int down = 0;
        while(nextMinCellIndex <= nextMaxCellindex){
            //check if meet connections
            Connection nextCon = gp.getConnectionAt(GamePane.cellToLocation(nextMinCellIndex));
            if(nextCon != null){
                if( (nextCon.reversed && nextCon instanceof Snake) || (!nextCon.reversed && nextCon instanceof Ladder)){
                    up++;
                }else if((!nextCon.reversed && nextCon instanceof Snake) || (nextCon.reversed && nextCon instanceof Ladder)){
                    //player do not travel down when get the lowest possible value
                    //when it exists at the lowest possible value cell index
                    if(nextMinCellIndex != nextPlayer.getCellIndex() + minNum){
                        down++;
                    }
                }
            }
            nextMinCellIndex++;

        }

        if(up > down){
            return true;
        }

        return false;
    }
}
