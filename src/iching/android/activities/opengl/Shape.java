package iching.android.activities.opengl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public interface Shape
{
	FloatBuffer getVertexBuffer();
	ShortBuffer getIndexBuffer();
	int getNumberOfIndices();
}
