package com.springframework.user.api;

import com.springframework.domain.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author summer
 * 2018/8/2
 */
@RequestMapping("/userservice")
public interface UserService {

    @PostMapping(value = "/create")
    Long create(@RequestBody UserDTO dto);

    @PostMapping(value = "/update")
    Integer update(@RequestBody UserDTO dto);

    @PostMapping(value = "/list")
    UserDTO list(@RequestBody UserDTO dto,
                 @RequestParam(value = "pageindex") int pageIndex,
                 @RequestParam(value = "pagesize") int pageSize);

    @GetMapping("/deletebyprimarykey")
    Integer deleteByPrimaryKey(@RequestParam(value = "id") Long id);

    @PostMapping("/batchdeletebyprimarykey")
    Integer batchDeleteByPrimaryKey(@RequestBody List<Long> ids);

}
