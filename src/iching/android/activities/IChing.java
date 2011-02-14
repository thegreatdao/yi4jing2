package iching.android.activities;

import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.service.MusicControl;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IChing extends Activity implements OnClickListener
{

	private static final String ICON_MENU_ITEM_VIEW = "com.android.internal.view.menu.IconMenuItemView";
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		spinnningIcon();
		setUpListeners();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	private void spinnningIcon()
	{
		ImageView spinner = (ImageView) findViewById(R.id.spinnner);
		spinner.setImageResource(R.drawable.yinyang);
		Animation iconRotation = AnimationUtils.loadAnimation(this, R.anim.rotation_icon);
		spinner.startAnimation(iconRotation);
	}

	@SuppressWarnings("unused")
	private void rotateImage(ImageView spinner, int degrees)
	{
		Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		int height = source.getHeight();
		int width = source.getWidth();
		Matrix matrix = new Matrix();
		matrix.postRotate(degrees);
		Bitmap rotatedBMP = Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(rotatedBMP);
		spinner.setImageDrawable(bitmapDrawable);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if(Preferences.isMusicOn(this))
		{
			MusicControl.play(this, R.raw.bg);
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(Preferences.isMusicOn(this))
		{
			MusicControl.resume(this, R.raw.bg);
		}
		else
		{
			MusicControl.stop(this);
		}
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		MusicControl.stop(this);
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.references_check:
				Intent referencesCheckIntent = new Intent(this, IChingView.class);
				startActivity(referencesCheckIntent);
				break;
			case R.id.cast_iching:
				Intent castIChingIntent = new Intent(this, CastIChing.class);
				startActivity(castIChingIntent);
				break;
			case R.id.load_divinations:
				Intent loadDivinationIntent = new Intent(this, Divinations.class);
				startActivity(loadDivinationIntent);
				break;
			case R.id.randomizer:
				Intent randomizerIntent = new Intent(this, Randomizer.class);
				prepareGuas(randomizerIntent);
				startActivity(randomizerIntent);
				break;
			case R.id.about:
				Intent aboutIntent = new Intent(this, About.class);
				startActivity(aboutIntent);
				break;
			case R.id.exit:
				showDialog(0);
				break;
			default:
				break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		startActivity(new Intent(this, Preferences.class));
		return Boolean.TRUE;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		setMenuBackground();
		return true;
	}

	private void setMenuBackground()
	{
		getLayoutInflater().setFactory(new Factory()
		{
			@Override
			public View onCreateView(String name, Context context, AttributeSet attrs)
			{

				if (name.equalsIgnoreCase(ICON_MENU_ITEM_VIEW))
				{

					try
					{
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable()
						{
							public void run()
							{
								view.setBackgroundResource(R.drawable.option);
							}
						});
						return view;
					}
					catch (InflateException e)
					{
					}
					catch (ClassNotFoundException e)
					{
					}
				}
				return null;
			}
		});
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);;
		builder.setMessage(R.string.confirmation_exit_text)
		.setCancelable(false)
		.setPositiveButton(R.string.yes,
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				finish();
			}
		}).setNegativeButton(R.string.no,
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		return builder.create();
	}

	
	private void setUpListeners()
	{
		View referencesCheckButton = findViewById(R.id.references_check);
		referencesCheckButton.setOnClickListener(this);
		View castIChingButton = findViewById(R.id.cast_iching);
		castIChingButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit);
		exitButton.setOnClickListener(this);
		View loadDivinationButton = findViewById(R.id.load_divinations);
		loadDivinationButton.setOnClickListener(this);
		View randomizerButton = findViewById(R.id.randomizer);
		randomizerButton.setOnClickListener(this);
	}
	
	
	private void prepareGuas(Intent intent)
	{
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		List<String[]> guas = iChingSQLiteDBHelper.getGuasByGongs();
		for(int i=0; i<guas.size(); i++)
		{
			intent.putExtra(Integer.toString(i+1), guas.get(i));
		}
	}

	@Override
	public void onBackPressed()
	{
		showDialog(0);
	}
}