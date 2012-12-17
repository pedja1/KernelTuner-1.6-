package rs.pedjaapps.KernelTuner.compatibility;

public final class GovEntry
{

	private final String name;
	private final String value;



	public GovEntry(final String name, final String value)
	{
		this.name = name;
		this.value = value;


	}


	public String getName()
	{
		return name;
	}
	

	public String getValue()
	{
		return value;
	}

}
