package com.springframework.auth.remote;

import com.springframework.auth.api.ZoneService;
import com.springframework.auth.api.domain.dto.ZoneDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author summer  ZoneService 消费方
 * 2018/11/20
 */
@FeignClient(value = "xxx" /*xxx为ZoneService提供方的服务名（spring.application.name属性值）*/, fallback = ZoneServiceFallback.class)
public interface ZoneServiceClient extends ZoneService {

}

@Component
class ZoneServiceFallback implements ZoneServiceClient {

    @Override
    public Long create(ZoneDTO zoneDTO) {
        throw new RuntimeException("no fallback method");
    }

    @Override
    public Integer update(ZoneDTO dto) {
        return null;
    }

    @Override
    public ZoneDTO list(ZoneDTO zoneDTO, int i, int i1) {
        return new ZoneDTO();
    }

    @Override
    public Integer deleteByPrimaryKey(Long id) {
        return null;
    }

    @Override
    public Integer batchDeleteByPrimaryKey(List<Long> ids) {
        return null;
    }

}
