package iching.android.activities;

import static iching.android.R.drawable.hexagram1;
import static iching.android.R.drawable.hexagram10;
import static iching.android.R.drawable.hexagram11;
import static iching.android.R.drawable.hexagram12;
import static iching.android.R.drawable.hexagram13;
import static iching.android.R.drawable.hexagram14;
import static iching.android.R.drawable.hexagram15;
import static iching.android.R.drawable.hexagram16;
import static iching.android.R.drawable.hexagram17;
import static iching.android.R.drawable.hexagram18;
import static iching.android.R.drawable.hexagram19;
import static iching.android.R.drawable.hexagram2;
import static iching.android.R.drawable.hexagram20;
import static iching.android.R.drawable.hexagram21;
import static iching.android.R.drawable.hexagram22;
import static iching.android.R.drawable.hexagram23;
import static iching.android.R.drawable.hexagram24;
import static iching.android.R.drawable.hexagram25;
import static iching.android.R.drawable.hexagram26;
import static iching.android.R.drawable.hexagram27;
import static iching.android.R.drawable.hexagram28;
import static iching.android.R.drawable.hexagram29;
import static iching.android.R.drawable.hexagram3;
import static iching.android.R.drawable.hexagram30;
import static iching.android.R.drawable.hexagram31;
import static iching.android.R.drawable.hexagram32;
import static iching.android.R.drawable.hexagram33;
import static iching.android.R.drawable.hexagram34;
import static iching.android.R.drawable.hexagram35;
import static iching.android.R.drawable.hexagram36;
import static iching.android.R.drawable.hexagram37;
import static iching.android.R.drawable.hexagram38;
import static iching.android.R.drawable.hexagram39;
import static iching.android.R.drawable.hexagram4;
import static iching.android.R.drawable.hexagram40;
import static iching.android.R.drawable.hexagram41;
import static iching.android.R.drawable.hexagram42;
import static iching.android.R.drawable.hexagram43;
import static iching.android.R.drawable.hexagram44;
import static iching.android.R.drawable.hexagram45;
import static iching.android.R.drawable.hexagram46;
import static iching.android.R.drawable.hexagram47;
import static iching.android.R.drawable.hexagram48;
import static iching.android.R.drawable.hexagram49;
import static iching.android.R.drawable.hexagram5;
import static iching.android.R.drawable.hexagram50;
import static iching.android.R.drawable.hexagram51;
import static iching.android.R.drawable.hexagram52;
import static iching.android.R.drawable.hexagram53;
import static iching.android.R.drawable.hexagram54;
import static iching.android.R.drawable.hexagram55;
import static iching.android.R.drawable.hexagram56;
import static iching.android.R.drawable.hexagram57;
import static iching.android.R.drawable.hexagram58;
import static iching.android.R.drawable.hexagram59;
import static iching.android.R.drawable.hexagram6;
import static iching.android.R.drawable.hexagram60;
import static iching.android.R.drawable.hexagram61;
import static iching.android.R.drawable.hexagram62;
import static iching.android.R.drawable.hexagram63;
import static iching.android.R.drawable.hexagram64;
import static iching.android.R.drawable.hexagram7;
import static iching.android.R.drawable.hexagram8;
import static iching.android.R.drawable.hexagram9;
import static iching.android.R.drawable.yinyang;
import static iching.android.activities.Preferences.DEFAULT_VALUE_VIEW;
import static iching.android.activities.Preferences.KEY_VIEW;
import static iching.android.activities.Preferences.getStringValue;
import iching.android.R;
import iching.android.bean.Hexagram;
import iching.android.bean.TitleAndCode;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.utils.IChingHelper;
import iching.android.viewadapters.IChingGridViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class IChingView extends Activity
{
	private ViewSwitcher viewSwitcher;
	private boolean isGridView;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	public static final Integer[] HEXAGRAM_ICONS =
	{ hexagram1, hexagram2, hexagram3, hexagram4, hexagram5, hexagram6, hexagram7, hexagram8, hexagram9, hexagram10, hexagram11, hexagram12, hexagram13, hexagram14, hexagram15, hexagram16,
			hexagram17, hexagram18, hexagram19, hexagram20, hexagram21, hexagram22, hexagram23, hexagram24, hexagram25, hexagram26, hexagram27, hexagram28, hexagram29, hexagram30, hexagram31,
			hexagram32, hexagram33, hexagram34, hexagram35, hexagram36, hexagram37, hexagram38, hexagram39, hexagram40, hexagram41, hexagram42, hexagram43, hexagram44, hexagram45, hexagram46,
			hexagram47, hexagram48, hexagram49, hexagram50, hexagram51, hexagram52, hexagram53, hexagram54, hexagram55, hexagram56, hexagram57, hexagram58, hexagram59, hexagram60, hexagram61,
			hexagram62, hexagram63, hexagram64, yinyang };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final IChingSQLiteDBHelper iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.FALSE);
		this.iChingSQLiteDBHelper = iChingSQLiteDBHelper;
		setContentView(R.layout.iching_view_switcher);
		final Locale locale = Locale.getDefault();
		setListView(iChingSQLiteDBHelper, locale);
		viewSwitcher = (ViewSwitcher) findViewById(R.id.iching_view_switcher);
		setGridView(iChingSQLiteDBHelper);
		isGridView = getStringValue(this, KEY_VIEW, DEFAULT_VALUE_VIEW).equals("0");
		if (!isGridView)
		{
			viewSwitcher.showPrevious();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo)
	{
		super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
		contextMenu.setHeaderTitle(R.string.switch_view);
		contextMenu.add(Menu.NONE, view.getId(), Menu.NONE, R.string.grid_view);
		contextMenu.add(Menu.NONE, view.getId(), Menu.FIRST, R.string.list_view);
	}

	@Override
	public boolean onContextItemSelected(MenuItem menuItem)
	{
		int itemOrder = menuItem.getOrder();
		if (itemOrder == Menu.FIRST && isGridView)
		{
			isGridView = Boolean.FALSE;
			viewSwitcher.showPrevious();
		}
		if (itemOrder == Menu.NONE && !isGridView)
		{
			isGridView = Boolean.TRUE;
			viewSwitcher.showPrevious();
		}
		return super.onOptionsItemSelected(menuItem);
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		iChingSQLiteDBHelper.close();
	}

	private ListView setListView(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final Locale locale)
	{
		List<TitleAndCode> titles = iChingSQLiteDBHelper.selectAllTitlesAndCodes(locale);
		ListView listView = (ListView) findViewById(R.id.hexagrams_list_view);
		listView.setAdapter(new HexagramAdapter(this, R.layout.list_item, getHexagrams(titles, HEXAGRAM_ICONS)));
		registerForContextMenu(listView);
		setOnItemClickListener(iChingSQLiteDBHelper, listView);
		listView.setTextFilterEnabled(true);
		return listView;
	}

	private ArrayList<Hexagram> getHexagrams(List<TitleAndCode> titleAndCodes, Integer[] icons)
	{
		ArrayList<Hexagram> result = new ArrayList<Hexagram>();
		int index = 0;
		for (TitleAndCode titleAndCode : titleAndCodes)
		{
			Hexagram hexagram = new Hexagram();
			hexagram.setTitle(titleAndCode.getTitle());
			hexagram.setCode(titleAndCode.getCode());
			hexagram.setIcon(icons[index]);
			result.add(hexagram);
			index++;
		}
		return result;
	}

	private GridView setGridView(final IChingSQLiteDBHelper iChingSQLiteDBHelper)
	{
		GridView gridView = (GridView) findViewById(R.id.hexagrams_grid_view);
		registerForContextMenu(gridView);
		gridView.setAdapter(new IChingGridViewAdapter(this));
		setOnItemClickListener(iChingSQLiteDBHelper, gridView);
		return gridView;
	}

	private void setOnItemClickListener(final IChingSQLiteDBHelper iChingSQLiteDBHelper, final AdapterView<?> adapterView)
	{
		adapterView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				final Intent intent = new Intent(getApplicationContext(), Gua.class);
				Map<String, String> gua;
				if(adapterView instanceof ListView)
				{
					gua = iChingSQLiteDBHelper.selectOneGuaByField(IChingSQLiteDBHelper.GUA_CODE, "'" + ((TextView)v.findViewById(R.id.hiddenCode)).getText().toString() + "'");
				}
				else
				{
					gua = iChingSQLiteDBHelper.selectOneGuaByField(IChingSQLiteDBHelper.ID, position + 1);
				}
				IChingHelper.setUpIntentWithGua(intent, gua);
				startActivity(intent);
			}

		});
	}

	private class HexagramAdapter extends ArrayAdapter<Hexagram> implements Filterable
	{
		private ArrayList<Hexagram> allHexagrams;
		private ArrayList<Hexagram> filteredHexagrams;
		private IChingFilter filter;

		@Override
		public int getCount()
		{
			return filteredHexagrams.size();
		}

		public HexagramAdapter(Context context, int textViewResourceId, ArrayList<Hexagram> hexagrams)
		{
			super(context, textViewResourceId, hexagrams);
			allHexagrams = hexagrams;
			filteredHexagrams = hexagrams;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View view = convertView;
			if (view == null)
			{
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = layoutInflater.inflate(R.layout.list_item, null);
			}
			Hexagram hexagram = filteredHexagrams.get(position);
			if (hexagram != null)
			{
				TextView hexagramText = (TextView) view.findViewById(R.id.hexagram_text);
				hexagramText.setText(hexagram.getTitle());
				ImageView hexagramIcon = (ImageView) view.findViewById(R.id.hexagram_icon);
				hexagramIcon.setImageResource(hexagram.getIcon());
				TextView codeTextView = (TextView) view.findViewById(R.id.hiddenCode);
				codeTextView.setText(hexagram.getCode());
			}
			return view;
		}

		@Override
		public Filter getFilter()
		{
			if (filter == null)
			{
				filter = new IChingFilter();
			}
			return filter;
		}

		private class IChingFilter extends Filter
		{

			@Override
			protected FilterResults performFiltering(CharSequence constraint)
			{
				ArrayList<Hexagram> hexs = HexagramAdapter.this.allHexagrams;
				FilterResults results = new FilterResults();
				String constraintString = constraint.toString().trim();
				if (constraint == null || constraintString.length() == 0)
				{
					synchronized (allHexagrams)
					{
						results.values = hexs;
						results.count = hexs.size();
					}
				}
				else
				{
					synchronized (filteredHexagrams)
					{
						List<Hexagram> hexagrams = new ArrayList<Hexagram>();
						for (Hexagram hexagram : hexs)
						{
							if (hexagram.getCode().startsWith(constraintString))
							{
								hexagrams.add(hexagram);
							}
						}
						results.values = hexagrams;
						results.count = hexagrams.size();
					}
				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results)
			{
				filteredHexagrams = (ArrayList<Hexagram>) results.values;
				notifyDataSetChanged();
			}
		}
	}

}
