package iching.android.activities;

import iching.android.activities.opengl.PolygonRenderer;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class IChingOpenGL extends Activity
{
	private GLSurfaceView glSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		glSurfaceView = new GLSurfaceView(this);
		glSurfaceView.setEGLConfigChooser(false);
//		glSurfaceView.setRenderer(new SimpleTriangleRenderer());
		glSurfaceView.setRenderer(new PolygonRenderer());
		setContentView(glSurfaceView);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		glSurfaceView.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		glSurfaceView.onResume();
	}
	
	
}
