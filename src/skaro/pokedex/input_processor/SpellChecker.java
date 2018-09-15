package skaro.pokedex.input_processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;

import skaro.pokedex.core.ResourceManager;

public class SpellChecker 
{
	private Map<Language, SpellDictionary> pokeDict;
	private Map<Language, SpellDictionary> itemDict;
	private Map<Language, SpellDictionary> typeDict;
	private Map<Language, SpellDictionary> abilityDict;
	private Map<Language, SpellDictionary> moveDict;
	private Map<Language, SpellDictionary> versionDict;
	private static SpellChecker instance;
	
	private SpellChecker() throws IOException
	{
		pokeDict = new HashMap<Language, SpellDictionary>(); //new SpellDictionaryHashMap();
		itemDict = new HashMap<Language, SpellDictionary>();
		typeDict = new HashMap<Language, SpellDictionary>();
		abilityDict = new HashMap<Language, SpellDictionary>();
		moveDict = new HashMap<Language, SpellDictionary>();
		versionDict = new HashMap<Language, SpellDictionary>();
		
		populateDict(pokeDict, "pokemon.txt", Language.ENGLISH);
		populateDict(itemDict, "items.txt", Language.ENGLISH);
		populateDict(typeDict, "types.txt", Language.ENGLISH);
		populateDict(abilityDict, "abilities.txt", Language.ENGLISH);
		populateDict(moveDict, "moves.txt", Language.ENGLISH);
		populateDict(versionDict, "versions.txt", Language.ENGLISH);
	}
	
	public static SpellChecker getInstance()
	{
		if(instance == null)
			try { instance = new SpellChecker(); }
			catch(IOException e)
			{
				System.out.println("[SpellChecker] Unable to create instance of SpellChecker");
				System.exit(1);
			}
		
		return instance;
	}
	
	private String getBestSuggestion(SpellDictionary dict, String word)
	{
		List<?> suggestions = dict.getSuggestions(word, 5);
		if(suggestions.isEmpty())	//No suggestions for this word
			return word;
		
		Word best = (Word)dict.getSuggestions(word, 5).get(0);
		return best.getWord();
	}
	
	//Spell checks a Pokemon
	public String spellCheckPokemon(String poke, Language lang)
	{
		poke = poke.toLowerCase();
		String[] temp = null;
		
		if(poke.contains("-"))
		{
			temp = poke.split("-");
			
			//If more than two dashes are used, return null
			if(temp.length > 3)
				return null;
			
			temp[0] = getBestSuggestion(pokeDict.get(lang), temp[0]);
			
			//Check for slang that is often used
			if(temp[1].equalsIgnoreCase("male")) //for nidoran male
				temp[1] = "m";
			else if(temp[1].equalsIgnoreCase("female"))//for nidoran female
				temp[1] = "f";
			else if(temp[1].equalsIgnoreCase("t"))//For genie forms
				temp[1] = "therian";
			//Rotom forms
			else if(temp[1].equalsIgnoreCase("W"))
				temp[1] = "wash";
			else if(temp[1].equalsIgnoreCase("M"))
				temp[1] = "mow";
			else if(temp[1].equalsIgnoreCase("F"))
				temp[1] = "frost";
			else if(temp[1].equalsIgnoreCase("H"))
				temp[1] = "heat";
			else
				temp[1] = getBestSuggestion(pokeDict.get(lang), temp[1]);
			
			//System.out.println(temp[1]);
			
			return temp[0]+"-"+temp[1]+((temp.length == 3) ? "-"+temp[2] : "");
		}
		else if(poke.contains(" "))
		{
			temp = poke.split(" ");
			
			//If more than two spaces are used, return null
			if(temp.length > 3)
				return null;
			
			temp[0] = getBestSuggestion(pokeDict.get(lang), temp[0]);
			
			//Check for slang that is often used
			if(temp[1].equalsIgnoreCase("male")) //for nidoran male
				temp[1] = "m";
			else if(temp[1].equalsIgnoreCase("female"))//for nidoran female
				temp[1] = "f";
			else if(temp[1].equalsIgnoreCase("t"))//For genie forms
				temp[1] = "therian";
			//Rotom forms
			else if(temp[1].equalsIgnoreCase("W"))
				temp[1] = "wash";
			else if(temp[1].equalsIgnoreCase("M"))
				temp[1] = "mow";
			else if(temp[1].equalsIgnoreCase("F"))
				temp[1] = "frost";
			else if(temp[1].equalsIgnoreCase("H"))
				temp[1] = "heat";
			else
				temp[1] = getBestSuggestion(pokeDict.get(lang), temp[1]);
			
			return temp[0]+" "+temp[1]+((temp.length == 3) ? " "+temp[2] : "");
		}
		
		else
			return getBestSuggestion(pokeDict.get(lang), poke);
	}
	
