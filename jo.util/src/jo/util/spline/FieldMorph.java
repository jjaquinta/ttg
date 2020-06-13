package jo.util.spline;

import java.util.ArrayList;
import java.util.List;

public class FieldMorph
{
	private double[][]				mFromPoints;
	private double[][]				mToPoints;
	private List<TriMorph> 			mTriMorphs;
	
	public FieldMorph()
	{
		mFromPoints = new double[0][2];
		mToPoints = new double[0][2];
	}

	public double[] morph(double x, double y)
	{
		double[] from = new double[] { x, y };
		morph(from);
		return from;
	}
	
	public void morph(double[] from)
	{
		morph(from, from);
	}
	
	public void morph(double[] from, double[] to)
	{
		for (int i = 0; i < from.length; i += 2)
			morph(from, to, i);		
	}
	
	public void morph(double[] from, double[] to, int i)
	{
		triangulatePolygon();
		TriMorph t = findClosestMorph(from[i+0], from[i+1]);
		if (t != null)
			t.morph(from, to, i);
	}
	
	private TriMorph findClosestMorph(double x, double y)
	{
		for (TriMorph t : mTriMorphs)
			if (t.getFrom().isInside(x, y))
				return t;
		return null;
	}

	public void addPoint(double sx, double sy, double ex, double ey)
	{
		System.out.println("["+mFromPoints.length+"] "+sx+","+sy+" -> "+ex+","+ey);
		double[][] fromPoints = new double[mFromPoints.length + 1][2];
		double[][] toPoints = new double[mToPoints.length + 1][2];
		for (int i = 0; i < mFromPoints.length; i++)
		{
			fromPoints[i] = mFromPoints[i];
			toPoints[i] = mToPoints[i];
		}
		fromPoints[fromPoints.length - 1][0] = sx;
		fromPoints[fromPoints.length - 1][1] = sy;
		toPoints[toPoints.length - 1][0] = ex;
		toPoints[toPoints.length - 1][1] = ey;
		mFromPoints = fromPoints;
		mToPoints = toPoints;
	}

	public double[][] getFromPoints()
	{
		return mFromPoints;
	}

	public void setFromPoints(double[][] fromPoints)
	{
		mFromPoints = fromPoints;
	}

	public void setFromPoints(double[] fromPoints)
	{
		mFromPoints = new double[fromPoints.length/2][2];
		for (int i = 0; i < mFromPoints.length; i++)
		{
			mFromPoints[i][0] = fromPoints[i*2+0];
			mFromPoints[i][1] = fromPoints[i*2+1];
		}
	}

	public double[][] getToPoints()
	{
		return mToPoints;
	}

	public void setToPoints(double[][] toPoints)
	{
		mToPoints = toPoints;
	}

	public void setToPoints(double[] toPoints)
	{
		mToPoints = new double[toPoints.length/2][2];
		for (int i = 0; i < mToPoints.length; i++)
		{
			mToPoints[i][0] = toPoints[i*2+0];
			mToPoints[i][1] = toPoints[i*2+1];
		}
	}

	/*
	 * Triangulates a polygon using simple O(N^2) ear-clipping algorithm Returns
	 * a Triangle array unless the polygon can't be triangulated, in which case
	 * null is returned. This should only happen if the polygon self-intersects,
	 * though it will not _always_ return null for a bad polygon - it is the
	 * caller's responsibility to check for self-intersection, and if it
	 * doesn't, it should at least check that the return value is non-null
	 * before using. You're warned!
	 */
	private void triangulatePolygon()
	{
		if (mTriMorphs != null)
			return;
		mTriMorphs = new ArrayList<TriMorph>();
		if (mFromPoints.length < 3)
			return;

		int[] idx = new int[mFromPoints.length];
		for (int i = 0; i < mFromPoints.length; ++i)
			idx[i] = i;

		while (idx.length > 3)
		{
			// Find an ear
			int[] ear = findEar(idx);
			// If we still haven't found an ear, we're screwed.
			// The user did Something Bad, so return null.
			// This will probably crash their program, since
			// they won't bother to check the return value.
			// At this we shall laugh, heartily and with great gusto.
			if (ear == null)
			{
				System.out.println("Illegally formatted polygon!");
				return;
			}
			// - add the clipped triangle to the triangle list
			TriMorph toAdd = makeTriMorph(idx[ear[0]], idx[ear[1]], idx[ear[2]]); 
			mTriMorphs.add(toAdd);


			// Clip off the ear:
			// - remove the ear tip from the list
			int[] newIdx = new int[idx.length - 1];
			System.arraycopy(idx, 0, newIdx, 0, ear[0]);
			System.arraycopy(idx, ear[0]+1, newIdx, ear[0], newIdx.length - ear[0]);
			// - replace the old list with the new one
			idx = newIdx;
		}
		TriMorph toAdd = makeTriMorph(idx[1], idx[2], idx[0]); 
		mTriMorphs.add(toAdd);
	}

