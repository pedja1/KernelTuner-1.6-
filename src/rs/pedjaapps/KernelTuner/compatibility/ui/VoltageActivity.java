package rs.pedjaapps.KernelTuner.compatibility.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import rs.pedjaapps.KernelTuner.compatibility.R;
import rs.pedjaapps.KernelTuner.compatibility.entry.Voltage;
import rs.pedjaapps.KernelTuner.compatibility.tools.ChangeVoltage;
import rs.pedjaapps.KernelTuner.compatibility.entry.VoltageEntry;
import rs.pedjaapps.KernelTuner.compatibility.helpers.DatabaseHandler;
import rs.pedjaapps.KernelTuner.compatibility.helpers.VoltageAdapter;
import rs.pedjaapps.KernelTuner.compatibility.helpers.IOHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public class VoltageActivity extends Activity
{

	private static VoltageAdapter voltageAdapter ;
	private ListView voltageListView;
	private DatabaseHandler db;
	
	private static List<Integer> voltages = new ArrayList<Integer>();
	private static List<String> voltageFreqs =  new ArrayList<String>();
	private static List<String> voltageFreqNames =  new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.voltage);
		
		
		
		
		

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ads = sharedPrefs.getBoolean("ads", true);
		if (ads == true)
		{AdView adView = (AdView)findViewById(R.id.ad);
			adView.loadAd(new AdRequest());}

		db = new DatabaseHandler(this);
		
		voltageListView = (ListView) findViewById(R.id.list);
		voltageAdapter = new VoltageAdapter(this, R.layout.voltage_list_item);
		voltageListView.setAdapter(voltageAdapter);

		for (final VoltageEntry entry : getVoltageEntries())
		{
			voltageAdapter.add(entry);
		}

		Button minus = (Button)findViewById(R.id.button1);
		minus.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"minus"});


				}

			});

		Button plus = (Button)findViewById(R.id.button2);
		plus.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
					new ChangeVoltage(VoltageActivity.this).execute(new String[] {"plus"});


				}

			});

		Button save = (Button)findViewById(R.id.button3);
		save.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle(getResources().getString(R.string.voltage_profile_name));

					builder.setMessage(getResources().getString(R.string.enter_voltage_profile_name));

					builder.setIcon(R.drawable.ic_menu_cc);

					final EditText input = new EditText(arg0.getContext());

					input.setGravity(Gravity.CENTER_HORIZONTAL);
					
					builder.setPositiveButton(getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								String name = String.valueOf(input.getText());
								
								String freqTemp;
								String valueTemp;
								StringBuilder freqBuilder  = new StringBuilder();
								StringBuilder valueBuilder  = new StringBuilder();
								for (String s : voltageFreqs){
									freqBuilder.append(s+" ");
								}
								for (int i=0; i<voltages.size(); i++){
									valueBuilder.append(String.valueOf(voltages.get(i))+" ");
								}
								freqTemp = freqBuilder.toString();
								valueTemp = valueBuilder.toString();
								db.addVoltage(new Voltage(name, freqTemp, 
										  valueTemp));

							}
						});
					builder.setView(input);

					AlertDialog alert = builder.create();

					alert.show();

				}

			});

		Button load = (Button)findViewById(R.id.button4);
		
		load.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				List<Voltage> voltages = db.getAllVoltages();
				List<CharSequence> items = new ArrayList<CharSequence>();
				for(Voltage v : voltages){
					items.add(v.getName());
				}
				final CharSequence[] items2;
				items2 = items.toArray(new String[0]);
				AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
				builder.setTitle(getResources().getString(R.string.select_profile));
				builder.setItems(items2, new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog, int item) {
				    	Voltage voltage = db.getVoltageByName(items2[item].toString()) ;
				    	VoltageAdapter.pd = ProgressDialog.show(VoltageActivity.this, null, getResources().getString(R.string.changing_voltage), true, false);
						new ChangeVoltage(VoltageActivity.this).execute(new String[] {"profile", voltage.getValue()});
				    	

				    	
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				
			}

		});

		Button clear = (Button)findViewById(R.id.button5);
		clear.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());

					builder.setTitle(getResources().getString(R.string.clear_voltage_profiles));

					builder.setMessage(getResources().getString(R.string.clear_voltage_profiles_confirm));

					builder.setIcon(R.drawable.ic_menu_delete);

					
					
					builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								
								List<Voltage> voltages = db.getAllVoltages();
								for(int i =0; i<voltages.size(); i++){
									db.deleteVoltageByName(voltages.get(i));
								}

							}
						});
					builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							

						}
					});
					
					AlertDialog alert = builder.create();

					alert.show();
					
			    	

				}

			});
		
		Button delete = (Button)findViewById(R.id.button6);
		delete.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View arg0)
				{
					
					final List<Voltage> voltages = db.getAllVoltages();
					List<CharSequence> items = new ArrayList<CharSequence>();
					for(Voltage v : voltages){
						items.add(v.getName());
					}
					final CharSequence[] items2;
					items2 = items.toArray(new String[0]);
					AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
					builder.setTitle(getResources().getString(R.string.select_profile_to_delte));
					builder.setItems(items2, new DialogInterface.OnClickListener() {
					    @Override
						public void onClick(DialogInterface dialog, int item) {
					    	db.deleteVoltageByName(voltages.get(item));
					    	
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();

				}

			});
		
	}

	public static void notifyChanges()
	{
		voltageAdapter.clear();
		for (final VoltageEntry entry : getVoltageEntries())
		{
			voltageAdapter.add(entry);
		}
		voltageAdapter.notifyDataSetChanged();
	}

	private static List<VoltageEntry> getVoltageEntries()
	{

		final List<VoltageEntry> entries = new ArrayList<VoltageEntry>();

		List<IOHelper.VoltageList> voltageList = IOHelper.voltages();
		if(voltageFreqs.isEmpty()==false){
			voltageFreqs.clear();
		}
		if(voltages.isEmpty()==false){
			voltages.clear();
		}
		if(voltageFreqNames.isEmpty()==false){
			voltageFreqNames.clear();
		}
		for(IOHelper.VoltageList v: voltageList){
			voltageFreqs.add((v.getFreq()));
		}
		for(IOHelper.VoltageList v: voltageList){
			voltages.add(v.getVoltage());
		}
		for(IOHelper.VoltageList v: voltageList){
			voltageFreqNames.add(v.getFreqName());
		}

		if (new File(IOHelper.VOLTAGE_PATH).exists())
		{
			for (int i= 0; i < voltages.size(); i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqNames.get(i), voltages.get(i)));
				

			}	

		}
		else if (new File(IOHelper.VOLTAGE_PATH_TEGRA_3).exists())
		{
			for (int i= 0; i < voltages.size(); i++)
			{	    	 
				entries.add(new VoltageEntry(voltageFreqNames.get(i), voltages.get(i)));
				

			}	
		}
		return entries;
	}
	
	
		
}