	//Spell checks abilities
	public String spellCheckAbility(String abil, Language lang)
	{
		abil = abil.toLowerCase();
		String[] temp = abil.split(" ");
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < temp.length; i++)
		output.append(getBestSuggestion(abilityDict.get(lang), temp[i]) + " ");
		
		return output.toString().trim();
	}
	
	//Spell checks items
	public String spellCheckItem(String item, Language lang)
	{
		item = item.toLowerCase();
		String[] temp = item.split(" ");
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < temp.length; i++)
			output.append(getBestSuggestion(itemDict.get(lang), temp[i]) + " ");
			
		return output.toString().trim();
	}
	
	//Spell checks moves
	public String spellCheckMove(String move, Language lang)
	{
		move = move.toLowerCase();
		String[] temp = move.split(" ");
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < temp.length; i++)
			output.append(getBestSuggestion(moveDict.get(lang), temp[i]) + " ");
		
		return output.toString().trim();
	}
	
	//Spell checks types
	public String spellCheckType(String type, Language lang)
	{
		type = type.toLowerCase();
		String[] temp = type.split(" ");
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < temp.length; i++)
			output.append(getBestSuggestion(typeDict.get(lang), temp[i]) + " ");
		
		return output.toString().trim();
	}
	
	//Spell checks artist
	public String spellCheckVersion(String ver, Language lang)
	{
		StringBuilder version = new StringBuilder(ver.toLowerCase());
		int index;
		
		//Check for abbreviations
		if((index = version.indexOf("1")) != -1)
			version.replace(index, index + 1, "");
			//version.replace("1", "");
		else if((index = version.indexOf("one")) != -1)
			version.replace(index, index + 3, "");
			//ver.replace("one", "");
		else if((index = version.indexOf("two")) != -1)
			version.replace(index, index + 3, "2");
			//ver.replace("two", "2");
		else if((index = version.indexOf("as")) != -1)
			version.replace(index, index + 2, "alpha sapphire");
			//ver.replace("as", "alpha sapphire");
		else if((index = version.indexOf("or")) != -1)
			version.replace(index, index + 2, "omega ruby");
			//ver.replace("or", "omega ruby");
		else if((index = version.indexOf("ss")) != -1)
			version.replace(index, index + 2, "soul silver");
			//ver.replace("ss", "soul silver");
		else if((index = version.indexOf("hg")) != -1)
			version.replace(index, index + 2, "heart gold");
			//ver.replace("hg", "heart gold");
		else if((index = version.indexOf("fr")) != -1)
			version.replace(index, index + 2, "fire red");
			//ver.replace("fr", "fire red");
		else if((index = version.indexOf("lg")) != -1)
			version.replace(index, index + 2, "leaf green");
			//ver.replace("lg", "leaf green");
		
		String[] temp = version.toString().split(" ");
		StringBuilder output = new StringBuilder();
		for(int i = 0; i < temp.length; i++)
			output.append(getBestSuggestion(versionDict.get(lang), temp[i]) + " ");

		return output.toString().trim();
	}
	
	//Helper method to train spell checkers
	private void populateDict(Map<Language, SpellDictionary> dict, String fileName, Language lang) throws IOException
	{
		InputStreamReader reader;
		BufferedReader bReader;
		String line = null;
		SpellDictionary tempDict = new SpellDictionaryHashMap();
		reader = new InputStreamReader(ResourceManager.getDictionaryResource(fileName, lang));
		bReader = new BufferedReader(reader);
		
		while((line = bReader.readLine()) != null)
			tempDict.addWord(line.toLowerCase());
		
		dict.put(lang, tempDict);
		bReader.close();
	}
}
