package rs.pedjaapps.KernelTuner.compatibility.ui;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.ads.*;


import java.io.File;
import java.text.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.FileUtils;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.entry.SDSummaryEntry;
import rs.pedjaapps.KernelTuner.compatibility.helpers.SDSummaryAdapter;


public class SDScannerConfigActivity extends Activity
{

	
	
	
	private CheckBox sw;
	private static final int GET_CODE = 0;
	  String pt;

	  TextView path;
	  LinearLayout chart;
	  int labelColor;
	  SDSummaryAdapter summaryAdapter;
	  ProgressDialog pd;
	  List<SDSummaryEntry> entries;
	  String[] names = {"Applications(*.apk)", "Videos", "Music", "Images", "Documents", "Archives"};
	  int[] icons = {R.drawable.apk, R.drawable.movie, R.drawable.music, R.drawable.img, R.drawable.doc, R.drawable.arch};
	  private static final String CALCULATING = "calculating...";
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	   }

	  @Override
	  protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	  }
	  
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		
		super.onCreate(savedInstanceState);

		boolean isSDPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if(isSDPresent==false){
			finish();
			Toast.makeText(this, "External Storage not mounted", Toast.LENGTH_LONG).show();
		}
		setContentView(R.layout.sd_scanner_config);
		final SharedPreferences.Editor editor = preferences.edit();
		boolean ads = preferences.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}
		
		sw = (CheckBox)findViewById(R.id.switch1);
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				if(arg0.isChecked()){
					arg0.setText("Scann Folders+Files");
				}
				else if(arg0.isChecked()==false){
					arg0.setText("Scann Folders");
				}
			}
			
		});
		final CheckBox displayType = (CheckBox)findViewById(R.id.display_in_switch);
		displayType.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				if(arg0.isChecked()){
					arg0.setText("Dispay Result in List");
				}
				else if(arg0.isChecked()==false){
					arg0.setText("Dispay Result in Chart");
				}
			}
			
		});
		
		path = (TextView)findViewById(R.id.path);
		final EditText depth = (EditText)findViewById(R.id.editText2);
		final EditText numberOfItems = (EditText)findViewById(R.id.editText3);
		path.setText(preferences.getString("SDScanner_path", Environment.getExternalStorageDirectory().getPath()));
		pt = preferences.getString("SDScanner_path", Environment.getExternalStorageDirectory().getPath());
		depth.setText(preferences.getString("SDScanner_depth", "1"));
		numberOfItems.setText(preferences.getString("SDScanner_items", "20"));
		if(preferences.getBoolean("SDScanner_scann_type", false)){
		sw.setChecked(true);
		}
		else{
			sw.setChecked(false);
		}
		if(preferences.getBoolean("SDScanner_display_type", false)){
			displayType.setChecked(true);
		}
		else{
			displayType.setChecked(false);
		}
		Button scan = (Button)findViewById(R.id.button2);
		scan.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String scannType = " ";
				if(sw.isChecked()){
					scannType = " -a ";
					editor.putBoolean("SDScanner_scann_type", true);
				}
				else{
					editor.putBoolean("SDScanner_scann_type", false);
				}
				Intent intent = new Intent();
				intent.putExtra("path", pt);
				intent.putExtra("depth", depth.getText().toString());
				intent.putExtra("items", numberOfItems.getText().toString());
				intent.putExtra("scannType", scannType);
				if(displayType.isChecked()){
					intent.setClass(SDScannerConfigActivity.this, SDScannerActivityList.class);
					editor.putBoolean("SDScanner_display_type", true);
				}
				else{
					intent.setClass(SDScannerConfigActivity.this, SDScannerActivity.class);
					editor.putBoolean("SDScanner_display_type", false);
				}
				startActivity(intent);
				editor.putString("SDScanner_path", path.getText().toString());
				editor.putString("SDScanner_depth", depth.getText().toString());
				editor.putString("SDScanner_items", numberOfItems.getText().toString());
				
				editor.commit();
				
				
			}
			
		});
		
		((Button)findViewById(R.id.browse)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(SDScannerConfigActivity.this, FMActivity.class), GET_CODE);
				
			}
		});
		
		ListView summaryListView = (ListView) findViewById(R.id.list);
		summaryAdapter = new SDSummaryAdapter(this, R.layout.sd_conf_list_row);
		summaryListView.setAdapter(summaryAdapter);
		summaryAdapter.add(new SDSummaryEntry(names[0], CALCULATING, 0, 0, icons[0]));
		summaryAdapter.add(new SDSummaryEntry(names[1], CALCULATING, 0, 0, icons[1]));
		summaryAdapter.add(new SDSummaryEntry(names[2], CALCULATING, 0, 0, icons[2]));
		summaryAdapter.add(new SDSummaryEntry(names[3], CALCULATING, 0, 0, icons[3]));
		summaryAdapter.add(new SDSummaryEntry(names[4], CALCULATING, 0, 0, icons[4]));
		summaryAdapter.add(new SDSummaryEntry(names[5], CALCULATING, 0, 0, icons[5]));
		
		new ScanSDCard().execute();
	}

	@Override
	  protected void onResume() {
	    super.onResume();
	    ((TextView)findViewById(R.id.mem_total)).setText("Total: "+size(getTotalSpaceInBytes()));
		((TextView)findViewById(R.id.mem_used)).setText("Used: "+size(getUsedSpaceInBytes()));
		((TextView)findViewById(R.id.mem_free)).setText("Free: "+size(getAvailableSpaceInBytes()));
	  }

	
	public static long getAvailableSpaceInBytes() {
	    long availableSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    availableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();

	    return availableSpace;
	}
	
	public static long getUsedSpaceInBytes() {
	    long usedSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    usedSpace = ((long) stat.getBlockCount() - stat.getAvailableBlocks()) * (long) stat.getBlockSize();

	    return usedSpace;
	}
	public static long getTotalSpaceInBytes() {
	    long totalSpace = -1L;
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    totalSpace = (long) stat.getBlockCount() * (long) stat.getBlockSize();

	    return totalSpace;
	}

	public String size(long size){
		String hrSize = "";
		
		long b = size;
		double k = size/1024.0;
		double m = size/1048576.0;
		double g = size/1073741824.0;
		double t = size/1099511627776.0;
		
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
		else if(b>1){
			hrSize = dec.format(b).concat("B");
		}
		
		
		
		
		return hrSize;
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  if (requestCode == GET_CODE){
	   if (resultCode == RESULT_OK) {
		   pt = data.getStringExtra("path");
		   path.setText(pt);
	   }
	   
	  }
	 }
	private class ScanSDCard extends AsyncTask<String, Integer, Void> {
		long apk;
		long video;
		long music;
		long images;
		long doc;
		long arch;
		
		
		
		
		@Override
		protected Void doInBackground(String... args) {
			entries = new ArrayList<SDSummaryEntry>();
			Iterator<File> apkIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"apk"}, true);
			while(apkIt.hasNext()){
				apk+=apkIt.next().length();
	        }
			publishProgress(0);
			Iterator<File> videoIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"avi", "mp4", "mkv", "m4v", "3gp"}, true);
			while(videoIt.hasNext()){
				video+=videoIt.next().length();
	        }
			publishProgress(1);
			Iterator<File> musicIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"mp3", "wma", "wav", "aac"}, true);
			while(musicIt.hasNext()){
				music+=musicIt.next().length();
	        }
			publishProgress(2);
			Iterator<File> imgIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"jpg", "jpeg", "png", "bmp", "jcs", "mpo"}, true);
			while(imgIt.hasNext()){
				images+=imgIt.next().length();
	        }
			publishProgress(3);
			Iterator<File> docIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"docx", "xls", "ppt", "docx", "pptx", "xlsx", "pdf", "epub"}, true);
			while(docIt.hasNext()){
				doc+=docIt.next().length();
	        }
			publishProgress(4);
			Iterator<File> archIt = FileUtils.iterateFiles(Environment.getExternalStorageDirectory(), new String[] {"zip", "jar", "rar", "7zip", "tar", "gz"}, true);
			while(archIt.hasNext()){
				arch+=archIt.next().length();
	        }
			publishProgress(5);
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values)
		{
			switch(values[0]){
				case 0:
					summaryAdapter.remove(summaryAdapter.getItem(0));
					summaryAdapter.insert(new SDSummaryEntry(names[0], size(apk), apk, (int)(apk*100/getTotalSpaceInBytes()), icons[0]), 0);
					entries.add(new SDSummaryEntry(names[0], size(apk), apk, (int)(apk*100/getTotalSpaceInBytes()), icons[0]));
					break;
				case 1:
					summaryAdapter.remove(summaryAdapter.getItem(1));
					summaryAdapter.insert(new SDSummaryEntry(names[1], size(video), video, (int)(video*100/getTotalSpaceInBytes()), icons[1]), 1);
					entries.add(new SDSummaryEntry(names[1], size(video), video, (int)(video*100/getTotalSpaceInBytes()), icons[1]));
					break;
				case 2:
					summaryAdapter.remove(summaryAdapter.getItem(2));
					summaryAdapter.insert(new SDSummaryEntry(names[2], size(music), music, (int)(music*100/getTotalSpaceInBytes()), icons[2]), 2);
					entries.add(new SDSummaryEntry(names[2], size(music), music, (int)(music*100/getTotalSpaceInBytes()), icons[2]));
					break;
				case 3:
					summaryAdapter.remove(summaryAdapter.getItem(3));
					summaryAdapter.insert(new SDSummaryEntry(names[3], size(images), images, (int)(images*100/getTotalSpaceInBytes()), icons[3]), 3);
					entries.add(new SDSummaryEntry(names[3], size(images), images, (int)(images*100/getTotalSpaceInBytes()), icons[3]));
					break;
				case 4:
					summaryAdapter.remove(summaryAdapter.getItem(4));
					summaryAdapter.insert(new SDSummaryEntry(names[4], size(doc), doc, (int)(doc*100/getTotalSpaceInBytes()), icons[4]), 4);
					entries.add(new SDSummaryEntry(names[4], size(doc), doc, (int)(doc*100/getTotalSpaceInBytes()), icons[4]));
					break;
				case 5:
					summaryAdapter.remove(summaryAdapter.getItem(5));
					summaryAdapter.insert(new SDSummaryEntry(names[5], size(arch), arch, (int)(arch*100/getTotalSpaceInBytes()), icons[5]), 5);
					entries.add(new SDSummaryEntry(names[5], size(arch), arch, (int)(arch*100/getTotalSpaceInBytes()), icons[5]));
					break;
			}
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void res) {
			summaryAdapter.clear();
			Collections.sort(entries, new MyComparator());
			for(SDSummaryEntry e : entries){
				summaryAdapter.add(e);
			}
			summaryAdapter.notifyDataSetChanged();
		}
		@Override
		protected void onPreExecute(){
		
		}

	}
	
	
	


	class MyComparator implements Comparator<SDSummaryEntry>{
	  public int compare(SDSummaryEntry ob1, SDSummaryEntry ob2){
	   return ob2.getSize().compareTo(ob1.getSize()) ;
	  }
	}
	
	
}
