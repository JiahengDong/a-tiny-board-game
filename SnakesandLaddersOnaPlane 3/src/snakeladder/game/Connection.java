package snakeladder.game;

import ch.aplu.jgamegrid.Location;

public abstract class Connection
{
  Location locStart;
  Location locEnd;
  int cellStart;
  int cellEnd;
  //record the connection is swtiched or not
  boolean reversed  = false;
  Connection(int cellStart, int cellEnd)
  {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);
  }

  String imagePath;

  public Location getLocStart() {
    return locStart;
  }

  public Location getLocEnd() {
    return locEnd;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double xLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_HORIZONTAL_CELLS;
  }
  public double yLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_VERTICAL_CELLS;
  }

  //reverse connection
  public void reverse(){
    if(this.reversed == true){
      this.reversed = false;
    }else{
      this.reversed = true;
    }


    Location locTep = locStart;
    locStart = locEnd;
    locEnd = locTep;

    int cellTemp = cellStart;
    cellStart = cellEnd;
    cellEnd = cellTemp;
  }

}