	private int[] findEar(int[] idx)
	{
		int[] ear = null;
		double earArea = 0;
		for (int i = 0; i < idx.length; ++i)
		{
			int[] e = isEar(i, idx);
			if (e == null)
				continue;
			Triangle eTri = makeFromTriangle(idx[e[0]], idx[e[1]], idx[e[2]]);
			double a = eTri.area();
			if ((ear == null) || (a < earArea))
			{
				ear = e;
				earArea = a;
			}
		}
		return ear;
	}
	
	// Checks if vertex i is the tip of an ear
	private int[] isEar(int i, int[] idx)
	{
		if (i >= idx.length || i < 0 || idx.length < 3)
		{
			return null;
		}
		int upper = i + 1;
		int lower = i - 1;
		if (i == 0)
			lower = idx.length - 1;
		else if (i == idx.length - 1)
			upper = 0;
		System.out.println("trying "+idx[lower]+", "+idx[i]+", "+idx[upper]+" "
			+"("+mFromPoints[idx[lower]][0]+","+mFromPoints[idx[lower]][1]+")"	
			+"("+mFromPoints[idx[i]][0]+","+mFromPoints[idx[i]][1]+")"	
			+"("+mFromPoints[idx[upper]][0]+","+mFromPoints[idx[upper]][1]+")"	
			);
		/*
		dx0 = mFromPoints[idx[i]][0] - mFromPoints[idx[lower]][0];
		dy0 = mFromPoints[idx[i]][1] - mFromPoints[idx[lower]][1];
		dx1 = mFromPoints[idx[upper]][0] - mFromPoints[idx[i]][0];
		dy1 = mFromPoints[idx[upper]][1] - mFromPoints[idx[i]][1];
		double cross = dx0*dy1 - dx1*dy0;
		if (cross > 0)
		{
			System.out.println("rejecting "+idx[lower]+", "+idx[i]+", "+idx[upper]);
			return null;
		}
		*/
		Triangle myTri = makeFromTriangle(idx[i], idx[upper], idx[lower]);
		if (myTri.isColinear())
		{
			System.out.println("Rejecting "+idx[lower]+", "+idx[i]+", "+idx[upper]+" = "+myTri.toString());
			return null;
		}
		for (int j = 0; j < idx.length; ++j)
		{
			if (j == i || j == lower || j == upper)
				continue;
			if (myTri.isInside(mFromPoints[idx[j]][0], mFromPoints[idx[j]][1]))
				return null;
		}
		System.out.println("Ear = "+idx[lower]+", "+idx[i]+", "+idx[upper]+" = "+myTri.toString());
		return new int[] { i, upper, lower };
	}
	
	private Triangle makeFromTriangle(int one, int two, int three)
	{
		return new Triangle(mFromPoints[one][0], mFromPoints[one][1], 
				mFromPoints[two][0], mFromPoints[two][1],
				mFromPoints[three][0], mFromPoints[three][1]);
	}
	
	private Triangle makeToTriangle(int one, int two, int three)
	{
		return new Triangle(mToPoints[one][0], mToPoints[one][1], 
				mToPoints[two][0], mToPoints[two][1],
				mToPoints[three][0], mToPoints[three][0]);
	}
	
	private TriMorph makeTriMorph(int one, int two, int three)
	{
		TriMorph tri = new TriMorph();
		tri.setFrom(makeFromTriangle(one, two, three));
		tri.setTo(makeToTriangle(one, two, three));
		return tri;
	}

	public List<TriMorph> getTriMorphs()
	{
		return mTriMorphs;
	}
}
