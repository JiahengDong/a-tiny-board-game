package snakeladder.game;
import ch.aplu.util.Monitor;
import snakeladder.utility.ServicesRandom;
import ch.aplu.jgamegrid.*;
import java.util.ArrayList;

public class RollDice {
    private NavigationPane navigationPane;
    private final int RANDOM_ROLL_TAG = -1;
    private final Location dieBoardLocation = new Location(100, 180);
    private int dieindex = 0;
    RollDice(NavigationPane np) {
        this.navigationPane = np;
    }
    public void roll(int rollNumber)
    {
        dieindex ++;
        navigationPane.nbchange(rollNumber);
        if (rollNumber == RANDOM_ROLL_TAG) {
            navigationPane.nbchange(ServicesRandom.get().nextInt(6) + 1);
        }
        navigationPane.showStatus("Rolling...");
        navigationPane.showPips("");

        navigationPane.removeActors(Die.class);
        Die die = new Die(navigationPane.getnb(), this);
        navigationPane.addActor(die, dieBoardLocation);
    }
    public void rollalldice(int numberOfDice){
        int total_die_value = 0;
        for(int i= 0;i < numberOfDice;i++ ){
            roll(getDieValue(i,numberOfDice));
            total_die_value += navigationPane.getnb();
            navigationPane.showPips("Pips : " + navigationPane.getnb());
            navigationPane.delay(1000/navigationPane.getNumberOfDice());
            navigationPane.gethandBtn().show(0);
        }
        for(int j= 1;j < numberOfDice;j++ )navigationPane.NbRollschange(navigationPane.getNbRolls() + 1);
        System.out.println("total:"+total_die_value);
        navigationPane.nbchange(total_die_value);
    }
    private int getDieValue(int index,int numberOfDice) {
        if (navigationPane.getdicevalues() == null) {
            return RANDOM_ROLL_TAG;
        }
        int currentRound = navigationPane.getNbRolls() / (navigationPane.getNumberOfPlayers()*numberOfDice);
        int playerIndex = (navigationPane.getNbRolls() / navigationPane.getNumberOfPlayers())%navigationPane.getNumberOfPlayers() ;
        if (navigationPane.getdicevalues().get(playerIndex).size() > (currentRound*numberOfDice)) {
            return navigationPane.getdicevalues().get(playerIndex).get(currentRound*numberOfDice + index);
        }
        return RANDOM_ROLL_TAG;
    }
    public void finishRolling(){
        navigationPane.startMoving(navigationPane.getnb());
    }
    public void resetDieIndex(){
        dieindex = 0;
    }
    public int getdieindex(){
        return dieindex;
    }
    public int getnumberofdie(){
        return navigationPane.getNumberOfDice();
    }
}


