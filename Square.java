
public class Square{
  private double side;
  public final int MAX_SIDE_LENGTH = 10;

  public Square(double theSide){
    side = theSide;
  }

  public double getSide(){
    return side;
  }

  public void setSide(double theSide){
    if(theSide <= MAX_SIDE_LENGTH && theSide >0)
      side = theSide;
  }

  public String toString(){
     String output = "Square with side length = " + side;
     return output;
  }
  
  public double area(){
    return side*side;
  }

  public double diagonal(){
    double c = Math.sqrt(side*side + side*side);
    return c; 
  }
}
