package iching.android.activities;

import iching.android.R;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.utils.IChingHelper;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Divination extends Activity implements OnClickListener
{

	private Map<String, String> originalGua;
	private Map<String, String> relatingGua;
	private String question;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divination);
		Bundle extras = getIntent().getExtras();
		TextView yourQuestion = (TextView)findViewById(R.id.your_question);
		question = extras.getString(IChingSQLiteDBHelper.QUESTION);
		yourQuestion.setText(question);
		ArrayList<ImageView> originalImageViews = new ArrayList<ImageView>();
		originalImageViews.add((ImageView)findViewById(R.id.yao));
		originalImageViews.add((ImageView)findViewById(R.id.yao2));
		originalImageViews.add((ImageView)findViewById(R.id.yao3));
		originalImageViews.add((ImageView)findViewById(R.id.yao4));
		originalImageViews.add((ImageView)findViewById(R.id.yao5));
		originalImageViews.add((ImageView)findViewById(R.id.yao6));
		
		String lines = extras.getString(IChingSQLiteDBHelper.ORIGINAL_LINES);
		String changingLines = extras.getString(IChingSQLiteDBHelper.CHANGING_LINES);
		
		ArrayList<ImageView> relatingImageViews = new ArrayList<ImageView>();
		relatingImageViews.add((ImageView)findViewById(R.id.yao_));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_1));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_2));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_3));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_4));
		relatingImageViews.add((ImageView)findViewById(R.id.yao_5));
		displayHexagramByLines(lines, changingLines, Boolean.TRUE, originalImageViews);
		IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		originalGua = iChingSQLiteDBHelper.selectOneGuaByField(IChingSQLiteDBHelper.GUA_CODE, "'" + lines + "'");
		String originalTitle = IChingHelper.getLocalizedTitle(originalGua);
		TextView originalTitleTextView = (TextView)findViewById(R.id.gua_title);
		originalTitleTextView.setText(originalTitle);
		originalTitleTextView.setOnClickListener(this);
		if(changingLines.trim().length() != 0)
		{
			displayHexagramByLines(lines, changingLines, Boolean.FALSE, relatingImageViews);
			relatingGua = iChingSQLiteDBHelper.selectOneGuaByField(IChingSQLiteDBHelper.GUA_CODE, "'" + IChingHelper.getRelatingCode(lines, changingLines) + "'");
			String relatingTitle = IChingHelper.getLocalizedTitle(relatingGua);
			TextView relatingTitleTextView = (TextView)findViewById(R.id.gua_title2);
			relatingTitleTextView.setText(relatingTitle);
			relatingTitleTextView.setOnClickListener(this);
		}
	}
	
	private void displayHexagramByLines(String lines, String changingLines, Boolean isOriginal, ArrayList<ImageView> lineImages)
	{
		char[] digits = lines.toCharArray();
		int index = 0;
		for(char digit : digits)
		{
			boolean changingLine = changingLines.indexOf(String.valueOf(index+1)) != -1;
			ImageView imageView = lineImages.get(index);
			if(digit == '1' && changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yang_changing);
			}
			else if(digit == '1' && !changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yang);
			}
			else if(digit == '1' && changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yin_relating);
			}
			else if(digit == '1' && !changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yang);
			}
			else if(digit == '0' && changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yin_changing);
			}
			else if(digit == '0' && !changingLine && isOriginal)
			{
				imageView.setImageResource(R.drawable.yin);
			}
			else if(digit == '0' && changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yang_relating);
			}
			else if(digit == '0' && !changingLine && !isOriginal)
			{
				imageView.setImageResource(R.drawable.yin);
			}
			imageView.setVisibility(View.VISIBLE);
			index++;
		}
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.gua_title:
				loadHexagram(originalGua);
				break;
			case R.id.gua_title2:
				loadHexagram(relatingGua);
				break;
			default:
				break;
		}
		
	}
	
	private void loadHexagram(Map<String, String> gua)
	{
		Intent intent = new Intent(this, Gua.class);
		IChingHelper.setUpIntentWithGua(intent, gua);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.castiching_menu, menu);
		return Boolean.TRUE;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{
		Intent intent = new Intent(this, DivinationSMS.class);
		StringBuilder sb = new StringBuilder();
		sb.append(question + "\n");
		switch (menuItem.getOrder())
		{
			case 0:
				sb.append(originalGua.get(IChingSQLiteDBHelper.GUA_TITLE) + "\n" + originalGua.get(IChingSQLiteDBHelper.GUA_BODY));
				if(relatingGua != null)
				{
					sb.append("\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_TITLE) + "\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_BODY));
				}
				intent.putExtra(DivinationSMS.SMS_BODY, sb.toString());
				break;
			case 1:
				sb.append(originalGua.get(IChingSQLiteDBHelper.GUA_TITLE_CN) + "\n" + originalGua.get(IChingSQLiteDBHelper.GUA_BODY_CN));
				if(relatingGua != null)
				{
					sb.append("\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_TITLE_CN) + "\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_BODY_CN));
				}
				intent.putExtra(DivinationSMS.SMS_BODY, sb.toString());
				break;				
			default:
				sb.append(originalGua.get(IChingSQLiteDBHelper.GUA_TITLE_TW) + "\n" + originalGua.get(IChingSQLiteDBHelper.GUA_BODY_TW));
				if(relatingGua != null)
				{
					sb.append("\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_TITLE_TW) + "\n" + relatingGua.get(IChingSQLiteDBHelper.GUA_BODY_TW));
				}
				intent.putExtra(DivinationSMS.SMS_BODY, sb.toString());
				break;
		}
		startActivity(intent);
		return Boolean.TRUE;
	}
}

