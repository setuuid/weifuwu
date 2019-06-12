package com.bit.job.handler;

import com.bit.job.feign.PartyDueFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Date ： 2019/1/1 16:09
 */
@Component
@JobHandler(value = "monthlyPartyDueJobHandler")
public class MonthlyPartyDueJobHandler extends IJobHandler {
    @Autowired
    private PartyDueFeign partyDueFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("生层单位党费档案");
        partyDueFeign.generateMonthlyPartyDueForSubordinates();
        return ReturnT.SUCCESS;
    }
}
