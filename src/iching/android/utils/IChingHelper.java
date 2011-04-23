package iching.android.utils;

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

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;

public class IChingHelper
{
	public static int getId(String name, Class<?> whichClass)
	{
		Field[] fields = whichClass.getFields();
		int iconId = R.drawable.bi;
		for (Field field : fields)
		{
			String fieldName = field.getName();
			if (fieldName.equals(name))
			{
				try
				{
					iconId = (Integer) field.get(null);
				}
				catch(Exception e)
				{
					throw new Error("no such icon");
				}
			}
		}
		return iconId;
	}
	
	public static String getIconFromInt(int value)
	{
		Field[] fields = R.drawable.class.getFields();
		String result = null;
		for (Field field : fields)
		{
			try
			{
				Integer id = (Integer)field.get(null);
				if(id == value)
				{
					result = field.getName();
					break;//hate doing this
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getRelatingCode(String originalCode, String changingLines)
	{
		char[] originalCodeArray = originalCode.toCharArray();
		char[] changingLineArray = changingLines.toCharArray();
		for(char digit : changingLineArray)
		{
			int index = Integer.parseInt(digit + "") - 1;
			char lineAtIndex = originalCodeArray[index];
			if(lineAtIndex == '1')
			{
				originalCodeArray[index] = '0';
			}
			else
			{
				originalCodeArray[index] = '1';
			}
		}
		return new String(originalCodeArray);
	}
	
	public static Intent setUpIntentWithGua(Intent intent, Map<String, String> gua)
	{
		intent.putExtra(GUA_BODY, gua.get(GUA_BODY));
		intent.putExtra(GUA_TITLE, gua.get(GUA_TITLE));
		intent.putExtra(GUA_BODY_CN, gua.get(GUA_BODY_CN));
		intent.putExtra(GUA_TITLE_CN, gua.get(GUA_TITLE_CN));
		intent.putExtra(GUA_BODY_TW, gua.get(GUA_BODY_TW));
		intent.putExtra(GUA_TITLE_TW, gua.get(GUA_TITLE_TW));
		intent.putExtra(GUA_ICON, gua.get(GUA_ICON));
		intent.putExtra(ID, gua.get(ID));
		return intent;
	}
	
	public static String getLocalizedTitle(Map<String, String> hexagram)
	{
		Locale locale = Locale.getDefault();
		if(locale.equals(Locale.CHINA))
		{
			return hexagram.get(IChingSQLiteDBHelper.GUA_TITLE_CN);
		}
		else if(locale.equals(Locale.TAIWAN))
		{
			return hexagram.get(IChingSQLiteDBHelper.GUA_TITLE_TW);
		}
		else
		{
			return hexagram.get(IChingSQLiteDBHelper.GUA_TITLE);
		}
	}
}
