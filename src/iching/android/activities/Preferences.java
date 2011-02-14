package iching.android.activities;

import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.service.MusicControl;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class Preferences extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	private static final String KEY_MUSIC = "musicPref";
	private static final boolean DEFAULT_VALUE_MUSIC = Boolean.TRUE;
	public static final String KEY_VIEW = "viewPref";
	public static final String DEFAULT_VALUE_VIEW = "0";
	public static final String KEY_DIVINATIONS = "numOfRecordsPref";
	public static final String DEFAULT_VALUE_DIVINATIONS = "10";
	
	private ListPreference viewListPreference;
	private ListPreference numOfRecordsListPreference;
	private CheckBoxPreference musicCheckBoxPreference;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	private String initNumOfRecords;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		viewListPreference = (ListPreference)getPreferenceScreen().findPreference(KEY_VIEW);
		numOfRecordsListPreference = (ListPreference)getPreferenceScreen().findPreference(KEY_DIVINATIONS);
		musicCheckBoxPreference = (CheckBoxPreference)getPreferenceScreen().findPreference(KEY_MUSIC);
		setUpMusicSummary(KEY_MUSIC);
		setUpViewSummary(KEY_VIEW);		
		setUpNumOfRecordsSummary(KEY_DIVINATIONS);
		initNumOfRecords = getStringValue(this, KEY_DIVINATIONS, DEFAULT_VALUE_DIVINATIONS);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if(key.equals(KEY_MUSIC))
		{
			if(isMusicOn(this))
			{
				MusicControl.play(this, R.raw.bg);				
			}
			else
			{
				MusicControl.stop(this);
			}
			setUpMusicSummary(key);
		}
		else if(key.equals(KEY_VIEW))
		{
			setUpViewSummary(key);
		}
		else if(key.equals(KEY_DIVINATIONS))
		{
			int numOfRecords = (int)iChingSQLiteDBHelper.getNumOfRecords(IChingSQLiteDBHelper.TABLE_DIVINATION);
			String currentValueString = getStringValue(this, key, DEFAULT_VALUE_DIVINATIONS);
			int currentValue = Integer.parseInt(currentValueString);
			if(currentValue > 0 && numOfRecords > currentValue)
			{
				showDialog(0);
				numOfRecordsListPreference.setValue(initNumOfRecords);
			}
			else
			{
				initNumOfRecords = currentValueString;
				setUpNumOfRecordsSummary(key);
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);;
		builder.setMessage(R.string.exceed_numember_of_records_warning_title)
		.setCancelable(false)
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		return builder.create();
	}
	
	private void setUpMusicSummary(String key)
	{
		String musicSummary = getString(R.string.music_summary);
		String musicValue = "";
		if(isMusicOn(this))
		{
			musicValue = getString(R.string.on);
		}
		else
		{
			musicValue = getString(R.string.off);
		}
		musicValue = getString(R.string.open_bracket) + musicValue + getString(R.string.close_bracket);
		musicSummary = stylizeValue(musicSummary, musicValue);
		musicCheckBoxPreference.setSummary(Html.fromHtml(musicSummary));
	}
	
	private void setUpNumOfRecordsSummary(String key)
	{
		String numOfRecordsSummary = getString(R.string.divination_summary);
		String numOfRecordsValue = getStringValue(this, key, DEFAULT_VALUE_DIVINATIONS);
		if(numOfRecordsValue.equals("-1"))
		{
			numOfRecordsValue = getString(R.string.infinite);
		}
		numOfRecordsValue = getString(R.string.open_bracket) + numOfRecordsValue + getString(R.string.close_bracket);
		numOfRecordsSummary = stylizeValue(numOfRecordsSummary, numOfRecordsValue);
		numOfRecordsListPreference.setSummary(Html.fromHtml(numOfRecordsSummary));
	}

	private void setUpViewSummary(String key)
	{
		String viewSummary = getString(R.string.hexagrams_view_preference_summary);
		String viewValue = getString(R.string.current_value_view_on);
		if(getStringValue(this, key, DEFAULT_VALUE_VIEW).equals("0"))
		{
			viewValue = getString(R.string.current_value_view_off);
		}
		viewSummary = stylizeValue(viewSummary, viewValue);
		viewListPreference.setSummary(Html.fromHtml(viewSummary));
	}
	
	private String stylizeValue(String summary, String value)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<font color=\"#000000\">");
		stringBuilder.append(summary + " ");
		stringBuilder.append("</font>");
		stringBuilder.append("<b><font color=\"#F49A05\">");
		stringBuilder.append(value);
		stringBuilder.append("</font></b>");
		return stringBuilder.toString();
	}
	
	@SuppressWarnings("unused")
	private void stylizeSummary(String input, Preference preference)
	{
		Spannable summary = new SpannableString (input);
		summary.setSpan(new ForegroundColorSpan(R.color.title_color), 0, summary.length(), 0 );
		preference.setSummary(summary);
	}
	
	public static boolean isMusicOn(Context context)
	{
		return getSharedPreferences(context).getBoolean(KEY_MUSIC, DEFAULT_VALUE_MUSIC);
	}
	
	public static String getStringValue(Context context, String key, String defaultValue)
	{
		return getSharedPreferences(context).getString(key, defaultValue);
	}
	
	private static SharedPreferences getSharedPreferences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
