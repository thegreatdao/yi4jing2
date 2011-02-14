package iching.android.viewadapters;

import static iching.android.persistence.IChingSQLiteDBHelper.ORIGINAL_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.QUESTION;
import static iching.android.persistence.IChingSQLiteDBHelper.RELATING_ICON;
import iching.android.R;
import iching.android.activities.IChingView;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class DivinationsCursorAdapter extends SimpleCursorAdapter
{
	private int layout;
	
	public DivinationsCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to)
	{
		super(context, layout, c, from, to);
		this.layout = layout;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
        setUpDisplayList(view, cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		final LayoutInflater inflater = LayoutInflater.from(context);
        View listViewItem = inflater.inflate(layout, parent, false);
        setUpDisplayList(listViewItem, cursor);
		return listViewItem;
	}

	private void setUpDisplayList(View view, Cursor cursor)
	{
		TextView questionTextView = (TextView)view.findViewById(R.id.divination_question);
		String question = cursor.getString(cursor.getColumnIndex(QUESTION));
		questionTextView.setText(question);
		int originalIconId = cursor.getInt(cursor.getColumnIndex(ORIGINAL_ICON));
        ImageView originalIcon = (ImageView)view.findViewById(R.id.original_lines);
		setIcon(originalIconId, originalIcon);
		int relatingIconId = cursor.getInt(cursor.getColumnIndex(RELATING_ICON));
    	ImageView relatingIcon = (ImageView)view.findViewById(R.id.relating_lines);
    	setIcon(relatingIconId, relatingIcon);
	}
	
	private void setIcon(int id, ImageView icon)
	{
        icon.setImageResource(IChingView.HEXAGRAM_ICONS[id]);
	}
	
}
