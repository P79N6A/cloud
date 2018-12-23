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

package com.springframework.common.datasource.actuator;

import org.apache.ibatis.executor.Executor;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;

/**
 * @author summer
 */

public class DataSourceHealthIndicator extends AbstractHealthIndicator {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceHealthIndicator.class);
    @Autowired(required = false)
    private Executor target;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        DataSource dataSource = this.getDataSource();
        try {
            if (dataSource == null) {
                builder.withDetail("dataSource", "dataSource is null")
                        .status(Status.DOWN)
                        .build();
            } else {
                builder.status(Status.UP)
                        .withDetail("dataSource", dataSource.toString())
                        .build();
            }
        } catch (Throwable e) {
            builder.status(Status.UNKNOWN)
                    .withException(e)
                    .build();
        }

    }

    private DataSource getDataSource() {
        if(this.target==null){
            return null;
        }
        org.apache.ibatis.transaction.Transaction transaction = this.target.getTransaction();
        if (transaction == null) {
            logger.error(String.format("Could not find transaction on target [%s]", this.target));
            return null;
        } else if (transaction instanceof SpringManagedTransaction) {
            String fieldName = "dataSource";
            Field field = ReflectionUtils.findField(transaction.getClass(), fieldName, DataSource.class);
            if (field == null) {
                logger.error(String.format("Could not find field [%s] of type [%s] on target [%s]", fieldName, DataSource.class, this.target));
                return null;
            } else {
                ReflectionUtils.makeAccessible(field);
                return (DataSource) ReflectionUtils.getField(field, transaction);
            }
        } else {
            logger.error(String.format("---the transaction is not SpringManagedTransaction:%s", transaction.getClass().toString()));
            return null;
        }
    }

}
