package com.ashuo.scms.controller;

import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Message;
import com.ashuo.scms.service.AthleteService;
import com.ashuo.scms.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Api(tags = "审核接口")
@RestController
@Slf4j
@RequestMapping("/athlete")
public class shenheController {

    @Autowired
    AthleteService athleteService;
    @Autowired
    MessageService messageService;
    @ApiOperation("审核通过")
    @PutMapping("/editAthlete1")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse editAthlete1(@RequestBody Athlete athlete) {
        if(athlete.getShenhe()!=2)
        {
            return ServerResponse.createByErrorCodeMessage(400, "审核失败，已审核");
        }
        athlete.setShenheTime(LocalDateTime.now());
        athlete.setShenhe(1);
        int effNum = 0;
        try {
            effNum = athleteService.modifyShenhe(athlete);
            Message message = new Message();
            message.setUserId(athlete.getUId());
            message.setMessage("您的信息已审核通过");
            message.setCreateTime(LocalDateTime.now());
            message.setIsRead(0);

            messageService.save(message);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }
    @ApiOperation("审核不通过")
    @PutMapping("/editAthlete2")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse editAthlete2(@RequestBody Athlete athlete) {
        if(athlete.getShenhe()!=2)
        {
            return ServerResponse.createByErrorCodeMessage(400, "审核失败，已审核");
        }
        athlete.setShenheTime(LocalDateTime.now());
        athlete.setShenhe(0);
        int effNum = 0;
        try {
            effNum = athleteService.modifyShenhe(athlete);
            Message message = new Message();
            message.setUserId(athlete.getUId());
            message.setMessage("您的信息已审核通过");
            message.setCreateTime(LocalDateTime.now());
            message.setIsRead(0);
            messageService.save(message);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }
}
