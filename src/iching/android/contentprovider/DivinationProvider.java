package iching.android.contentprovider;

import static iching.android.persistence.IChingSQLiteDBHelper.TABLE_DIVINATION;
import iching.android.persistence.IChingSQLiteDBHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class DivinationProvider extends ContentProvider
{

	public static final String PROVIDER_DIVINATIONS = "iching.android.contentprovider.Divinations";

	public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_DIVINATIONS + "/divinations");

	private static final int DIVINATIONS = 1;
	private static final int DIVINATIONS_ID = 2;

	private static final UriMatcher uriMatcher;
	
	static
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_DIVINATIONS, "divinations", DIVINATIONS);
		uriMatcher.addURI(PROVIDER_DIVINATIONS, "divinations/#", DIVINATIONS_ID);
	}  
	   

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2)
	{
		return 0;
	}

	@Override
	public String getType(Uri arg0)
	{
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1)
	{
		return null;
	}

	@Override
	public boolean onCreate()
	{
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(getContext(), Boolean.FALSE);
		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
	    sqlBuilder.setTables(TABLE_DIVINATION);
	    Cursor cursor = sqlBuilder.query(iChingSQLiteDBHelper.getSqLiteDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3)
	{
		return 0;
	}

}
