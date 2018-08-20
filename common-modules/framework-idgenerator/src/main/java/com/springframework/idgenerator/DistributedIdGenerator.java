package com.springframework.idgenerator;

/**
 * eq:
 * DistributedIdGenerator idGenerator = new SnowflakeIdGenerator(redisTemplate, IdTypeEnum.PAY_CENTER);
 * Long id = idGenerator.nextId();
 *
 * @author summer
 * 2018/8/20
 */
public interface DistributedIdGenerator {
}
