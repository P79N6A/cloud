/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.springframework.cloud.stream.binder.rocket.actuator;

import com.springframework.cloud.stream.binder.rocket.metrics.InstrumentationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

import static com.springframework.cloud.stream.binder.rocket.RocketMQBinderConstants.ENDPOINT_ID;


/**
 * @author Timur Valiev
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@Endpoint(id = ENDPOINT_ID)
public class RocketMQBinderEndpoint {

	@Autowired(required = false)
	private InstrumentationManager instrumentationManager;

	@ReadOperation
	public Map<String, Object> invoke() {
		Map<String, Object> result = new HashMap<>();
		if (instrumentationManager != null) {
			result.put("metrics",
					instrumentationManager.getMetricRegistry().getMetrics());
			result.put("runtime", instrumentationManager.getRuntime());
		}
		else {
			result.put("warning",
					"please add metrics-core dependency, we use it for metrics");
		}
		return result;
	}

}
