package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gua);
		Bundle extras = getIntent().getExtras();
		String data = extras.getString(GUA_BODY);
		TextView textView = (TextView) findViewById(R.id.gua_content);
		textView.setText(data);
		setTitle(extras.getString(GUA_TITLE));
		ImageView iconImage = (ImageView) findViewById(R.id.gua_icon);
		String icon = extras.getString(GUA_ICON);
		int iconId = IChingHelper.getId(icon, R.drawable.class);
		iconImage.setImageResource(iconId);
		ImageView us = (ImageView)findViewById(R.id.us);
		ImageView cn = (ImageView)findViewById(R.id.cn);
		ImageView hk = (ImageView)findViewById(R.id.hk);
	}

	private class ChangeLanEventListener implements OnClickListener
	{
		
		@Override
		public void onClick(View view)
		{
			
		}
		
	}
}
