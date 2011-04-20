package iching.android.activities;

import static iching.android.persistence.IChingSQLiteDBHelper.GUA_CODE;
import static iching.android.persistence.IChingSQLiteDBHelper.GUA_TITLE;
import static iching.android.persistence.IChingSQLiteDBHelper.ID;
import static iching.android.persistence.IChingSQLiteDBHelper.TABLE_DIVINATION;
import iching.android.R;
import iching.android.bean.Line;
import iching.android.persistence.IChingSQLiteDBHelper;
import iching.android.utils.IChingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CastIChing extends Activity implements OnClickListener, SensorEventListener
{
	private Handler handler;
	private int threadCount;
	private int tossTimes;
	private Line[] originalHexagramLines = new Line[6];
	private Map<String, String> originalHexagram;
	private Map<String, String> relatingHexagram;
	private IChingSQLiteDBHelper iChingSQLiteDBHelper;
	private String lines;
	private String changingLines;
	private String question;
	private Button saveDivinationButton;
	private List<ImageView> imageLines = new ArrayList<ImageView>(6);
	private List<ImageView> imageCoins = new ArrayList<ImageView>(6);
	private List<TextView> hexagramTitles = new ArrayList<TextView>(2);
	// 0 for tossing 1 for reset
	private static final int EMPTY_STRING = 0;
	private static final int RECORDS_FULL = 1;
	private int action;
	private static final int FLIP_COIN_TIMES = 1000;
	private int firstCoinImage;
	private int secondCoinImage;
	private int thirdCoinImage;
	private SensorManager sensorManager;
	private final double FORCE_THRESHOLD = 1.3;
	private boolean canShake;
	private ExecutorService threadPool;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		canShake= Boolean.TRUE;
		setContentView(R.layout.cast_iching);
		Button button = (Button) findViewById(R.id.tossCoin);
		TextView guaTitle = (TextView) findViewById(R.id.gua_title);
		TextView guaTitle2 = (TextView) findViewById(R.id.gua_title2);
		collectAllTitles(guaTitle, guaTitle2);
		saveDivinationButton = (Button)findViewById(R.id.saveDivination);
		button.setOnClickListener(this);
		guaTitle.setOnClickListener(this);
		guaTitle2.setOnClickListener(this);
		saveDivinationButton.setOnClickListener(this);		
		collectAllLines();
		collectAllCoins();
		handler = new Handler();
		iChingSQLiteDBHelper = new IChingSQLiteDBHelper(this, Boolean.TRUE);
		Toast message = Toast.makeText(this, getString(R.string.instruction_cast_Iching), Toast.LENGTH_SHORT);
		View messageView = message.getView();
		messageView.setBackgroundResource(R.drawable.button_pressed);
		message.show();
	}

	private void collectAllTitles(TextView guaTitle, TextView guaTitle2)
	{
		hexagramTitles.add(guaTitle);
		hexagramTitles.add(guaTitle2);
	}

	private void collectAllCoins()
	{
		imageCoins.add((ImageView)findViewById(R.id.first_coin));
		imageCoins.add((ImageView)findViewById(R.id.second_coin));
		imageCoins.add((ImageView)findViewById(R.id.third_coin));
	}

	private void collectAllLines()
	{
		imageLines.add((ImageView)findViewById(R.id.yao));
		imageLines.add((ImageView)findViewById(R.id.yao2));
		imageLines.add((ImageView)findViewById(R.id.yao3));
		imageLines.add((ImageView)findViewById(R.id.yao4));
		imageLines.add((ImageView)findViewById(R.id.yao5));
		imageLines.add((ImageView)findViewById(R.id.yao6));
		imageLines.add((ImageView)findViewById(R.id.yao_));
		imageLines.add((ImageView)findViewById(R.id.yao_1));
		imageLines.add((ImageView)findViewById(R.id.yao_2));
		imageLines.add((ImageView)findViewById(R.id.yao_3));
		imageLines.add((ImageView)findViewById(R.id.yao_4));
		imageLines.add((ImageView)findViewById(R.id.yao_5));
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.gua_title:
				showHexagram(Boolean.TRUE);
				break;
			case R.id.gua_title2:
				showHexagram(Boolean.FALSE);
				break;
			case R.id.tossCoin:
				if(action == 0)
				{
					tossCoin();
				}
				else
				{
					EditText question = (EditText)findViewById(R.id.question);
					Button tossButton = (Button)findViewById(R.id.tossCoin);
					reset(imageLines, imageCoins, hexagramTitles, question, tossButton);
				}
				break;
			case R.id.saveDivination:
				lines = getOriginalCodes(originalHexagramLines);
				changingLines = getChangingLinePositions(originalHexagramLines);
				question = ((EditText)findViewById(R.id.question)).getText().toString();
				String maximumNumOfRecords = Preferences.getStringValue(this, Preferences.KEY_DIVINATIONS, Preferences.DEFAULT_VALUE_DIVINATIONS);
				int maximumNumOfRecordsIntValue = Integer.parseInt(maximumNumOfRecords);
				if(maximumNumOfRecordsIntValue == -1)
				{
					maximumNumOfRecordsIntValue = Integer.MAX_VALUE;
				}
				if(question.trim().length()==0)
				{
					showDialog(0);
				}
				else if(iChingSQLiteDBHelper.getNumOfRecords(TABLE_DIVINATION) == maximumNumOfRecordsIntValue)
				{
					showDialog(1);
				}
				else
				{
					saveDivination(lines, changingLines, question);
				}
				break;
			default:
				break;
		}
	}

	private void showHexagram(boolean original)
	{
		Map<String, String> hexagram = relatingHexagram;
		if(original)
		{
			hexagram = originalHexagram;
		}
		Intent intent = new Intent(getApplicationContext(), Gua.class);
		IChingHelper.setUpIntentWithGua(intent, hexagram);
		startActivity(intent);
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		switch (id) 
		{
			case EMPTY_STRING:
				builder.setMessage(R.string.empty_question);
				builder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				break;
			case RECORDS_FULL:
				builder.setMessage(R.string.records_full);
				builder.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						iChingSQLiteDBHelper.deleteTopMostDivination();
						saveDivination(lines, changingLines, question);
					}
				}).setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				break;
			default:
				break;
		}
		
		return builder.create();
	}
	
	private void tossCoin()
	{
		final Button button = (Button) findViewById(R.id.tossCoin);
		button.setClickable(Boolean.FALSE);
		canShake = Boolean.FALSE;
		Runnable tossCoinRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				tossTimes++;
				tossTimes = tossTimes % 7;
				for(int i=0; i < FLIP_COIN_TIMES; i++)
				{
					tossCoins(i);
					threadCount++;
				}
				Runnable ichingChecker = new Runnable()
				{
					
					@Override
					public void run()
					{
						boolean run = true;
						while(run)
						{
							if(threadCount == FLIP_COIN_TIMES)
							{
								run = false;
								threadCount = 0;
								final int[] coins = {firstCoinImage, secondCoinImage, thirdCoinImage};
								Runnable setYaoRunnable = new Runnable(){
									@Override
									public void run()
									{
										setYao(coins);
										canShake = Boolean.TRUE;
									}
								};
								handler.post(setYaoRunnable);
								button.setClickable(Boolean.TRUE);
								if(tossTimes != 6)
								{
									action = 0;
								}
								else
								{
									action = 1;
									Runnable showRelatingHexgramRunnable = new Runnable()
									{
										
										@Override
										public void run()
										{
											String originalHexgramCode = getOriginalCodes(originalHexagramLines);
											originalHexagram = iChingSQLiteDBHelper.selectOneGuaByField("code", "'" + originalHexgramCode + "'");
											TextView originalTitle = (TextView) findViewById(R.id.gua_title);
											originalTitle.setText(originalHexagram.get(GUA_TITLE));
											button.setText(R.string.restCoin);
											String relatingHexgramCode = getRelatingCodes(originalHexagramLines);
											if(relatingHexagramExists(originalHexagramLines))
											{
												displayRelatingHexagram(originalHexagramLines);
												relatingHexagram = iChingSQLiteDBHelper.selectOneGuaByField("code", "'" + relatingHexgramCode + "'");
												TextView relatingTitle = (TextView) findViewById(R.id.gua_title2);
												relatingTitle.setText(relatingHexagram.get(GUA_TITLE));
											}
											saveDivinationButton.setVisibility(View.VISIBLE);
										}
									};
									handler.post(showRelatingHexgramRunnable);
									canShake = Boolean.FALSE;
								}
							}
						}
					}

					private boolean relatingHexagramExists(Line[] hexagram)
					{
						boolean result = false;
						for(Line line : hexagram)
						{
							if(line.isChanging())
							{
								result = true;
							}
						}
						return result;
					}
					
					private void displayRelatingHexagram(Line[] hexagrams)
					{
						int index = 0;
						for(Line line : hexagrams)
						{
							String sourcePrefix = "yao_";
							if(index != 0)
							{
								sourcePrefix += index;
							}
							ImageView relatingYao = (ImageView)findViewById(IChingHelper.getId(sourcePrefix, R.id.class));
							relatingYao.setVisibility(View.VISIBLE);
							if(line.isChanging())
							{
								if(line.isYang())
								{
									relatingYao.setImageResource(IChingHelper.getId("yin_relating", R.drawable.class));
								}
								else
								{
									relatingYao.setImageResource(IChingHelper.getId("yang_relating", R.drawable.class));
								}
							}
							else
							{
								if(!line.isYang())
								{
									relatingYao.setImageResource(IChingHelper.getId("yin", R.drawable.class));
								}
								else
								{
									relatingYao.setImageResource(IChingHelper.getId("yang", R.drawable.class));
								}
							}
							index++;
						}
					}
					
					private void setYao(int[] coins) {
						String idString = "yao";
						if(tossTimes != 1)
						{
							idString += tossTimes;
						}								
						int enableImageId = IChingHelper.getId(idString, R.id.class);
						ImageView yao = (ImageView) findViewById(enableImageId);
						int yaoSource = getYaoSource(coins, originalHexagramLines, tossTimes - 1);
						yao.setImageResource(yaoSource);
						yao.setVisibility(View.VISIBLE);
						int dot = 10;		// Length of a Morse Code "dot" in milliseconds
						long[] pattern = {
								0, 	// Start immediately
								dot
						};
						vibrate(pattern);
					}

					
					private int getYaoSource(int[] coins, Line[] originalHexagram, int index)
					{
						String result = null;
						int numOfYins = 0;
						int numOfYangs = 0;
						for(int i : coins)
						{
							if(i == R.drawable.manwen)
							{
								numOfYangs++;
							}
							else
							{
								numOfYins++;
							}
						}
						Line line = new Line();
						if(numOfYangs == 3)
						{
							result = "yang_changing";
							line.setChanging(Boolean.TRUE);
							line.setYang(Boolean.TRUE);
						}
						else if(numOfYins == 3)
						{
							result = "yin_changing";
							line.setChanging(Boolean.TRUE);
							line.setYang(Boolean.FALSE);
						}
						else if(numOfYangs == 2)
						{
							result = "yin";
							line.setChanging(Boolean.FALSE);
							line.setYang(Boolean.FALSE);
						}
						else
						{
							result = "yang";
							line.setChanging(Boolean.FALSE);
							line.setYang(Boolean.TRUE);
						}
						originalHexagram[index] = line;
						return IChingHelper.getId(result, R.drawable.class);
					}
				};
				threadPool.submit(ichingChecker);
			}
		};
		threadPool.submit(tossCoinRunnable);
	}
	
	private void tossCoins(int counter)
	{
		final ImageView firstCoin = (ImageView)findViewById(R.id.first_coin);
		final ImageView secondCoin = (ImageView)findViewById(R.id.second_coin);
		final ImageView thirdCoin = (ImageView)findViewById(R.id.third_coin);
		
		int headOrTailFirst = headOrTail();		
		handler.post(new TossCoinRunnable(firstCoin, headOrTailFirst));
		int headOrTailSecond = headOrTail();
		handler.post(new TossCoinRunnable(secondCoin, headOrTailSecond));
		int headOrTailThird = headOrTail();
		handler.post(new TossCoinRunnable(thirdCoin, headOrTailThird));
		if(counter + 1 == FLIP_COIN_TIMES)
		{
			firstCoinImage = headOrTailFirst;
			secondCoinImage = headOrTailSecond;
			thirdCoinImage = headOrTailThird;
		}
	}

	private int headOrTail()
	{
		double mid = 0.5;
		double random = Math.random();
		if(random > mid)
		{
			return R.drawable.hanwen;
		}
		else
		{
			return R.drawable.manwen;
		}
	}
	
	private void reset(List<ImageView> lines, List<ImageView> coins, List<TextView> hexagramTitles, EditText question, Button tossButton)
	{
		for(ImageView line : lines)
		{
			line.setVisibility(View.INVISIBLE);
		}
		for(ImageView coin : coins)
		{
			coin.setImageResource(R.drawable.default_coin);
		}
		for(TextView hexagramTitle : hexagramTitles)
		{
			hexagramTitle.setText("");
		}
		question.setText("");
		tossButton.setText(R.string.tossCoin);
		saveDivinationButton.setVisibility(View.INVISIBLE);
		action = 0;
		threadCount = 0;
		tossTimes = 0;
		originalHexagramLines = new Line[6];
		canShake = Boolean.TRUE;
	}
	
	private void saveDivination(String lines, String changingLines, String question)
	{
		int originalIcon = getIconIDByCode(lines, iChingSQLiteDBHelper);
		int changingIcon = 64;
		if(changingLines.trim().length() != 0)
		{
			String relatingCode = IChingHelper.getRelatingCode(lines, changingLines);
			changingIcon = getIconIDByCode(relatingCode, iChingSQLiteDBHelper);
		}
		iChingSQLiteDBHelper.insertDivination(lines, changingLines, question, originalIcon, changingIcon);
		Toast message = Toast.makeText(this, R.string.succesful_save, Toast.LENGTH_LONG);
		View messageView = message.getView();
		messageView.setBackgroundResource(R.drawable.button_pressed);
		message.show();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				View saveButton = findViewById(R.id.saveDivination);
				saveButton.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	private int getIconIDByCode(String lines, IChingSQLiteDBHelper iChingSQLiteDBHelper)
	{
		lines = "'" + lines + "'";
		Map<String, String> gua = iChingSQLiteDBHelper.selectOneGuaByField(GUA_CODE, lines, Locale.getDefault());
		return Integer.parseInt(gua.get(ID)) - 1;
	}
	
	private class TossCoinRunnable implements Runnable
	{
		private ImageView coin;
		private int source;
		
		TossCoinRunnable(ImageView coin, int source)
		{
			this.coin = coin;
			this.source = source;
		}
		
		@Override
		public void run()
		{
			if(source == R.drawable.manwen)
			{
				Matrix markerMatrix = new Matrix();
				coin.setImageMatrix(markerMatrix);
			}
			coin.setImageResource(source);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		iChingSQLiteDBHelper.close();
	}
	
	private String getOriginalCodes(Line[] lines)
	{
		StringBuilder code = new StringBuilder();
		for(Line line : lines)
		{
			if(line.isYang())
			{
				code.append('1');
			}
			else
			{
				code.append('0');
			}
		}
		return code.toString();
	}
	
	private String getRelatingCodes(Line[] lines)
	{
		StringBuilder code = new StringBuilder();
		for(Line line : lines)
		{
			char digit = '0';
			if(line.isYang() && !line.isChanging())
			{
				digit = '1';
			}
			if(!line.isYang() && line.isChanging())
			{
				digit = '1';
			}
			code.append(digit);
		}
		return code.toString();
	}
	
	private String getChangingLinePositions(Line[] lines)
	{
		StringBuilder positions = new StringBuilder();
		int index = 1;
		for(Line line : lines)
		{
			if(line.isChanging())
			{
				positions.append(index);
			}
			index++;
		}
		return positions.toString();
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent)
	{
		if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && canShake && tossTimes != 6)
		{
			float xAxis = sensorEvent.values[SensorManager.DATA_X];
			float yAxis = sensorEvent.values[SensorManager.DATA_Y];
			float zAxis = sensorEvent.values[SensorManager.DATA_Z];
			float gravityEarth = SensorManager.GRAVITY_EARTH;
			double totalForce = Math.pow(xAxis/gravityEarth, 2);
			totalForce += Math.pow(yAxis/gravityEarth, 2);
			totalForce += Math.pow(zAxis/gravityEarth, 2);
			totalForce = Math.sqrt(totalForce);
			if(totalForce > FORCE_THRESHOLD)
			{
				int dot = 100;		// Length of a Morse Code "dot" in milliseconds
				long[] pattern = {
						0, 	// Start immediately
						dot
				};
				vibrate(pattern);
				tossCoin();
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1)
	{
		
	}
	
	private void vibrate(long[] pattern)
	{
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		threadPool = Executors.newCachedThreadPool();
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		threadPool.shutdownNow();
		sensorManager.unregisterListener(this);
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
		startActivity(new Intent(this, DivinationSMS.class));
		return Boolean.TRUE;
	}
}
