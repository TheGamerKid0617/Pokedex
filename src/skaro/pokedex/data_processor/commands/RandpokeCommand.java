package skaro.pokedex.data_processor.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.jetty.util.MultiMap;

import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import skaro.pokedex.core.IServiceManager;
import skaro.pokedex.core.ServiceConsumerException;
import skaro.pokedex.core.ServiceType;
import skaro.pokedex.data_processor.AbstractCommand;
import skaro.pokedex.data_processor.IDiscordFormatter;
import skaro.pokedex.data_processor.Response;
import skaro.pokedex.input_processor.Input;
import skaro.pokedex.input_processor.Language;
import skaro.pokedex.input_processor.arguments.ArgumentCategory;
import skaro.pokeflex.api.Endpoint;
import skaro.pokeflex.api.IFlexObject;
import skaro.pokeflex.api.PokeFlexFactory;
import skaro.pokeflex.api.PokeFlexRequest;
import skaro.pokeflex.api.Request;
import skaro.pokeflex.objects.pokemon.Pokemon;

public class RandpokeCommand extends AbstractCommand 
{
	public RandpokeCommand(IServiceManager services, IDiscordFormatter formatter) throws ServiceConsumerException
	{
		super(services, formatter);
		if(!hasExpectedServices(this.services))
			throw new ServiceConsumerException("Did not receive all necessary services");
		
		commandName = "randpoke".intern();
		argCats.add(ArgumentCategory.NONE);
		expectedArgRange = new ArgumentRange(0,0);
		aliases.put("rand", Language.ENGLISH);
		aliases.put("randompoke", Language.ENGLISH);
		aliases.put("randompokemon", Language.ENGLISH);
		aliases.put("randpokemon", Language.ENGLISH);
		aliases.put("zufällig", Language.GERMAN);
		aliases.put("zufallig", Language.GERMAN);
		aliases.put("casuale", Language.ITALIAN);
		aliases.put("mujagwiui", Language.KOREAN);
		aliases.put("suíjī", Language.CHINESE_SIMPMLIFIED);
		aliases.put("suiji", Language.CHINESE_SIMPMLIFIED);
		aliases.put("aleatorio", Language.SPANISH);
		aliases.put("randamu", Language.JAPANESE_HIR_KAT);
		aliases.put("hasard", Language.FRENCH);
		
		aliases.put("ランダム", Language.JAPANESE_HIR_KAT);
		aliases.put("随机", Language.CHINESE_SIMPMLIFIED);
		aliases.put("무작위의", Language.KOREAN);
		
		extraMessages.add("See the shiny with the %shiny command! (Patrons only)");
		this.createHelpMessage("https://i.imgur.com/cOEo8jW.gif");
	}
	
	@Override
	public boolean makesWebRequest() { return true; }
	@Override
	public String getArguments() { return "none"; }

	@Override
	public boolean hasExpectedServices(IServiceManager services) 
	{
		return super.hasExpectedServices(services) &&
				services.hasServices(ServiceType.POKE_FLEX, ServiceType.PERK);
	}
	
	@Override
	public Mono<Response> discordReply(Input input, User requester)
	{
		try
		{
			PokeFlexFactory factory = (PokeFlexFactory)services.getService(ServiceType.POKE_FLEX);
			EmbedCreateSpec builder = new EmbedCreateSpec();
			int randDexNum = ThreadLocalRandom.current().nextInt(1, 807 + 1);
			Mono<MultiMap<IFlexObject>> result;
			
			result = Mono.just(new MultiMap<IFlexObject>())
					.flatMap(dataMap -> Flux.fromIterable(createRequests(randDexNum))
						.flatMap(request -> request.makeRequest(factory))
						.doOnNext(flexObject -> dataMap.add(flexObject.getClass().getName(), flexObject))
						.then(Mono.just(dataMap)));
			
			this.addRandomExtraMessage(builder);
			return result.flatMap(dataMap -> Mono.just(dataMap.getValue(Pokemon.class.getName(), 0))
					.ofType(Pokemon.class)
					.flatMap(pokemon -> this.addAdopter(pokemon, builder))
					.map(pokemon -> formatter.format(input, dataMap, builder)));
		}
		catch(Exception e)
		{
			Response response = new Response();
			this.addErrorMessage(response, input, "1002", e); 
			return Mono.just(response);
		}
	}
	
	private List<PokeFlexRequest> createRequests(int pokedexNumber)
	{
		List<PokeFlexRequest> result = new ArrayList<>();
		String pokedexNumberAsString = Integer.toString(pokedexNumber);
		
		result.add(new Request(Endpoint.POKEMON, pokedexNumberAsString));
		result.add(new Request(Endpoint.POKEMON_SPECIES, pokedexNumberAsString));
		
		return result;
	}
}
