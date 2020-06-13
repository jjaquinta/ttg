package jo.util.utils.io;

import java.util.ArrayList;
import java.util.List;

public class ChainedOutputDevice implements OutputDevice 
{
	private List<OutputDevice>	mChain;
	
	public ChainedOutputDevice()
	{
		mChain = new ArrayList<OutputDevice>();		
	}
	
	public ChainedOutputDevice(OutputDevice od)
	{
		this();
		add(od);
	}
	
	public void add(OutputDevice od)
	{
		mChain.add(od);
	}
	
	public void remove(OutputDevice od)
	{
		mChain.remove(od);
	}
	
	public void print(String msg) {
		for (OutputDevice od : mChain)
			od.print(msg);
	}

	public void println(String msg) {
		for (OutputDevice od : mChain)
			od.println(msg);
	}

	public void println() {
		for (OutputDevice od : mChain)
			od.println();
	}

}
