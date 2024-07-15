package com.ashuo.scms.controller;


import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.QueryInfo;
import com.ashuo.scms.entity.TeamEnter;
import com.ashuo.scms.service.TeamEnterService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Api(tags = "团队报名接口")
@RestController
@Slf4j
@RequestMapping("/teamEnter")
public class TeamEnterController {
    @Autowired
    TeamEnterService teamEnterService;

    @ApiOperation("查询报名团队")
    @GetMapping("/queryTeamEnter")
    @RequiresAuthentication
    public ServerResponse queryEnter(QueryInfo queryInfo, TeamEnter football) {
        football.setEventName(queryInfo.getQuery());
        Page<TeamEnter> page = new Page<>(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<TeamEnter> FootballList = teamEnterService.getAllTeam(page, football);

        return ServerResponse.createBySuccess(FootballList);
    }


    @ApiOperation("添加报名团队")
    @PostMapping("/addTeamEnter")
    @RequiresRoles(value = {"1"})
    public ServerResponse addFootball(@RequestBody TeamEnter teamEnter) {

        if (teamEnter == null || teamEnter.getEventName() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，团体信息为空");
        }
        List<TeamEnter>to=teamEnterService.getTeamByCondition(teamEnter);
        if ( to.size()!=0) {

            return ServerResponse.createByErrorCodeMessage(400, "添加失败，团体名称已存在");
        }
        //设置创建时间
        int effNum = 0;
        try {
            effNum = teamEnterService.addTeam(teamEnter);
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
    @DeleteMapping("/deleteTeamEnter")
    @RequiresRoles(value = {"1"})
    public ServerResponse deleteTeam(Integer id) {
        int effNum = 0;
        try {
            effNum = teamEnterService.removeTeam(id);
        } catch (Exception e) {
            e.printStackTrace();  // 打印异常堆栈跟踪
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @ApiOperation("获取团体信息")
    @GetMapping("/getTeamEnter")
    @RequiresRoles(value = {"1"})
    public ServerResponse getSchedule(TeamEnter footballCondition) {

        List<TeamEnter> football = teamEnterService.getTeamByCondition(footballCondition);
        if (football != null) {
            return ServerResponse.createBySuccess(football);
        }
        return ServerResponse.createByErrorMessage("查询不到该团体信息");
    }

    @ApiOperation("修改团体")
    @PostMapping("/updateTeamEnter")
    @RequiresRoles(value = {"1"})
    public ServerResponse editTeam(@RequestBody TeamEnter football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，团体信息为空");
        }

        /*if (teamEnterService.getTeamByCondition(football) != null) {
            return ServerResponse.createByErrorCodeMessage(100, "修改失败，团体名称已存在");
        }*/

        int effNum = 0;
        try {
            effNum = teamEnterService.modifyTeam(football);
        } catch (Exception e) {
            e.printStackTrace();  // 打印异常堆栈跟踪
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    @ApiOperation("团体分组")
    @PostMapping("/groupTeamEnter")
    @RequiresRoles(value = {"1"})
    public ServerResponse groupTeam(@RequestBody TeamEnter football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "分组失败，比赛信息为空");
        }

        List<TeamEnter> teamList = teamEnterService.getTeamByCondition(football);
        if (teamList == null || teamList.size() != 32) {

            return ServerResponse.createByErrorMessage("比赛队数不足");
        }

        // 随机打乱队伍列表
        Collections.shuffle(teamList, new Random());

        // 分配组号，每组4个队
        int totalGroups = 8; // 总组数
        int i = 0;
        for (TeamEnter team : teamList) {
            int k=(i / 4) + 1;
            team.setGroupName(k);
            i++;
            teamEnterService.modifyTeam(team);
        }

        // 更新数据库
        return ServerResponse.createBySuccessMessage("分组成功");
    }

}

