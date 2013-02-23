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

public final class ProfilesEntry
{

	private final String name;
	private final int check;
	/*private final int vt;
	private final int md;
	private final int gpu;
	private final int cbl;
	private final int vs;
	private final int fc;
	private final int cd;
	private final int io;
	private final int sd;
	private final int nlt;
	private final int s2w;*/



	public ProfilesEntry(final String name, 
			final int check/* final int vt,
			final int md, final int gpu,
			final int cbl, final int vs,
			final int fc, final int cd,
			final int io, final int sd,
			final int nlt, final int s2w*/)
	{
		this.name = name;
		this.check = check;
	/*	this.vt = vt;
		this.md = md;
		this.gpu = gpu;
		this.cbl = cbl;
		this.vs = vs;
		this.fc = fc;
		this.cd = cd;
		this.io = io;
		this.sd = sd;
		this.nlt = nlt;
		this.s2w = s2w;*/
		

	}


	public String getName()
	{
		return name;
	}


	public int getCheck(){
		return check;
	}
	
/*	public int getVt(){
		return vt;
	}
	
	public int getMd(){
		return md;
	}
	
	public int getGpu(){
		return gpu;
	}
	
	public int getCbl(){
		return cbl;
	}
	
	public int getVs(){
		return vs;
	}
	
	public int getFc(){
		return fc;
	}
	
	public int getCd(){
		return cd;
	}
	
	
	public int getIo(){
		return io;
	}
	
	public int getSd(){
		return sd;
	}
	
	public int getNlt(){
		return nlt;
	}
	
	public int getS2w(){
		return s2w;
	}
	
	*/

}
