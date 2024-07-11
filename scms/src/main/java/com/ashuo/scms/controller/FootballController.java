package com.ashuo.scms.controller;


import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.QueryInfo;
import com.ashuo.scms.entity.Football;
import com.ashuo.scms.service.FootballService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "团队接口")
@RestController
@Slf4j
@RequestMapping("/racefootball")
public class FootballController {
    @Autowired
    FootballService footballService;

    @ApiOperation("查询团体")
    @GetMapping("/queryrace")
    @RequiresAuthentication
    public ServerResponse queryFootball(QueryInfo queryInfo, Football football) {
        football.setName(queryInfo.getQuery());
        Page<Football> page = new Page<>(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<Football> FootballList = footballService.getAllFootball(page, football);
        List<Football> records = FootballList.getRecords();

        System.out.println("helloworld");
        System.out.println(records);
        System.out.println("helloworld");
        return ServerResponse.createBySuccess(FootballList);
    }


    @ApiOperation("添加团体")
    @PostMapping("/addrace")
    @RequiresRoles(value = {"1"})
    public ServerResponse addFootball(@RequestBody Football football) {

        if (football == null || football.getName() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，团体信息为空");
        }

        if (footballService.getFootballByCondition(football) != null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，团体名称已存在");
        }
        //设置创建时间
        int effNum = 0;
        try {
            effNum = footballService.addFootball(football);
        } catch (Exception e) {
            e.printStackTrace();  // 打印异常堆栈跟踪
            System.out.println("Hello World!");
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @ApiOperation("删除团体")
    @DeleteMapping("/deleterace")
    @RequiresRoles(value = {"1"})
    public ServerResponse deleteTeam(Integer schedule_id) {
        int effNum = 0;
        try {
            effNum = footballService.removeFootball(schedule_id);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @ApiOperation("获取团体信息")
    @GetMapping("/getSchedule")
    @RequiresRoles(value = {"1"})
    public ServerResponse getSchedule(Football footballCondition) {

        Football football = footballService.getFootballByCondition(footballCondition);
        if (football != null) {
            return ServerResponse.createBySuccess(football);
        }
        return ServerResponse.createByErrorMessage("查询不到该团体信息");
    }

    @ApiOperation("修改团体")
    @PutMapping("/updateSchedule")
    @RequiresRoles(value = {"1"})
    public ServerResponse editTeam(@RequestBody Football football) {
        if (football == null || football.getName() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，团体信息为空");
        }

        if (footballService.getFootballByCondition(football) != null) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，团体名称已存在");
        }


        int effNum = 0;
        try {
            effNum = footballService.modifyFootball(football);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }
}

