package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.*;
import ch.aplu.util.*;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.utility.ServicesRandom;

import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid
        implements GGButtonListener
{
  private class SimulatedPlayer extends Thread
  {
    public void run()
    {
      while (true)
      {
        int total_dice_value = 0;
        Monitor.putSleep();
        gethandBtn().show(1);
        rd.rollalldice(getNumberOfDice());
        total_dice_value = nb;

      }
    }

  }

  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;
  private final int RANDOM_ROLL_TAG = -1;

  private final Location handBtnLocation = new Location(110, 70);
  private final Location dieBoardLocation = new Location(100, 180);
  private final Location pipsLocation = new Location(70, 230);
  private final Location statusLocation = new Location(20, 330);
  private final Location statusDisplayLocation = new Location(100, 320);
  private final Location scoreLocation = new Location(20, 430);
  private final Location scoreDisplayLocation = new Location(100, 430);
  private final Location resultLocation = new Location(20, 495);
  private final Location resultDisplayLocation = new Location(100, 495);

  private final Location autoChkLocation = new Location(15, 375);
  private final Location toggleModeLocation = new Location(95, 375);

  //location of indicator
  private final Location indicatorLocation = new Location(20,560);
  private final Location indicatorDisplayLocation = new Location(100,560);

  private final Location die1Location = new Location(20, 270);
  private final Location die2Location = new Location(50, 270);
  private final Location die3Location = new Location(80, 270);
  private final Location die4Location = new Location(110, 270);
  private final Location die5Location = new Location(140, 270);
  private final Location die6Location = new Location(170, 270);

  private GamePane gp;
  private GGButton handBtn = new GGButton("sprites/handx.gif");
  public GGButton gethandBtn(){return handBtn;}
  private RollDice rd = new RollDice(this);

  private GGButton die1Button = new CustomGGButton(DIE1_BUTTON_TAG, "sprites/Number_1.png");
  private GGButton die2Button = new CustomGGButton(DIE2_BUTTON_TAG, "sprites/Number_2.png");
  private GGButton die3Button = new CustomGGButton(DIE3_BUTTON_TAG, "sprites/Number_3.png");
  private GGButton die4Button = new CustomGGButton(DIE4_BUTTON_TAG, "sprites/Number_4.png");
  private GGButton die5Button = new CustomGGButton(DIE5_BUTTON_TAG, "sprites/Number_5.png");
  private GGButton die6Button = new CustomGGButton(DIE6_BUTTON_TAG, "sprites/Number_6.png");

  private GGTextField pipsField;
  private GGTextField statusField;
  private GGTextField resultField;
  private GGTextField scoreField;

  //indicator field
  private GGTextField indicatorField;

  private boolean isAuto;
  private GGCheckButton autoChk;
  private boolean isToggle = false;
  private GGCheckButton toggleCheck =
          new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);
  private int nbRolls = 0;
  private int numberOfDice = 0;
  private int total_die_value = 0;
  public int nb = 0;
  private volatile boolean isGameOver = false;
  private Properties properties;
  private java.util.List<java.util.List<Integer>> dieValues = new ArrayList<>();
  private GamePlayCallback gamePlayCallback;
  NavigationPane(Properties properties)
  {
    this.properties = properties;
    numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    System.out.println("numberOfDice = " + numberOfDice);
    isAuto = Boolean.parseBoolean(properties.getProperty("autorun"));
    autoChk = new GGCheckButton("Auto Run", YELLOW, TRANSPARENT, isAuto);
    System.out.println("autorun = " + isAuto);
    setSimulationPeriod(200);
    setBgImagePath("sprites/navigationpane.png");
    setCellSize(1);
    setNbHorzCells(200);
    setNbVertCells(600);
    doRun();
    new SimulatedPlayer().start();
  }
  public int getNumberOfDice(){return numberOfDice;}
  public int getnb(){return nb;}
  public void nbchange(int newvalue){ this.nb = newvalue;}
  public void NbRollschange(int newvalue){this.nbRolls = newvalue;}
  public int getNbRolls(){return nbRolls;}
  public int getNumberOfPlayers(){return gp.getNumberOfPlayers();}
  public int getTotal_die_value(){return total_die_value;}
  public java.util.List<java.util.List<Integer>> getdicevalues(){return dieValues;}
  void setupDieValues() {
    for (int i = 0; i < gp.getNumberOfPlayers(); i++) {
      java.util.List<Integer> dieValuesForPlayer = new ArrayList<>();
      if (properties.getProperty("die_values." + i) != null) {
        String dieValuesString = properties.getProperty("die_values." + i);
        String[] dieValueStrings = dieValuesString.split(",");
        for (int j = 0; j < dieValueStrings.length; j++) {
          dieValuesForPlayer.add(Integer.parseInt(dieValueStrings[j]));
        }
        dieValues.add(dieValuesForPlayer);
      } else {
        System.out.println("All players need to be set a die value for the full testing mode to run. " +
                "Switching off the full testing mode");
        dieValues = null;
        break;
      }
    }
    System.out.println("dieValues = " + dieValues);
  }

  void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

  void setGamePane(GamePane gp)
  {
    this.gp = gp;
    setupDieValues();
  }

  class ManualDieButton implements GGButtonListener {
    @Override
    public void buttonPressed(GGButton ggButton) {

    }

    @Override
    public void buttonReleased(GGButton ggButton) {

    }

    @Override
    public void buttonClicked(GGButton ggButton) {
      System.out.println("manual die button clicked");
      if (ggButton instanceof CustomGGButton) {
        CustomGGButton customGGButton = (CustomGGButton) ggButton;
        int tag = customGGButton.getTag();
        System.out.println("manual die button clicked - tag: " + tag);
        prepareBeforeRoll();
        rd.roll(tag);
        total_die_value += nb;
        nb = total_die_value;
        for(int i = 1;i < getNumberOfDice();i++) nbRolls++;
      }
    }
  }
  void addDieButtons() {
    ManualDieButton manualDieButton = new ManualDieButton();

    addActor(die1Button, die1Location);
    addActor(die2Button, die2Location);
    addActor(die3Button, die3Location);
    addActor(die4Button, die4Location);
    addActor(die5Button, die5Location);
    addActor(die6Button, die6Location);

    die1Button.addButtonListener(manualDieButton);
    die2Button.addButtonListener(manualDieButton);
    die3Button.addButtonListener(manualDieButton);
    die4Button.addButtonListener(manualDieButton);
    die5Button.addButtonListener(manualDieButton);
    die6Button.addButtonListener(manualDieButton);
  }

  public void toggleButton(){
    if(toggleCheck.isChecked()){
      toggleCheck.setChecked(false);
      gp.reverseAllConnections();
      showIndicator("Snake: down Ladder: up");
    }else if(!toggleCheck.isChecked()){
      toggleCheck.setChecked(true);
      gp.reverseAllConnections();
      showIndicator("Snake: up Ladder: down");
    }
  }

  void createGui()
  {
    addActor(new Actor("sprites/dieboard.gif"), dieBoardLocation);

    handBtn.addButtonListener(this);
    addActor(handBtn, handBtnLocation);
    addActor(autoChk, autoChkLocation);
    autoChk.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton button, boolean checked)
      {
        isAuto = checked;
        if (isAuto)
          Monitor.wakeUp();
      }
    });

    addActor(toggleCheck, toggleModeLocation);
    toggleCheck.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
        isToggle = checked;
        gp.reverseAllConnections();

        if(isToggle){
          showIndicator("Snake: up Ladder: down");
        }
        else{
          showIndicator("Snake: down Ladder: up");
        }

      }
    });

    addDieButtons();

    pipsField = new GGTextField(this, "", pipsLocation, false);
    pipsField.setFont(new Font("Arial", Font.PLAIN, 16));
    pipsField.setTextColor(YELLOW);
    pipsField.show();

    addActor(new Actor("sprites/linedisplay.gif"), statusDisplayLocation);
    statusField = new GGTextField(this, "Click the hand!", statusLocation, false);
    statusField.setFont(new Font("Arial", Font.PLAIN, 16));
    statusField.setTextColor(YELLOW);
    statusField.show();

    addActor(new Actor("sprites/linedisplay.gif"), scoreDisplayLocation);
    scoreField = new GGTextField(this, "# Rolls: 0", scoreLocation, false);
    scoreField.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreField.setTextColor(YELLOW);
    scoreField.show();

    addActor(new Actor("sprites/linedisplay.gif"), resultDisplayLocation);
    resultField = new GGTextField(this, "Current pos: 0", resultLocation, false);
    resultField.setFont(new Font("Arial", Font.PLAIN, 16));
    resultField.setTextColor(YELLOW);
    resultField.show();

    addActor(new Actor("sprites/linedisplay.gif"), indicatorDisplayLocation);
    indicatorField = new GGTextField(this, "Snake: down, Ladder: up", indicatorLocation, false);
    indicatorField.setFont(new Font("Arial", Font.PLAIN, 14));
    indicatorField.setTextColor(YELLOW);
    indicatorField.show();
  }

  void showIndicator(String text){
    indicatorField.setText(text);
    System.out.println(text);
  }

  void showPips(String text)
  {
    pipsField.setText(text);
    if (text != "") System.out.println(text);
  }

  void showStatus(String text)
  {
    statusField.setText(text);
    System.out.println("Status: " + text);
  }

  void showScore(String text)
  {
    scoreField.setText(text);
    System.out.println(text);
  }

  void showResult(String text)
  {
    resultField.setText(text);
    System.out.println("Result: " + text);
  }

  void prepareRoll(int currentIndex)
  {
    if (currentIndex == 100)  // Game over
    {
      playSound(GGSound.FADE);
      showStatus("Click the hand!");
      showResult("Game over");
      isGameOver = true;
      handBtn.setEnabled(true);

      java.util.List  <String> playerPositions = new ArrayList<>();
      for (Puppet puppet: gp.getAllPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
        puppet.printStat();
      }
      gamePlayCallback.finishGameWithResults(nbRolls % gp.getNumberOfPlayers(), playerPositions);
      gp.resetAllPuppets();
    }
    else
    {
      playSound(GGSound.CLICK);
      showStatus("Done. Click the hand!");
      String result = gp.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      showResult(result);
      gp.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );
      // Check whether there is a collision at the start of the player's round
      if (gp.detectCollision())
        gp.getPuppet().go(-1);
      if (isAuto) {
        Monitor.wakeUp();
      } else if (gp.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        handBtn.setEnabled(true);
      }
    }
  }

  void startMoving(int nb)
  {
    showStatus("Moving...");
    showPips("Total : " + nb);
    showScore("# Rolls: " + (++nbRolls));
    gp.getPuppet().go(nb);
    total_die_value = 0;
  }
  void prepareBeforeRoll() {
    handBtn.setEnabled(false);
    if (isGameOver)  // First click after game over
    {
      isGameOver = false;
      nbRolls = 0;
    }
  }

  public void buttonClicked(GGButton btn)
  {
    System.out.println("hand button clicked");
    prepareBeforeRoll();
    rd.rollalldice(getNumberOfDice());
  }


  public void buttonPressed(GGButton btn)
  {
  }

  public void buttonReleased(GGButton btn)
  {
  }

  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }
}
