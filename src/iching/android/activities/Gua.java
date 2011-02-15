package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.*;

import java.util.Locale;

import iching.android.R;
import iching.android.utils.IChingHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Gua extends Activity
{
	private TextView textView;
	private Bundle extras;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gua);
		Locale locale = Locale.getDefault();
		extras = getIntent().getExtras();
		String data = extras.getString(GUA_BODY);
		String title = extras.getString(GUA_TITLE);
		if(locale.equals(Locale.CHINA))
		{
			data = extras.getString(GUA_BODY_CN);
			title = extras.getString(GUA_TITLE_CN);
		}
		else if(locale.equals(Locale.TAIWAN))
		{
			data = extras.getString(GUA_BODY_TW);
			title = extras.getString(GUA_TITLE_TW);
		}
		textView = (TextView) findViewById(R.id.gua_content);
		textView.setText(data);
		setTitle(title);
		ImageView iconImage = (ImageView) findViewById(R.id.gua_icon);
		String icon = extras.getString(GUA_ICON);
		int iconId = IChingHelper.getId(icon, R.drawable.class);
		iconImage.setImageResource(iconId);
		ImageView us = (ImageView)findViewById(R.id.us);
		ImageView cn = (ImageView)findViewById(R.id.cn);
		ImageView hk = (ImageView)findViewById(R.id.hk);
		ChangeLanEventListener changeLanEventListener = new ChangeLanEventListener();
		us.setOnClickListener(changeLanEventListener);
		cn.setOnClickListener(changeLanEventListener);
		hk.setOnClickListener(changeLanEventListener);
	}

	private class ChangeLanEventListener implements OnClickListener
	{
		
		@Override
		public void onClick(View view)
		{
			ImageView lang = (ImageView)view;
			switch (lang.getId())
			{
				case R.id.us:
					setTitle(extras.getString(GUA_TITLE));
					textView.setText(extras.getString(GUA_BODY));
					break;
				case R.id.cn:
					setTitle(extras.getString(GUA_TITLE_CN));
					textView.setText(extras.getString(GUA_BODY_CN));
					break;
				case R.id.hk:
					setTitle(extras.getString(GUA_TITLE_TW));
					textView.setText(extras.getString(GUA_BODY_TW));
					break;
				default:
					break;
			}
		}
		
	}
}
