package iching.android.viewadapters;

import static iching.android.R.drawable.bi;
import static iching.android.R.drawable.bi2;
import static iching.android.R.drawable.bo;
import static iching.android.R.drawable.cui;
import static iching.android.R.drawable.daguo;
import static iching.android.R.drawable.daxu;
import static iching.android.R.drawable.dayou;
import static iching.android.R.drawable.dazhuang;
import static iching.android.R.drawable.ding;
import static iching.android.R.drawable.dui;
import static iching.android.R.drawable.dun;
import static iching.android.R.drawable.feng;
import static iching.android.R.drawable.fou;
import static iching.android.R.drawable.fu;
import static iching.android.R.drawable.ge;
import static iching.android.R.drawable.gen;
import static iching.android.R.drawable.gou;
import static iching.android.R.drawable.guan;
import static iching.android.R.drawable.guimei;
import static iching.android.R.drawable.heng;
import static iching.android.R.drawable.huan;
import static iching.android.R.drawable.jian;
import static iching.android.R.drawable.jiaren;
import static iching.android.R.drawable.jie;
import static iching.android.R.drawable.jie2;
import static iching.android.R.drawable.jiji;
import static iching.android.R.drawable.jin;
import static iching.android.R.drawable.jing;
import static iching.android.R.drawable.jue;
import static iching.android.R.drawable.kan;
import static iching.android.R.drawable.kui;
import static iching.android.R.drawable.kun;
import static iching.android.R.drawable.kun2;
import static iching.android.R.drawable.li;
import static iching.android.R.drawable.lin;
import static iching.android.R.drawable.lv;
import static iching.android.R.drawable.meng;
import static iching.android.R.drawable.mingyi;
import static iching.android.R.drawable.qian;
import static iching.android.R.drawable.qian2;
import static iching.android.R.drawable.qian3;
import static iching.android.R.drawable.sheng;
import static iching.android.R.drawable.shi;
import static iching.android.R.drawable.shike;
import static iching.android.R.drawable.song;
import static iching.android.R.drawable.sui;
import static iching.android.R.drawable.sun;
import static iching.android.R.drawable.tai;
import static iching.android.R.drawable.tongren;
import static iching.android.R.drawable.tun;
import static iching.android.R.drawable.weiji;
import static iching.android.R.drawable.wuwang;
import static iching.android.R.drawable.xian;
import static iching.android.R.drawable.xiaochu;
import static iching.android.R.drawable.xiaoguo;
import static iching.android.R.drawable.xu;
import static iching.android.R.drawable.xun;
import static iching.android.R.drawable.yi;
import static iching.android.R.drawable.yi2;
import static iching.android.R.drawable.yu;
import static iching.android.R.drawable.zhen;
import static iching.android.R.drawable.zhong;
import static iching.android.R.drawable.lu;
import static iching.android.R.drawable.zhongfu;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IChingGridViewAdapter extends BaseAdapter
{
	private Integer[] hexagramImages = {qian3, kun, tun, meng, xu, song, shi, bi,
			xiaochu, lu, tai, fou, tongren, dayou, qian, yu,
			sui, zhong, lin, guan, shike, bi2, bo, fu,
			wuwang, daxu, yi, daguo, kan, li, xian, heng,
			dun, dazhuang, jin, mingyi, jiaren, kui, qian2, jie,
			sun, yi2, jue, gou, cui, sheng, kun2, jing,
			ge, ding, zhen, gen, jian ,guimei, feng, lv,
			xun, dui, huan, jie2, zhongfu, xiaoguo, jiji, weiji};
	private Context context;

	public IChingGridViewAdapter(Context context)
	{
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return hexagramImages.length;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null)
		{
			imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		}
		else
		{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(hexagramImages[position]);
		return imageView;
	}

}
