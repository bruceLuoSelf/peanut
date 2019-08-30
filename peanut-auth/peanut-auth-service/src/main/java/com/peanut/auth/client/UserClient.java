package com.peanut.auth.client;

import com.peanut.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author ljn
 * @date 2019/8/30.
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
