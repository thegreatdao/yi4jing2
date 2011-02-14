package iching.android.activities;

import iching.android.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		TextView textView = (TextView)findViewById(R.id.about);
		String about = getString(R.string.about_text);
		String developer = getString(R.string.developer);
		String email = getString(R.string.email);
		textView.setText(Html.fromHtml(stylizeText(about, developer, email)));
		setTitleColor(Color.rgb(41, 40, 41));
	}
	
	private String stylizeText(String first, String second, String email)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(first);
		sb.append("<br/><br/><font color=\"#F49A05\">");
		sb.append(second);
		sb.append("</font>");
		sb.append("<br/><b><font color=\"#0033CC\">");
		sb.append(email);
		sb.append("</font></b>");
		return sb.toString();
	}
}
