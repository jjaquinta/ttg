package jo.util.heal;

public interface IHEALVector extends IHEALCoord
{
	public int getDirection();
	public void setDirection(int v);
	public void turnLeft();
	public void turnRight();
	public IHEALVector move();
}
