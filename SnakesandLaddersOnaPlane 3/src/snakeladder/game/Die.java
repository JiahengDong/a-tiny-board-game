package snakeladder.game;

import ch.aplu.jgamegrid.Actor;

public class Die extends Actor
{
  private RollDice rd;
  private int nb;

  Die(int nb, RollDice rd)
  {
    super("sprites/pips" + nb + ".gif", 7);
    this.nb = nb;
    this.rd = rd;
  }

  public void act()
  {
    showNextSprite();
    if (getIdVisible() == 6)
    {
      setActEnabled(false);
      if(rd.getdieindex() == rd.getnumberofdie()){
        rd.resetDieIndex();
        rd.finishRolling();
      }
    }
  }

}
