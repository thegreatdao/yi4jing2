package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.CHANGING_LINES;
import static iching.android.persistence.IChingSQLiteDBHelper.CREATED_TIME;
import static iching.android.persistence.IChingSQLiteDBHelper.ID;
import static iching.android.persistence.IChingSQLiteDBHelper.ORIGINAL_ICON;
import static iching.android.persistence.IChingSQLiteDBHelper.ORIGINAL_LINES;
import static iching.android.persistence.IChingSQLiteDBHelper.QUESTION;
import static iching.android.persistence.IChingSQLiteDBHelper.RELATING_ICON;
import iching.android.R;
import iching.android.contentprovider.DivinationProvider;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.viewadapters.DivinationsCursorAdapter;

import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class Divinations extends ListActivity
{
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	private long divinationId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.divinations);
		setAdapter();
        getListView().setOnItemLongClickListener((new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				divinationId = id;
				showDialog(0);
//				just return true from onItemLongClick(); this means you have consumed the event, so it won't propagate up to the normal click handler. 
				return true;
			}
		}));
        iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.TRUE);
	}

	private void setAdapter()
	{
		Cursor cursor = managedQuery(DivinationProvider.CONTENT_URI, new String[] {ID, QUESTION, ORIGINAL_ICON, RELATING_ICON}, null, null, CREATED_TIME);
        String[] from = new String[]{QUESTION, ORIGINAL_ICON, RELATING_ICON};
        int[] to = new int[] {R.id.divination_question};
        DivinationsCursorAdapter divinationsCursorAdapter = new DivinationsCursorAdapter(this, R.layout.divination_list_item, cursor, from, to);
        setListAdapter(divinationsCursorAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		Map<String, String> divination = iChingSQLiteDBHelper.selectOneDivinationByField("_id", id, Locale.getDefault());
		Intent divinationIntent = new Intent(getBaseContext(), Divination.class);
		divinationIntent.putExtra(QUESTION, divination.get(QUESTION));
		divinationIntent.putExtra(ORIGINAL_LINES, divination.get(ORIGINAL_LINES));
		divinationIntent.putExtra(CHANGING_LINES, divination.get(CHANGING_LINES));
		startActivity(divinationIntent);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.confirm_delete_divination)
		.setCancelable(false)
		.setPositiveButton(R.string.yes,
		new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				deleteDiviantion();
				Toast message = Toast.makeText(getApplicationContext(), R.string.divination_deleted, Toast.LENGTH_SHORT);
				View messageView = message.getView();
				messageView.setBackgroundResource(R.drawable.button_pressed);
				message.show();
				setAdapter();
			}

			private void deleteDiviantion()
			{
				iChingSQLiteDBHelper.deleteDivinationById(divinationId);
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
	
}
