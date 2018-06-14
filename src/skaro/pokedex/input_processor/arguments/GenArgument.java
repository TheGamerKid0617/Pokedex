package skaro.pokedex.input_processor.arguments;

import skaro.pokedex.data_processor.TextFormatter;

public class GenArgument extends AbstractArgument 
{
	public GenArgument()
	{
		
	}
	
	public void setUp(String argument) 
	{
		//Set up argument
		this.dbForm = TextFormatter.dbFormat(argument).replaceAll("[^0-9]", "");
		this.cat = ArgumentCategory.GEN;
		this.rawInput = argument;
		
		//Check if resource is recognized. If it is not recognized, attempt to spell check it.
		//If it is still not recognized, then return the argument as invalid (default)
		if(!isGen(this.dbForm))
		{
			this.valid = false;
			return;
		}
		
		this.valid = true;
		this.flexForm = this.dbForm;
	}
	
	public boolean isGen(String s)
	{
		if(s.length() != 1)
			return false;
		
        char c = s.charAt(0);
        if (c < '1' || c > '7') 
        	return false;
        
        return true;
	}
}