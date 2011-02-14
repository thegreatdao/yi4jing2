package iching.android.service;

import iching.android.activities.Preferences;
import android.content.Context;
import android.media.MediaPlayer;

public class MusicControl
{
	private static MediaPlayer mediaPlayer = null;
	
	public static void play(Context context, int resource)
	{
		if (Preferences.isMusicOn(context) && mediaPlayer == null)
		{
			playMusic(context, resource);
		}
	}	

	public static void pause(Context context)
	{
		mediaPlayer.pause();
	}
	
	public static void resume(Context context, int resource)
	{
		if(mediaPlayer == null)
		{
			playMusic(context, resource);
		}
		else
		{
			mediaPlayer.start();
		}
	}
		
	public static void stop(Context context)
	{
		if (mediaPlayer != null)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	private static void playMusic(Context context, int resource)
	{
		mediaPlayer = MediaPlayer.create(context, resource);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}

}
