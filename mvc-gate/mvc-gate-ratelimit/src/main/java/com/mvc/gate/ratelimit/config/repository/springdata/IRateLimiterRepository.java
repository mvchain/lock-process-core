package com.mvc.gate.ratelimit.config.repository.springdata;

import com.mvc.gate.ratelimit.config.Rate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author qyc
 */
@Repository
public interface IRateLimiterRepository extends CrudRepository<Rate, String> {

}
