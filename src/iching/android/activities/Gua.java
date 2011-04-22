package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY_CN;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_BODY_TW;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE_CN;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE_TW;
import static iching.android.persistence.IChingSQLiteDBHelper.ID;
import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.utils.IChingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Gua extends Activity
{
	private static final int LANG_EN = 1;
	private static final int LANG_CN = 2;
	private static final int LANG_TW = 3;
	private static final int LOWER_BOUND = 0;
	private static final int UPPER_BOUND = 65;
	
	private int zoomTime;
	private TextView textViewBody;
	private Bundle extras;
	private int language;
	private ImageView iconImage;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	private List<MenuItem> menuItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init();
	}

	private void init()
	{
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, false);
		setContentView(R.layout.gua);
		Locale locale = Locale.getDefault();
		extras = getIntent().getExtras();
		String body = extras.getString(GUA_BODY);
		String title = extras.getString(GUA_TITLE);
		if(locale.equals(Locale.CHINA))
		{
			body = extras.getString(GUA_BODY_CN);
			title = extras.getString(GUA_TITLE_CN);
		}
		else if(locale.equals(Locale.TAIWAN))
		{
			body = extras.getString(GUA_BODY_TW);
			title = extras.getString(GUA_TITLE_TW);
		}
		textViewBody = (TextView) findViewById(R.id.gua_content);
		textViewBody.setText(body);
		setTitle(title);
		iconImage = (ImageView) findViewById(R.id.gua_icon);
		String icon = extras.getString(GUA_ICON);
		int iconId = IChingHelper.getId(icon, R.drawable.class);
		iconImage.setImageResource(iconId);
		ImageView us = (ImageView)findViewById(R.id.us);
		ImageView cn = (ImageView)findViewById(R.id.cn);
		ImageView hk = (ImageView)findViewById(R.id.hk);
		ImageView prev = (ImageView)findViewById(R.id.left_arrow);
		ImageView next = (ImageView)findViewById(R.id.right_arrow);
		ChangeLanEventListener changeLanEventListener = new ChangeLanEventListener();
		us.setOnClickListener(changeLanEventListener);
		cn.setOnClickListener(changeLanEventListener);
		hk.setOnClickListener(changeLanEventListener);
		prev.setOnClickListener(changeLanEventListener);
		next.setOnClickListener(changeLanEventListener);
	}

	private class ChangeLanEventListener implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			ImageView image = (ImageView)view;
			int id = Integer.parseInt(extras.getString(ID));
			switch (image.getId())
			{
				case R.id.us:
					setTitle(extras.getString(GUA_TITLE));
					textViewBody.setText(extras.getString(GUA_BODY));
					language = LANG_EN;
					break;
				case R.id.cn:
					setTitle(extras.getString(GUA_TITLE_CN));
					textViewBody.setText(extras.getString(GUA_BODY_CN));
					language = LANG_CN;
					break;
				case R.id.hk:
					setTitle(extras.getString(GUA_TITLE_TW));
					textViewBody.setText(extras.getString(GUA_BODY_TW));
					language = LANG_TW;
					break;
				case R.id.left_arrow:
					getNextGua(id - 1);
					break;
				case R.id.right_arrow:
					getNextGua(id + 1);
					break;
				default:
					break;
			}
			ScrollView guaScrollView = (ScrollView)findViewById(R.id.gua_body);
			guaScrollView.scrollTo(0, 0);
		}

		private void getNextGua(int id)
		{
			if(id <= LOWER_BOUND || id >= UPPER_BOUND)
			{
				Toast message = Toast.makeText(Gua.this, getString(R.string.end_of_records), Toast.LENGTH_SHORT);
				View messageView = message.getView();
				messageView.setBackgroundResource(R.drawable.button_pressed);
				message.show();
			}
			else
			{
				Map<String, String> gua = iChingSQLiteDBHelper.selectOneGuaByField(IChingSQLiteDBHelper.ID, id);
				Intent intent = IChingHelper.setUpIntentWithGua(new Intent(), gua);
				extras = intent.getExtras();
				String bodyContent = gua.get(GUA_BODY);
				String title = gua.get(GUA_TITLE);
				if(language == 0)
				{
					Locale locale = Locale.getDefault();
					if(locale.equals(Locale.CHINA))
					{
						language = LANG_CN;
					}
					else if(locale.equals(Locale.TAIWAN))
					{
						language = LANG_TW;
					}
					else
					{
						language = LANG_EN;
					}
				}
				
				if(language == LANG_CN)
				{
					bodyContent = gua.get(GUA_BODY_CN);
					title = gua.get(GUA_TITLE_CN);
				}
				else if (language == LANG_TW)
				{
					bodyContent = gua.get(GUA_BODY_TW);
					title = gua.get(GUA_TITLE_TW);
				}
				iconImage.setImageResource(IChingHelper.getId(gua.get(GUA_ICON), R.drawable.class));
				textViewBody.setText(bodyContent);
				setTitle(title);
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		switch (menuItem.getOrder())
		{
			case 0:
				textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, textViewBody.getTextSize() + 2);
				if(++zoomTime == 3)
				{
					menuItems.get(0).setEnabled(false);
				}
				menuItems.get(1).setEnabled(true);
				break;
			case 1:
				textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, textViewBody.getTextSize() - 2);
				if(--zoomTime == -3)
				{
					menuItems.get(1).setEnabled(false);
				}
				menuItems.get(0).setEnabled(true);
				break;
			default:
				break;
		}
		return Boolean.TRUE;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.font_menu, menu);
		menuItems = new ArrayList<MenuItem>();
		for(int i=0; i<menu.size(); i++)
		{
			menuItems.add(menu.getItem(i));
		}
		return true;
	}
}
