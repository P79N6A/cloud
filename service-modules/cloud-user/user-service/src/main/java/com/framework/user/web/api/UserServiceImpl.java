package com.framework.user.web.api;

import com.springframework.domain.dto.UserDTO;
import com.springframework.user.api.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author summer
 * 2018/8/2
 */
@RestController
public class UserServiceImpl implements UserService {


    @Override
    public Long create(@RequestBody UserDTO dto) {
        return 0L;
    }

    @Override
    public Integer update(@RequestBody UserDTO dto) {
        return 0;
    }

    @Override
    public UserDTO list(@RequestBody UserDTO dto,
                        @RequestParam(value = "pageindex") int pageIndex,
                        @RequestParam(value = "pagesize") int pageSize) {
        return null;
    }

    @Override
    public Integer deleteByPrimaryKey(@RequestParam(value = "id") Long id) {
        return 0;
    }

    @Override
    public Integer batchDeleteByPrimaryKey(@RequestBody List<Long> ids) {
        return 0;
    }
}
