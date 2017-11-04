package skaro.pokedex.data_processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TypeTracker {
	
	private static double[][] effectiveness = new double[/*attacker*/][/*defender*/]{
		  //  			   0    1    2    3    4    5    6    7    8    9    10   11   12   13   14   15   16   17
		  /*0Normal*/  	{ 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 0.0, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
		  /*1Fighting*/ { 2.0, 1.0, 0.5, 0.5, 1.0, 2.0, 0.5, 0.0, 2.0, 1.0, 1.0, 1.0, 1.0, 0.5, 2.0, 1.0, 2.0, 0.5 },
		  /*2Flying*/  	{ 1.0, 2.0, 1.0, 1.0, 1.0, 0.5, 2.0, 1.0, 0.5, 1.0, 1.0, 2.0, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0 },
		  /*3Poison*/  	{ 1.0, 1.0, 1.0, 0.5, 0.5, 0.5, 1.0, 0.5, 0.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0 },
		  /*4Ground*/  	{ 1.0, 1.0, 0.0, 2.0, 1.0, 2.0, 0.5, 1.0, 2.0, 2.0, 1.0, 0.5, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0 },
		  /*5Rock*/  	{ 1.0, 0.5, 2.0, 1.0, 0.5, 1.0, 2.0, 1.0, 0.5, 2.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0 },
		  /*6Bug*/  	{ 1.0, 0.5, 0.5, 0.5, 1.0, 1.0, 1.0, 0.5, 0.5, 0.5, 1.0, 2.0, 1.0, 2.0, 1.0, 1.0, 2.0, 0.5 },
		  /*7Ghost*/  	{ 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.5, 1.0 },
		  /*8Steel*/  	{ 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.5, 0.5, 0.5, 1.0, 0.5, 1.0, 2.0, 1.0, 1.0, 2.0 },
		  /*9Fire*/  	{ 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 2.0, 1.0, 2.0, 0.5, 0.5, 2.0, 1.0, 1.0, 2.0, 0.5, 1.0, 1.0 },
		  /*10Water*/  	{ 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 1.0, 2.0, 0.5, 0.5, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0 },
		  /*11Grass*/  	{ 1.0, 1.0, 0.5, 0.5, 2.0, 2.0, 0.5, 1.0, 0.5, 0.5, 2.0, 0.5, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0 },
		  /*12Electric*/{ 1.0, 1.0, 2.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 0.5, 0.5, 1.0, 1.0, 0.5, 1.0, 1.0 },
		  /*13Psychic*/ { 1.0, 2.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 0.0, 1.0 },
		  /*14Ice*/     { 1.0, 1.0, 2.0, 1.0, 2.0, 1.0, 1.0, 1.0, 0.5, 0.5, 0.5, 2.0, 1.0, 1.0, 0.5, 2.0, 1.0, 1.0 },
		  /*15Dragon*/  { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 0.0 },
		  /*16Dark*/ 	{ 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 1.0, 1.0, 0.5, 0.5 },
		  /*17Fairy*/  	{ 1.0, 2.0, 1.0, 0.5, 1.0, 1.0, 1.0, 1.0, 0.5, 0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 2.0, 2.0, 1.0 }
		    			};
		
	private static Map<String,Integer> typeMapString = new HashMap<>();
	private static Map<Integer,String> typeMapInt = new HashMap<>();
	
	public TypeTracker()
	{
		initializeMaps();
	}
	
	/**
	 * A function to check defensive type interactions
	 * @param type1: one of two types to check defensively
	 * @param type2: two of two types to check defensively. Can be null
	 * @return An object that wraps all the type interactions
	 */
	public TypeInteractionWrapper onDefense(String type1, String type2)
	{
		if(type1 == null)
			return null;
		
		TypeInteractionWrapper result = new TypeInteractionWrapper();
		
		ArrayList<String> temp = new ArrayList<String>();
		Double[] multiplier = {0.0, 0.25, 0.5, 1.0, 2.0, 4.0};
		type1 = type1.toLowerCase();
		
		if(type2 == null)
		{
			for(int j = 0; j < multiplier.length; j++)
			{
				for(int i = 0; i < 18; i++)
					if(effectiveness[i][typeMapString.get(type1)] == multiplier[j])
						temp.add(typeMapInt.get(i));
			
				result.setInteraction(multiplier[j], temp);
				temp = new ArrayList<String>();
			}
		}
		else
		{
			type2 = type2.toLowerCase();
			for(int j = 0; j < multiplier.length; j++)
			{
				for(int i = 0; i < 18; i++)
					if(effectiveness[i][typeMapString.get(type1)] * effectiveness[i][typeMapString.get(type2)] == multiplier[j])
						temp.add(typeMapInt.get(i));
				
				result.setInteraction(multiplier[j], temp);
				temp = new ArrayList<String>();
			}
			
		}
		
		result.setType1(typeMapInt.get(typeMapString.get(type1))); //Get capitalized name
		if(type2 != null)
			result.setType2(typeMapInt.get(typeMapString.get(type2))); //Get capitalized name
		return result;
	}
	
	/**
	 * A method to check for coverage against other typing. If one of the types is super effective 
	 * against a type then that type is considered to be covered. If all of the inputed types
	 * are not very effective/immune against a type then it is not covered.
	 * @param typeX: a typing to check for type interaction
	 * @return The interaction between these four types and all other types
	 */
	public TypeInteractionWrapper coverage(String type1, String type2, String type3, String type4)
	{
		TypeInteractionWrapper result = new TypeInteractionWrapper();
		String[] types = {type1, type2, type3, type4};
		Set<String> effective = new HashSet<String>();
		Set<String> resist = new HashSet<String>();
		Set<String> neutral = new HashSet<String>();
		Set<String> immune = new HashSet<String>();
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i = 0; i < 4; i++)
		{
			if(types[i] != null)
			{
				types[i] = types[i].toLowerCase();
				result.setType(typeMapInt.get(typeMapString.get(types[i])), i + 1);
				for(int j = 0; j < 18; j++)
				{
					effective.addAll(atk2xEffective(types[i]));
					resist.addAll(atk2xResist(types[i]));
					neutral.addAll(atkNeutral(types[i]));
					immune.addAll(atkImmune(types[i]));
				}
			}
		}
		
		//Add all super effective types
		temp.addAll(effective);
		result.setEx2(temp);
		temp = new ArrayList<String>();
		
		//Add all resistive types
		for (String s : resist) 
		{
		    if(!neutral.contains(s) && !immune.contains(s) && !effective.contains(s))
		    {
		    	temp.add(s);
		    }
		}
		result.setRx2(temp);
		temp = new ArrayList<String>();
		
		//Add all neutral types
		for (String s : neutral) 
		{
		    if(!immune.contains(s) && !effective.contains(s))
		    	temp.add(s);
		}
		result.setN(temp);
		temp = new ArrayList<String>();
		
		//Add all neutral types
		for (String s : immune) 
		{
		    if(!effective.contains(s) && !neutral.contains(s))
		    	temp.add(s);
		}
		result.setImm(temp);
		
		return result;
	}
	
	public boolean isType(String input)
	{
		input = input.toLowerCase();
		if(typeMapString.get(input) != null)
			return true;
		return false;
	}
	
	public ArrayList<String> def2xEffective(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[i][typeMapString.get(type)] == 2.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> atk2xResist(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[typeMapString.get(type)][i] == 0.5)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	
	public ArrayList<String> defImmune(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[i][typeMapString.get(type)] == 0.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> atkImmune(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[typeMapString.get(type)][i] == 0.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> def2xResist(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[i][typeMapString.get(type)] == 0.5)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> atk2xEffective(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[typeMapString.get(type)][i] == 2.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> atkNeutral(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[typeMapString.get(type)][i] == 1.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> defNeutral(String type)
	{
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < 18; i++)
			if(effectiveness[i][typeMapString.get(type)] == 1.0)
				result.add(typeMapInt.get(i));
		
		return result;
	}
	
	public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) 
	{
		ArrayList<String> list = new ArrayList<String>();

        for (int i = 0; i < list1.size(); i++) 
        {
            if(list2.contains(list1.get(i))) 
            {
                list.add(list1.get(i));
            }
        }

        return list;
    }
	
	
	private void initializeMaps()
	{
		typeMapString.put("normal", 0);
		typeMapString.put("fighting", 1);
		typeMapString.put("flying", 2);
		typeMapString.put("poison", 3);
		typeMapString.put("ground", 4);
		typeMapString.put("rock", 5);
		typeMapString.put("bug", 6);
		typeMapString.put("ghost", 7);
		typeMapString.put("steel", 8);
		typeMapString.put("fire", 9);
		typeMapString.put("water", 10);
		typeMapString.put("grass", 11);
		typeMapString.put("electric", 12);
		typeMapString.put("psychic", 13);
		typeMapString.put("ice", 14);
		typeMapString.put("dragon", 15);
		typeMapString.put("dark", 16);
		typeMapString.put("fairy", 17);
		
		typeMapInt.put(0, "Normal");
		typeMapInt.put(1, "Fighting");
		typeMapInt.put(2, "Flying");
		typeMapInt.put(3, "Poison");
		typeMapInt.put(4, "Ground");
		typeMapInt.put(5, "Rock");
		typeMapInt.put(6, "Bug");
		typeMapInt.put(7, "Ghost");
		typeMapInt.put(8, "Steel");
		typeMapInt.put(9, "Fire");
		typeMapInt.put(10, "Water");
		typeMapInt.put(11, "Grass");
		typeMapInt.put(12, "Electric");
		typeMapInt.put(13, "Psychic");
		typeMapInt.put(14, "Ice");
		typeMapInt.put(15, "Dragon");
		typeMapInt.put(16, "Dark");
		typeMapInt.put(17, "Fairy");
	}
		
}