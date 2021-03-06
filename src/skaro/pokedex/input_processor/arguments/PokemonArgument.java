package skaro.pokedex.input_processor.arguments;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import skaro.pokedex.data_processor.TextUtility;
import skaro.pokedex.input_processor.CommandArgument;
import skaro.pokedex.input_processor.Language;
import skaro.pokedex.input_processor.SpellChecker;

public class PokemonArgument extends CommandArgument
{
	public PokemonArgument()
	{
		
	};
	
	public void setUp(String argument, Language lang)
	{
		//Utility variables
		SpellChecker sc = SpellChecker.getInstance();
		
		//Set up argument
		this.dbForm = TextUtility.dbFormat(argument, lang);
		this.category = ArgumentCategory.POKEMON;
		this.rawInput = argument;
		
		//Check if resource is recognized. If it is not recognized, attempt to spell check it.
		//If it is still not recognized, then return the argument as invalid (default)
		if(!isPokemon(this.dbForm, lang))
		{
			String correction;
			correction = sc.spellCheckPokemon(argument, lang);
			
			if(!isPokemon(correction, lang))
			{
				this.valid = false;
				return;
			}
			
			this.dbForm = correction;
			this.rawInput = correction.intern();
			this.isSpellChecked = true;
		}
		
		this.valid = true;
		this.flexForm = sqlManager.getPokemonFlexForm(dbForm, lang).get();
	}
	
	private boolean isPokemon(String s, Language lang)
	{
		String attribute = lang.getSQLAttribute();
		
		Optional<ResultSet> resultOptional = sqlManager.dbQuery("SELECT "+attribute+" FROM Pokemon WHERE "+attribute+" = '"+s+"';");
		boolean resourceExists = false;
		
		if(resultOptional.isPresent())
		{
			try 
			{ 
				resourceExists = resultOptional.get().next();
				resultOptional.get().close();
			} 
			catch(SQLException e)
			{ return resourceExists; }
		}

		return resourceExists;
	}
}
