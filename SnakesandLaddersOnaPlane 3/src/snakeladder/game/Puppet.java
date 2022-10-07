package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.Point;

public class Puppet extends Actor
{
  private GamePane gamePane;
  private NavigationPane navigationPane;
  private int cellIndex = 0;
  private int nbSteps;
  private Connection currentCon = null;
  private int y;
  private int dy;
  private boolean isAuto;
  private String puppetName;
  private SwitchStrategy switchStrategy = new BasicStrategy();
  private Statistics stat;

  Puppet(GamePane gp, NavigationPane np, String puppetImage)
  {
    super(puppetImage);
    this.gamePane = gp;
    this.navigationPane = np;
  }

  public boolean isAuto() {
    return isAuto;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public String getPuppetName() {
    return puppetName;
  }

  public void setPuppetName(String puppetName) {
    this.puppetName = puppetName;
    stat = new Statistics(navigationPane.getNumberOfDice(), puppetName);
  }

  void go(int nbSteps)
  {
    if (cellIndex == 100)  // after game over
    {
      cellIndex = 0;
      setLocation(gamePane.startLocation);
    }
    this.nbSteps = nbSteps;
    setActEnabled(true);
    //statistics, rolling data records
    stat.recordRollingResults(nbSteps);
  }

  void resetToStartingPoint() {
    cellIndex = 0;
    setLocation(gamePane.startLocation);
    setActEnabled(true);
  }

  int getCellIndex() {
    return cellIndex;
  }

  private void moveToNextCell()
  {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() - 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    cellIndex++;
  }
  // Move to the previous cell.
  private void moveToPreviousCell() {
    int tens = cellIndex / 10;
    int ones = cellIndex - tens * 10;
    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
    {
      if (ones == 0 && cellIndex > 0)
        setLocation(new Location(getX(), getY() + 1));
      else
        setLocation(new Location(getX() - 1, getY()));
    }
    else     // Cells starting left 20, 40, .. 100
    {
      if (ones == 0)
        setLocation(new Location(getX(), getY() + 1));
      else
        setLocation(new Location(getX() + 1, getY()));
    }
    cellIndex--;
  }

  //apply switch
  public void applySwitch(){
    if(isAuto){
      boolean toToggle = switchStrategy.applyStrategy(navigationPane, gamePane);
      if(toToggle) {
        navigationPane.toggleButton();
      }
    }
  }

  public void printStat(){
    stat.printRollingResults();
    stat.printTraversals();
  }

  public void act()
  {
    if ((cellIndex / 10) % 2 == 0)
    {
      if (isHorzMirror())
        setHorzMirror(false);
    }
    else
    {
      if (!isHorzMirror())
        setHorzMirror(true);
    }
    // Animation: Move on connection
    if (currentCon != null  && nbSteps <= 0 && (navigationPane.getTotal_die_value() != navigationPane.getNumberOfDice() && currentCon.locEnd.y < currentCon.locStart.y)) //task2
    {
      int x = gamePane.x(y, currentCon);
      setPixelLocation(new Point(x, y));
      y += dy;

      // Check end of connection
      if ((dy > 0 && (y - gamePane.toPoint(currentCon.locEnd).y) > 0)
        || (dy < 0 && (y - gamePane.toPoint(currentCon.locEnd).y) < 0))
      {
        gamePane.setSimulationPeriod(100);
        setActEnabled(false);
        setLocation(currentCon.locEnd);
        cellIndex = currentCon.cellEnd;
        setLocationOffset(new Point(0, 0));
        currentCon = null;
        applySwitch();
        navigationPane.prepareRoll(cellIndex);
      }
      return;
    }
    // Normal movement
    if (nbSteps != 0)
    {
      if (nbSteps > 0) {
        moveToNextCell();
      }
      else {
        moveToPreviousCell();
      }

      if (cellIndex == 100)  // Game over
      {
        setActEnabled(false);
        navigationPane.prepareRoll(cellIndex);
        return;
      }

      nbSteps = nbSteps > 0 ? nbSteps - 1 : nbSteps + 1;
      if (nbSteps == 0)
      {
        // Check if on connection start
        if ((currentCon = gamePane.getConnectionAt(getLocation())) != null && (navigationPane.getTotal_die_value() != navigationPane.getNumberOfDice() && currentCon.locEnd.y < currentCon.locStart.y)) //task2
        {
          gamePane.setSimulationPeriod(50);
          y = gamePane.toPoint(currentCon.locStart).y;
          if (currentCon.locEnd.y > currentCon.locStart.y)
            dy = gamePane.animationStep;
          else
            dy = -gamePane.animationStep;
          if (currentCon instanceof Snake )
          {
            //record traversal of snakes
            if(currentCon.reversed){
              stat.updateTraversals("up");
            }else{
              stat.updateTraversals("down");
            }
            navigationPane.showStatus("Digesting...");
            navigationPane.playSound(GGSound.MMM);
          }
          else
          {
            //record traversal of ladders
            if(currentCon.reversed){
              stat.updateTraversals("down");
            }else{
              stat.updateTraversals("up");
            }
            navigationPane.showStatus("Climbing...");
            navigationPane.playSound(GGSound.BOING);
          }
        }
        else
        {
          applySwitch();
          setActEnabled(false);
          navigationPane.prepareRoll(cellIndex);
        }

      }
    }
  }

}
