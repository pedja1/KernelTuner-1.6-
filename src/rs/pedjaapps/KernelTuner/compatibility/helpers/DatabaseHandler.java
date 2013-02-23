/*
* This file is part of the Kernel Tuner.
*
* Copyright Predrag Čokulov <predragcokulov@gmail.com>
*
* Kernel Tuner is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Kernel Tuner is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Kernel Tuner. If not, see <http://www.gnu.org/licenses/>.
*/
package rs.pedjaapps.KernelTuner.compatibility.helpers;


import java.util.ArrayList;
import java.util.List;

import rs.pedjaapps.KernelTuner.compatibility.entry.Profile;
import rs.pedjaapps.KernelTuner.compatibility.entry.Voltage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "KTDatabase.db";

    // table names
    private static final String TABLE_PROFILES = "profiles";
    private static final String TABLE_VOLTAGE = "voltage";

    // Table Columns names
    private static final String KEY_PROFILE_ID = "id";
    private static final String KEY_PROFILE_NAME = "Name";
    private static final String KEY_PROFILE_CPU0MIN = "cpu0min";
    private static final String KEY_PROFILE_CPU0MAX = "cpu0max";
    private static final String KEY_PROFILE_CPU1MAX = "cpu1max";
    private static final String KEY_PROFILE_CPU1MIN = "cpu1min";

    private static final String KEY_PROFILE_CPU0GOV = "cpu0gov";
    private static final String KEY_PROFILE_CPU1GOV = "cpu1gov";
	private static final String KEY_PROFILE_VOLTAGE = "voltageProfile";
	private static final String KEY_PROFILE_BUTTONS_BACKLIGHT = "buttonsLight";
    private static final String KEY_PROFILE_VSYNC = "vsync";
	private static final String KEY_PROFILE_F_CHARGE = "fastcharge";
	private static final String KEY_PROFILE_CDEPTH = "cdepth";
	private static final String KEY_PROFILE_IOSCHEDULER = "IOScheduler";
	private static final String KEY_PROFILE_SDCACHE = "sdCache";
	private static final String KEY_PROFILE_SWEEP2WAKE = "sweep2wake";
  
	
    
    private static final String KEY_VOLTAGE_ID = "id";
    private static final String KEY_VOLTAGE_NAME = "Name";
    private static final String KEY_VOLTAGE_FREQ = "freq";
    private static final String KEY_VOLTAGE_VALUE = "value";

    public DatabaseHandler(Context context)
	{
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
	{
        String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
			+ KEY_PROFILE_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PROFILE_NAME + " TEXT,"
			+ KEY_PROFILE_CPU0MIN + " TEXT,"
			+ KEY_PROFILE_CPU0MAX + " TEXT," 
			+ KEY_PROFILE_CPU1MIN + " TEXT,"
			+ KEY_PROFILE_CPU1MAX + " TEXT,"
			+ KEY_PROFILE_CPU0GOV + " TEXT,"
			+ KEY_PROFILE_CPU1GOV + " TEXT,"
			+ KEY_PROFILE_VOLTAGE + " TEXT,"
			+ KEY_PROFILE_BUTTONS_BACKLIGHT + " TEXT,"
			+ KEY_PROFILE_VSYNC + " INTEGER,"
			+ KEY_PROFILE_F_CHARGE + " INTEGER,"
			+ KEY_PROFILE_CDEPTH + " TEXT,"
			+ KEY_PROFILE_IOSCHEDULER + " TEXT,"
			+ KEY_PROFILE_SDCACHE + " INTEGER,"
			+ KEY_PROFILE_SWEEP2WAKE + " INTEGER"
			
			+
			")";
        String CREATE_VOLTAGE_TABLE = "CREATE TABLE " + TABLE_VOLTAGE + "("
    			+ KEY_VOLTAGE_ID + " INTEGER PRIMARY KEY," + KEY_VOLTAGE_NAME + " TEXT," + KEY_VOLTAGE_FREQ + " TEXT," + KEY_VOLTAGE_VALUE + " TEXT"
    			+
    			")";
        db.execSQL(CREATE_PROFILES_TABLE);
        db.execSQL(CREATE_VOLTAGE_TABLE);
    }

    
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLTAGE);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

  
    public void addProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_NAME, profile.getName());
        values.put(KEY_PROFILE_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_PROFILE_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_PROFILE_CPU1MIN, profile.getCpu1min());
        values.put(KEY_PROFILE_CPU1MAX, profile.getCpu1max());
        values.put(KEY_PROFILE_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_PROFILE_CPU1GOV, profile.getCpu1gov());
		values.put(KEY_PROFILE_VOLTAGE,profile.getVoltage());
		values.put(KEY_PROFILE_BUTTONS_BACKLIGHT, profile.getButtonsLight());
        values.put(KEY_PROFILE_VSYNC, profile.getVsync());
        values.put(KEY_PROFILE_F_CHARGE, profile.getFcharge());
        values.put(KEY_PROFILE_CDEPTH, profile.getCdepth());
        values.put(KEY_PROFILE_IOSCHEDULER, profile.getIoScheduler());
        values.put(KEY_PROFILE_SDCACHE, profile.getSdcache());
        values.put(KEY_PROFILE_SWEEP2WAKE, profile.getSweep2wake());
       

        // Inserting Row
        db.insert(TABLE_PROFILES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single profile
   public Profile getProfile(int id)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_PROFILE_ID,
									 KEY_PROFILE_NAME,
									 KEY_PROFILE_CPU0MIN,
									 KEY_PROFILE_CPU0MAX,
									 KEY_PROFILE_CPU1MIN,
									 KEY_PROFILE_CPU1MAX,
									 KEY_PROFILE_CPU0GOV,
									 KEY_PROFILE_CPU1GOV,
									 KEY_PROFILE_VOLTAGE,
									 KEY_PROFILE_BUTTONS_BACKLIGHT,
									 KEY_PROFILE_VSYNC,
									 KEY_PROFILE_F_CHARGE,
									 KEY_PROFILE_CDEPTH,
									 KEY_PROFILE_IOSCHEDULER,
									 KEY_PROFILE_SDCACHE,
									 KEY_PROFILE_SWEEP2WAKE
									}, KEY_PROFILE_ID + "=?",
								 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)),
									  cursor.getString(1),
									  cursor.getString(2),
									  cursor.getString(3), 
									  cursor.getString(4), 
									  cursor.getString(5),
									  cursor.getString(6),
									  cursor.getString(7),
									  cursor.getString(8),
									  cursor.getString(9),
									  cursor.getString(10),
									  cursor.getString(11),
									  cursor.getString(12),
									  cursor.getString(13),
									  cursor.getInt(20),
									  cursor.getInt(21),
									  cursor.getString(22),
									  cursor.getString(23),
									  cursor.getInt(24),
									  
									  cursor.getInt(25)
									
									  
									  );
        // return contact
        db.close();
        cursor.close();
        return profile;
    }

    public Profile getProfileByName(String name)
	{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_PROFILE_ID,
									 KEY_PROFILE_NAME,
									 KEY_PROFILE_CPU0MIN,
									 KEY_PROFILE_CPU0MAX,
									 KEY_PROFILE_CPU1MIN,
									 KEY_PROFILE_CPU1MAX,
									 KEY_PROFILE_CPU0GOV,
									 KEY_PROFILE_CPU1GOV,
									 KEY_PROFILE_VOLTAGE,
									 KEY_PROFILE_BUTTONS_BACKLIGHT,
									 KEY_PROFILE_VSYNC,
									 KEY_PROFILE_F_CHARGE,
									 KEY_PROFILE_CDEPTH,
									 KEY_PROFILE_IOSCHEDULER,
									 KEY_PROFILE_SDCACHE,
									 KEY_PROFILE_SWEEP2WAKE
									}, KEY_PROFILE_NAME + "=?",
								 new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)),
									  cursor.getString(1),
									  cursor.getString(2),
									  cursor.getString(3), 
									  cursor.getString(4), 
									  cursor.getString(5),
									  cursor.getString(6),
									  cursor.getString(7),
									  cursor.getString(8),
									  cursor.getString(9),
									  cursor.getString(10),
									  cursor.getString(11),
									  cursor.getString(12),
									  cursor.getString(13),
									  cursor.getInt(20),
									  cursor.getInt(21),
									  cursor.getString(22),
									  cursor.getString(23),
									  cursor.getInt(24),
									  
									  cursor.getInt(25)
									);
        
        db.close();
        cursor.close();
        return profile;
    }

    // Getting All Contacts
    public List<Profile> getAllProfiles()
	{
        List<Profile> profileList = new ArrayList<Profile>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PROFILES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst())
		{
            do {
				Profile profile = new Profile();
                profile.setID(Integer.parseInt(cursor.getString(0)));
                profile.setName(cursor.getString(1));
                profile.setCpu0min(cursor.getString(2));
                profile.setCpu0max(cursor.getString(3));
                profile.setCpu1min(cursor.getString(4));
                profile.setCpu1max(cursor.getString(5));
                profile.setCpu0gov(cursor.getString(10));
                profile.setCpu1gov(cursor.getString(11));
				profile.setVoltage(cursor.getString(14));
				profile.setButtonsLight(cursor.getString(19));
                profile.setVsync(cursor.getInt(20));
                profile.setFcharge(cursor.getInt(21));
                profile.setCdepth(cursor.getString(22));
                profile.setIoScheduler(cursor.getString(23));
                profile.setSdcache(cursor.getInt(24));
                
                profile.setSweep2wake(cursor.getInt(25));
              
                
                // Adding  to list
                profileList.add(profile);
            } while (cursor.moveToNext());
        }

        // return list
        db.close();
        cursor.close();
        return profileList;
    }

    // Updating single 
    public int updateProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_NAME, profile.getName());
        values.put(KEY_PROFILE_CPU0MIN, profile.getCpu0min()); 
        values.put(KEY_PROFILE_CPU0MAX, profile.getCpu0max()); 
        values.put(KEY_PROFILE_CPU1MIN, profile.getCpu1min());
        values.put(KEY_PROFILE_CPU1MAX, profile.getCpu1max());
        values.put(KEY_PROFILE_CPU0GOV, profile.getCpu0gov());
        values.put(KEY_PROFILE_CPU1GOV, profile.getCpu1gov());
		values.put(KEY_PROFILE_VOLTAGE,profile.getVoltage());
		values.put(KEY_PROFILE_BUTTONS_BACKLIGHT, profile.getButtonsLight());
        values.put(KEY_PROFILE_VSYNC, profile.getVsync());
        values.put(KEY_PROFILE_F_CHARGE, profile.getFcharge());
        values.put(KEY_PROFILE_CDEPTH, profile.getCdepth());
        values.put(KEY_PROFILE_IOSCHEDULER, profile.getIoScheduler());
        values.put(KEY_PROFILE_SDCACHE, profile.getSdcache());
        values.put(KEY_PROFILE_SWEEP2WAKE, profile.getSweep2wake());
        
        
        // updating row
        db.close();
        return db.update(TABLE_PROFILES, values, KEY_PROFILE_ID + " = ?",
						 new String[] { String.valueOf(profile.getID()) });
        
	}

    // Deleting single profile
    public void deleteProfile(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_PROFILE_ID + " = ?",
				  new String[] { String.valueOf(profile.getID()) });
        db.close();
    }

    public void deleteProfileByName(Profile profile)
	{
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_PROFILE_NAME + " = ?",
				  new String[] { String.valueOf(profile.getName()) });
        db.close();
    }

    // Getting profile Count
    public int getProfileCount()
	{
        String countQuery = "SELECT  * FROM " + TABLE_PROFILES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    
    public void addVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();

           ContentValues values = new ContentValues();
           values.put(KEY_VOLTAGE_NAME, voltage.getName());
           values.put(KEY_VOLTAGE_FREQ, voltage.getFreq());
           values.put(KEY_VOLTAGE_VALUE, voltage.getValue()); 
         

           // Inserting Row
           db.insert(TABLE_VOLTAGE, null, values);
           db.close(); // Closing database connection
       }

       // Getting single 
      public Voltage getVoltage(int id)
   	{
           SQLiteDatabase db = this.getReadableDatabase();

           Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
   									 KEY_VOLTAGE_NAME,
   									 KEY_VOLTAGE_FREQ,
   									 KEY_VOLTAGE_VALUE}, KEY_VOLTAGE_ID + "=?",
   								 new String[] { String.valueOf(id) }, null, null, null, null);
           if (cursor != null)
               cursor.moveToFirst();

           Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
   									  cursor.getString(1),
   									  cursor.getString(2),
   									cursor.getString(3));
           // return 
           db.close();
           cursor.close();
           return voltage;
       }

       public Voltage getVoltageByName(String Name)
   	{
           SQLiteDatabase db = this.getReadableDatabase();

           Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
        		   						KEY_VOLTAGE_NAME,
        		   						KEY_VOLTAGE_FREQ,
   									 KEY_VOLTAGE_VALUE }, KEY_VOLTAGE_NAME + "=?",
   								 new String[] { Name }, null, null, null, null);
           if (cursor != null)
               cursor.moveToFirst();

           Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
   									  cursor.getString(1),
   									  cursor.getString(2),
   									cursor.getString(3));
           // return 
           db.close();
           cursor.close();
           return voltage;
       }
       
       public Voltage getVoltageByFreq(String freq)
      	{
              SQLiteDatabase db = this.getReadableDatabase();

              Cursor cursor = db.query(TABLE_VOLTAGE, new String[] { KEY_VOLTAGE_ID,
           		   						KEY_VOLTAGE_NAME,
           		   						KEY_VOLTAGE_FREQ,
      									 KEY_VOLTAGE_VALUE }, KEY_VOLTAGE_FREQ + "=?",
      								 new String[] { freq }, null, null, null, null);
              if (cursor != null)
                  cursor.moveToFirst();

              Voltage voltage = new Voltage(Integer.parseInt(cursor.getString(0)),
      									  cursor.getString(1),
      									  cursor.getString(2),
      									cursor.getString(3));
              // return 
              db.close();
              cursor.close();
              return voltage;
          }

       // Getting All 
       public List<Voltage> getAllVoltages()
   	{
           List<Voltage> voltageList = new ArrayList<Voltage>();
           // Select All Query
           String selectQuery = "SELECT  * FROM " + TABLE_VOLTAGE;

           SQLiteDatabase db = this.getWritableDatabase();
           Cursor cursor = db.rawQuery(selectQuery, null);

           // looping through all rows and adding to list
           if (cursor.moveToFirst())
   		{
               do {
   				Voltage voltage = new Voltage();
                   voltage.setID(Integer.parseInt(cursor.getString(0)));
                   voltage.setName(cursor.getString(1));
                   voltage.setFreq(cursor.getString(2));
                   voltage.setValue(cursor.getString(3));
                   
                   // Adding  to list
                   voltageList.add(voltage);
               } while (cursor.moveToNext());
           }

           // return  list
           db.close();
           cursor.close();
           return voltageList;
       }

       // Updating single 
       public int updateVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();

           ContentValues values = new ContentValues();
           values.put(KEY_VOLTAGE_NAME, voltage.getFreq());
           values.put(KEY_VOLTAGE_FREQ, voltage.getFreq());
           values.put(KEY_VOLTAGE_VALUE, voltage.getValue());

           // updating row
           db.close();
           return db.update(TABLE_VOLTAGE, values, KEY_VOLTAGE_ID + " = ?",
   						 new String[] { String.valueOf(voltage.getID()) });
       }

       // Deleting single 
       public void deleteVoltage(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();
           db.delete(TABLE_VOLTAGE, KEY_VOLTAGE_ID + " = ?",
   				  new String[] { String.valueOf(voltage.getID()) });
           db.close();
       }

       public void deleteVoltageByName(Voltage voltage)
   	{
           SQLiteDatabase db = this.getWritableDatabase();
           db.delete(TABLE_VOLTAGE, KEY_VOLTAGE_NAME + " = ?",
   				  new String[] { String.valueOf(voltage.getName()) });
           db.close();
       }

       // Getting  Count
       public int getVoltageCount()
   	{
           String countQuery = "SELECT  * FROM " + TABLE_VOLTAGE;
           SQLiteDatabase db = this.getReadableDatabase();
           Cursor cursor = db.rawQuery(countQuery, null);
           cursor.close();

           // return count
           return cursor.getCount();
       }

}
