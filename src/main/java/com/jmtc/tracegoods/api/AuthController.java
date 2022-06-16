package com.jmtc.tracegoods.api;

import com.jmtc.tracegoods.comm.oap.LoggerManage;
import com.jmtc.tracegoods.domain.User;
import com.jmtc.tracegoods.domain.result.NotificationMsg;
import com.jmtc.tracegoods.domain.result.RespEntity;
import com.jmtc.tracegoods.services.UserService;
import com.jmtc.tracegoods.utils.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Chris
 * @date 2021/6/8 11:47
 * @Email:gang.wu@nexgaming.com
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @LoggerManage(description="授权登录")
    @ApiOperation(value="auth", notes="auth")
    @CrossOrigin
    public RespEntity<Map<String, Object>> login(@RequestBody User user, HttpServletRequest request) {
        if (StringUtils.isEmpty(user.getName())) {
            return error(NotificationMsg.USER_NAME_EMPTY);
        }

        if (StringUtils.isEmpty(user.getPassword())) {
            return error(NotificationMsg.USER_PASSWORD_EMPTY);
        }

        User user1 = userService.findUserById("1");


        Map<String, Object> map = Maps.create()
                .add("token", user1.getToken())
                .get();
        return success(map);
    }
}
