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
package rs.pedjaapps.KernelTuner.compatibility.entry;

public class Profile
{

	//private variables
    int _id;
    String _Name;
    String _cpu0min;
    String _cpu0max;
    String _cpu1min;
    String _cpu1max;
    String _cpu0gov;
    String _cpu1gov;
    String _voltageProfile;
    String _buttonsLight;
    int _vsync;
    int _fCharge;
    String _colorDepth;
    String _IOScheduler;
    int _sdCache;
    
    int _sweep2wake;
   
    
   

    // Empty constructor
    public Profile()
	{

    }
    // constructor
    public Profile(int id, String Name, 
    				
    				String cpu0min, 
    				String cpu0max, 
    				String cpu1min,
				   String cpu1max,
				   String cpu0gov,
				   String cpu1gov,
				   String voltageProfile,
				   String mtu,
				   String mtd,
				   String gpu2d,
				   String gpu3d,
				   String buttonsLight,
				   int vsync,
				   int fCharge,
				   String colorDepth,
				   String IOScheduler,
				   int sdCache,
				   
				   int sweep2wake
				 )
	{
        this._id = id;
        this._Name = Name;
        
        this._cpu0min = cpu0min;
        this._cpu0max = cpu0max;
        this._cpu1min = cpu1min;
        this._cpu1max = cpu1max;
        this._cpu0gov = cpu0gov;
        this._cpu1gov = cpu1gov;
        this._voltageProfile = voltageProfile;
        this._buttonsLight = buttonsLight;
        this._vsync = vsync;
        this._fCharge = fCharge;
        this._colorDepth = colorDepth;
		  this._IOScheduler = IOScheduler;
		   this._sdCache = sdCache;
		   this._sweep2wake = sweep2wake;
        
  


    }

    // constructor
    public Profile(String Name, 
    		
			String cpu0min, 
			String cpu0max, 
			String cpu1min,
		   String cpu1max,
		   String cpu0gov,
		   String cpu1gov,
		   String voltageProfile,
		   String buttonsLight,
		   int vsync,
		   int fCharge,
		   String colorDepth,
		   String IOScheduler,
		   int sdCache,
		   int sweep2wake
				 )
	{

    	this._Name = Name;
    	
        this._cpu0min = cpu0min;
        this._cpu0max = cpu0max;
        this._cpu1min = cpu1min;
        this._cpu1max = cpu1max;
        this._cpu0gov = cpu0gov;
        this._cpu1gov = cpu1gov;
        this._voltageProfile = voltageProfile;
        this._buttonsLight = buttonsLight;
        this._vsync = vsync;
        this._fCharge = fCharge;
        this._colorDepth = colorDepth;
		  this._IOScheduler = IOScheduler;
		   this._sdCache = sdCache;
		   this._sweep2wake = sweep2wake;
      
    }
    // getting ID
    public int getID()
	{
        return this._id;
    }

    // setting id
    public void setID(int id)
	{
        this._id = id;
    }

	// getting Name
    public String getName()
	{
        return this._Name;
    }

    // setting Name
    public void setName(String Name)
	{
        this._Name = Name;
    }

    // getting cpu0min
    public String getCpu0min()
	{
        return this._cpu0min;
    }

    // setting cpu0min
    public void setCpu0min(String cpu0min)
	{
        this._cpu0min = cpu0min;
    }

    // getting phone cpu0max
    public String getCpu0max()
	{
        return this._cpu0max;
    }

    // setting phone cpu0max
    public void setCpu0max(String cpu0max)
	{
        this._cpu0max = cpu0max;
    }

	// getting phone cpu1min
    public String getCpu1min()
	{
        return this._cpu1min;
    }

    // setting phone cpu1min
    public void setCpu1min(String cpu1min)
	{
        this._cpu1min = cpu1min;
    }

	// getting phone cpu1max
    public String getCpu1max()
	{
        return this._cpu1max;
    }

    // setting phone cpu1max
    public void setCpu1max(String cpu1max)
	{
        this._cpu1max = cpu1max;
    }

	
	
	// getting phone cpu0gov
    public String getCpu0gov()
	{
        return this._cpu0gov;
    }

    // setting phone cpu0gov
    public void setCpu0gov(String cpu0gov)
	{
        this._cpu0gov = cpu0gov;
    }

	// getting phone cpu1gov
    public String getCpu1gov()
	{
        return this._cpu1gov;
    }

    // setting phone cpu1gov
    public void setCpu1gov(String cpu1gov)
	{
        this._cpu1gov = cpu1gov;
    }

	


	// getting phone vsync
    public int getVsync()
	{
        return this._vsync;
    }

    // setting phone vsync
    public void setFcharge(int fCharge)
	{
        this._fCharge = fCharge;
    }

	public int getFcharge()
	{
        return this._fCharge;
    }

	public void setSdcache(int sd)
	{
        this._sdCache = sd;
    }

	public int getSdcache()
	{
        return this._sdCache;
    }
	
	public void setSweep2wake(int s2w)
	{
        this._sweep2wake = s2w;
    }

	public int getSweep2wake()
	{
        return this._sweep2wake;
    }
	
    // setting phone vsync
    public void setVsync(int vsync)
	{
        this._vsync = vsync;
    }
	
	
	public String getVoltage()
	{
        return this._voltageProfile;
    }

    public void setVoltage(String voltageProfile)
	{
        this._voltageProfile = voltageProfile;
    }
	
	public String getCdepth()
	{
        return this._colorDepth;
    }

    public void setCdepth(String cDepth)
	{
        this._colorDepth = cDepth;
    }
	
	
	
	public String getIoScheduler()
	{
        return this._IOScheduler;
    }

    public void setIoScheduler(String io)
	{
        this._IOScheduler = io;
    }
	
	public String getButtonsLight()
	{
        return this._buttonsLight;
    }

    public void setButtonsLight(String buttonsLight)
	{
        this._buttonsLight = buttonsLight;
    }
	
	
   /* public int getCpu(){
    	return this._cpu;
    }
    
    public void setCpu(int cpu){
    	this._cpu = cpu;
    }
    
    public int getVt(){
    	return this._vt;
    }
    
    public void setVt(int vt){
    	this._vt = vt;
    }
    public int getMd(){
    	return this._md;
    }
    
    public void setMd(int md){
    	this._md = md;
    }
    public int getGpu(){
    	return this._gpu;
    }
    
    public void setGpu(int gpu){
    	this._gpu = gpu;
    }
    public int getCbl(){
    	return this._cbl;
    }
    
    public void setCbl(int cbl){
    	this._cbl = cbl;
    }
    public int getVs(){
    	return this._vs;
    }
    
    public void setVs(int vs){
    	this._vs = vs;
    }
    public int getFc(){
    	return this._fc;
    }
    
    public void setFc(int fc){
    	this._fc = fc;
    }
    
    public int getCd(){
    	return this._cd;
    }
    
    public void setCd(int cd){
    	this._cd = cd;
    }
    
    public int getIo(){
    	return this._io;
    }
    
    public void setIo(int io){
    	this._io = io;
    }
    
    public int getSd(){
    	return this._sd;
    }
    
    public void setSd(int sd){
    	this._sd = sd;
    }
    
    public int getNlt(){
    	return this._nlt;
    }
    
    public void setNlt(int nlt){
    	this._nlt = nlt;
    }
    
    public int getS2w(){
    	return this._s2w;
    }
    
    public void setS2w(int s2w){
    	this._s2w = s2w;
    }
*/
}
