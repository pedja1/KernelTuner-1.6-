package rs.pedjaapps.KernelTuner.compatibility.ui;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;

import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.google.ads.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.lang.Process;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.entry.SDScannerEntry;


public class SDScannerActivity extends Activity
{

	
	private ProgressDialog pd;
	private List<SDScannerEntry> entries = new ArrayList<SDScannerEntry>();
	public static final String TYPE = "type";

	  private static int[] COLORS = new int[] {Color.parseColor("#FF0000"), 
		  Color.parseColor("#F88017"), 
		  Color.parseColor("#FBB117"), 
		  Color.parseColor("#FDD017"),
		  Color.parseColor("#FFFF00"),
		  Color.parseColor("#FFFF00"),
		  Color.parseColor("#5FFB17"),
		  Color.GREEN,
		  Color.parseColor("#347C17"),
		  Color.parseColor("#387C44"),
		  Color.parseColor("#348781"),
		  Color.parseColor("#6698FF"),
		  Color.BLUE,
		  Color.parseColor("#6C2DC7"),
		  Color.parseColor("#7D1B7E"),
		  Color.WHITE,
		  Color.CYAN,
		  Color.MAGENTA,
		  Color.GRAY};
	  int labelColor;

	  private CategorySeries mSeries = new CategorySeries("");

	  private DefaultRenderer mRenderer = new DefaultRenderer();

	  private String mDateFormat;

	  private GraphicalView mChartView;

	  
	  LinearLayout chart;
	  String depth;
	  String numberOfItems;
	  String scannType;
	  
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    mSeries = (CategorySeries) savedState.getSerializable("current_series");
	    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
	    mDateFormat = savedState.getString("date_format");
	  }

	  @Override
	  protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putSerializable("current_series", mSeries);
	    outState.putSerializable("current_renderer", mRenderer);
	    outState.putString("date_format", mDateFormat);
	  }
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sd_scanner);
		mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	    mRenderer.setChartTitleTextSize(20);
	    mRenderer.setLabelsTextSize(15);
	    mRenderer.setLegendTextSize(15);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setZoomButtonsVisible(false);
	    mRenderer.setStartAngle(90);
		mRenderer.setAntialiasing(true);
		mRenderer.setLabelsColor(labelColor);
		mRenderer.setApplyBackgroundColor(false);
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
	
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		depth = intent.getStringExtra("depth");
		numberOfItems = intent.getStringExtra("items");
		scannType = intent.getStringExtra("scannType");
		
		new ScannSDCard().execute(new String[] {path,
				depth,
				numberOfItems,
				scannType});
		
	}

	@Override
	  protected void onResume() {
	    super.onResume();
	    if (mChartView == null) {
	      chart = (LinearLayout) findViewById(R.id.chart);
	      
	      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
	      mRenderer.setClickEnabled(true);
	      mRenderer.setSelectableBuffer(10);
	      mChartView.setOnClickListener(new View.OnClickListener() {
	          @Override
	          public void onClick(View v) {
	        	  

	          }
	        });
	      chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	          LayoutParams.MATCH_PARENT));
	    } else {
	      mChartView.repaint();
	    }
	  }

	private class ScannSDCard extends AsyncTask<String, String, Void> {
		String line;
		int numberOfItems;
		
		
		
		
		@Override
		protected Void doInBackground(String... args) {
			entries = new ArrayList<SDScannerEntry>();
			Process proc = null;
			try{
			numberOfItems = Integer.parseInt(args[2]);
			}
			catch(NumberFormatException e){
				numberOfItems = 20;
			}
			
			 
			try
			{
				proc = Runtime.getRuntime().exec(getFilesDir().getPath()+"/du -d "+args[1] + args[3] +args[0]);


				InputStream inputStream = proc.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				
				while ( ( line = bufferedReader.readLine() ) != null )
				{

					entries.add(new SDScannerEntry(line.substring(line.lastIndexOf("/")+1, line.length()),Integer.parseInt(line.substring(0, line.indexOf("/")).trim()), size(Integer.parseInt(line.substring(0, line.indexOf("/")).trim())), line.substring(line.indexOf("/"), line.length()).trim(), false) );
					publishProgress(line.substring(line.indexOf("/"), line.length()).trim());
					
				}
			}
			catch (IOException e)
			{
				Log.e("du","error "+e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onProgressUpdate(String... values)
		{
			pd.setMessage("scanning...\n"+values[0]);
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res) {
			pd.dismiss();
			
			Collections.sort(entries,new MyComparator());
			if(entries.isEmpty()==false){
			entries.remove(entries.get(0));
			}
			for(int i = entries.size(); i>numberOfItems; i--){
				entries.remove(entries.size()-1);
			}
			mSeries.clear();
				for(SDScannerEntry e : entries){
					mSeries.add(e.getFileName()   + " " +e.getHRsize(), e.getSize());
			        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			        mRenderer.addSeriesRenderer(renderer);
			        if (mChartView != null) {
			          mChartView.repaint();
			        }
				}
			
			
			
		}
		@Override
		protected void onPreExecute(){
		
			
			
			pd = new ProgressDialog(SDScannerActivity.this);
			pd.setIndeterminate(true);
			//pd.setTitle("Scanning SD Card");
			pd.setTitle("Please Wait...");
			pd.setIcon(R.drawable.info);
			//pd.setMessage("Please Wait\n" +
					//"This can take a while");
		
		pd.setOnCancelListener(new OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				new ScannSDCard(). cancel(true);
			}
			
		});
			pd.show();
		}

	}
	
	
	


	class MyComparator implements Comparator<SDScannerEntry>{
	  public int compare(SDScannerEntry ob1, SDScannerEntry ob2){
	   return ob2.getSize() - ob1.getSize() ;
	  }
	}
	
	private String size(int size){
		String hrSize = "";
		int k = size;
		double m = size/1024.0;
		double g = size/1048576.0;
		double t = size/1073741824.0;
		
		DecimalFormat dec = new DecimalFormat("0.00");
	
		if (t>1)
		{
	
			hrSize = dec.format(t).concat("TB");
		}
		else if (g>1)
		{
			
			hrSize = dec.format(g).concat("GB");
		}
		else if (m>1)
		{
		
			hrSize = dec.format(m).concat("MB");
		}
		else if (k>1)
		{
	
			hrSize = dec.format(k).concat("KB");

		}
		
		
		
		
		return hrSize;
		
	}
	
}
