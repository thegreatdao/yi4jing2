package iching.android.activities;

import iching.android.R;
import iching.android.service.MusicControl;
import android.app.ListActivity;

public abstract class IChingBaseListActivity extends ListActivity
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
