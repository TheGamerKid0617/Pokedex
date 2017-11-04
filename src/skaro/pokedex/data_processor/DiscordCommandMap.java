package skaro.pokedex.data_processor;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import skaro.pokedex.core.CommandLibrary;

public class DiscordCommandMap 
{
	private CacheManager discordCacheManager;
	private Cache<String, ICommand> discordCommandCache;
	
	public DiscordCommandMap(CommandLibrary lib)
	{
		discordCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("discordCommandCache",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, ICommand.class,
								ResourcePoolsBuilder.heap(50))
						.build())
				.build(true);

		discordCommandCache = discordCacheManager.getCache("discordCommandCache", String.class, ICommand.class);
		
		initializeCache(lib);
	}
	
	public ICommand get(String key)
	{
		return discordCommandCache.get(key);
	}
	
	private void initializeCache(CommandLibrary lib)
	{		
		for(ICommand icmd : lib.getLibrary())
			discordCommandCache.put(icmd.getCommandName(), icmd);
	}
}