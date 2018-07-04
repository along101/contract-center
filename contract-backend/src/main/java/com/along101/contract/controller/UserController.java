package com.along101.contract.controller;

import com.along101.atlas.api.UserControllerApiClient;
import com.along101.atlas.model.UserDto;
import com.along101.contract.controller.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用于对user 的各种查询辅助操作
 */
@Slf4j
@RestController
@RequestMapping("/web/users")
public class UserController {

    @Autowired
    private UserControllerApiClient userControllerApiClient;

    //模糊匹配
    @RequestMapping(value = "/fuzzy/", method = RequestMethod.GET)
    public Response<List<UserDto>> fuzzySearchUsersByUserName(@RequestParam("userName") String userName) {
        List<UserDto> userDtos = userControllerApiClient.fuzzySearchUsersByUserNameUsingGET(userName).getBody().getDetail();
        return Response.success(userDtos);
    }

}
