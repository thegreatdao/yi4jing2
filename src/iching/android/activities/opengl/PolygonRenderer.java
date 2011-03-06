package iching.android.activities.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;

public class PolygonRenderer extends BaseIChing3DRenderer
{
	// Number of points or vertices we want to use
	@SuppressWarnings("unused")
	private final static int VERTS = 4;
	// A raw native buffer to hold the point coordinates
	private FloatBuffer mFVertexBuffer;
	// A raw native buffer to hold indices
	// allowing a reuse of points.
	private ShortBuffer mIndexBuffer;
	private int numOfIndices = 0;
	private long prevtime = SystemClock.uptimeMillis();
	private int sides = 3;

	public PolygonRenderer()
	{
		prepareBuffers(sides);
	}

	@Override
	protected void draw(GL10 gl)
	{
		long curtime = SystemClock.uptimeMillis();
		if ((curtime - prevtime) > 2000)
		{
			prevtime = curtime;
			sides += 1;
			if (sides > 20)
			{
				sides = 3;
			}
			this.prepareBuffers(sides);
		}
		// EvenPolygon.test();
		gl.glColor4f(1.0f, 0, 0, 0.5f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, this.numOfIndices,
				GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}

	private void prepareBuffers(int sides)
	{
		RegularPolygon t = new RegularPolygon(0, 0, 0, 1, sides);
		// RegularPolygon t = new RegularPolygon(1,1,0,1,sides);
		this.mFVertexBuffer = t.getVertexBuffer();
		this.mIndexBuffer = t.getIndexBuffer();
		this.numOfIndices = t.getNumberOfIndices();
		this.mFVertexBuffer.position(0);
		this.mIndexBuffer.position(0);
	}
}
