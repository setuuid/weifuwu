package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service-pb")
public interface PartyDueFeign {

    @GetMapping(value = "/partyDue/generate")
    public BaseVo generatePartyDueMonthly();

    @GetMapping(value = "/monthlyPartyDue/subOrganization/generate")
    public BaseVo generateMonthlyPartyDueForSubordinates();
}
