import java.io.Serializable;
import java.util.ArrayList;

public class Triangle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ArrayList<Point> points;
	
	public Triangle() {
		points = new ArrayList<Point>();
	}
	
	
	public void insertPoint(Point p)	{	points.add(p);	}
	public ArrayList<Point> getPoints()	{	return points;	}
	/*
	public Point getP1()	{	return points.get(0);	}
	public Point getP2()	{	return points.get(1);	}
	public Point getP3()	{	return points.get(2);	}
*/
}
