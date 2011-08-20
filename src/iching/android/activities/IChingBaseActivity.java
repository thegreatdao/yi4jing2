package iching.android.activities;

import iching.android.R;
import iching.android.service.MusicControl;
import android.app.Activity;

public abstract class IChingBaseActivity extends Activity
{

	@Override
	protected void onPause()
	{
		super.onPause();
		MusicControl.pause(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (Preferences.isMusicOn(this))
		{
			MusicControl.resume(this, R.raw.bg);
		}
		else
		{
			MusicControl.stop(this);
		}
	}
	
}
