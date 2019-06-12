package com.bit.soft.gateway.feign;

import com.bit.soft.gateway.model.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "servicea")
public interface TokenFeign {

    @RequestMapping(value = "/auth/verify/{token}",method = RequestMethod.GET)
    public JsonResult verify(@PathVariable(value="token")String token);

}
