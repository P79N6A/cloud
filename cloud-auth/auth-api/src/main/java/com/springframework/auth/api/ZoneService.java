package com.springframework.auth.api;

import com.springframework.auth.api.domain.dto.ZoneDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author summer
 * 2018/11/20
 */
@RequestMapping("/zoneservice")
public interface ZoneService {

    @PostMapping("/create")
    Long create(@RequestBody ZoneDTO dto);

    @PostMapping("/update")
    Integer update(@RequestBody ZoneDTO dto);

    @PostMapping(value = "/list")
    ZoneDTO list(@RequestBody ZoneDTO dto,
                 @RequestParam(value = "pageindex") int pageIndex,
                 @RequestParam(value = "pagesize") int pageSize);

    @GetMapping("/deletebyprimarykey")
    Integer deleteByPrimaryKey(@RequestParam(value = "id") Long id);

    @PostMapping("/batchdeletebyprimarykey")
    Integer batchDeleteByPrimaryKey(@RequestBody List<Long> ids);

}

