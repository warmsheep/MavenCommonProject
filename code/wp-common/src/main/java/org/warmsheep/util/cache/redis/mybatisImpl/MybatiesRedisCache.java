package org.warmsheep.util.cache.redis.mybatisImpl;

import org.apache.ibatis.cache.decorators.LoggingCache;
/**
 * ClassName: MybatiesRedisCache <br/>
 * Function:  详细说明 请看  com.gw.common.utils.cache.redis.mybatisImpl.RedisCache <br/>
 */
public class MybatiesRedisCache  extends LoggingCache  {

	public MybatiesRedisCache(String id) {  
        super(new RedisCache(id));  
	}
}
