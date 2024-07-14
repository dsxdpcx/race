package com.ashuo.scms.controller;

import com.ashuo.scms.entity.Player;
import com.ashuo.scms.entity.TeamEnter;
import com.ashuo.scms.entity.Node;
import com.ashuo.scms.service.TeamEnterService;
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
import java.util.Comparator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
@Api(tags = "足球赛程接口")
@RestController
@Slf4j
@RequestMapping("/racefootball")
public class FootballController {
    @Autowired
    FootballService footballService;
    @Autowired
    TeamEnterService teamEnterService;

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
    @PostMapping("/updateSchedule")
    @RequiresRoles(value = {"1"})
    public ServerResponse editTeam(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，团体信息为空");
        }

        if (footballService.getFootballByCondition(football) != null) {
            return ServerResponse.createByErrorCodeMessage(100, "修改失败，团体名称已存在");
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

    @ApiOperation("生成小组赛")
    @PostMapping("/groupRace")
    //@RequiresRoles(value = {"1"})
    public ServerResponse addracetable(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }
        if (footballService.getFootballByCondition(football) != null ) {
            return ServerResponse.createBySuccessMessage("不可重复生成");
        }
        TeamEnter teamEnter = new TeamEnter();
        teamEnter.setEventName(football.getName());


        List<TeamEnter> teamList=teamEnterService.getTeamByCondition(teamEnter);
        if (teamList == null || teamList.size() != 32) {
            return ServerResponse.createByErrorMessage("比赛队数不足");
        }

        // 1. 根据 groupName 进行排序
        Collections.sort(teamList, Comparator.comparing(TeamEnter::getGroupName));

        // 2. 获取比赛开始时间
        LocalDateTime time = football.getTime();
        int sumtime=1;

        // 3. 循环处理每个 TeamEnter，为其设置时间和地点
        footballService.queryWinnerName(football);
        for (int k=0;k<8;k++)
        {
            for (int i = k*4; i <k*4+3; i++) {
                TeamEnter teamA = teamList.get(i);
                for(int j=i+1;j<=k*4+3;j++)
                {
                    TeamEnter teamB = teamList.get(j);
                    LocalDateTime time1 = time.plusDays(sumtime);
                    Football football1 = new Football();
                    football1.setTeam_a(teamA.getTeamName());
                    football1.setTeam_b(teamB.getTeamName());
                    football1.setName(football.getName());
                    football1.setMatch_class("小组赛");
                    football1.setReferee("裁判C");
                    football1.setTime(time1);
                    football1.setLocation("第一运动场");
                    footballService.addFootball(football1);

                    Football football2 = new Football();
                    LocalDateTime time2 = time.plusDays(sumtime+1);

                    football2.setTeam_a(teamB.getTeamName());
                    football2.setTeam_b(teamA.getTeamName());
                    football2.setName(football.getName());
                    football2.setMatch_class("小组赛");
                    football2.setReferee("裁判A");
                    football2.setLocation("第二运动场");
                    football2.setTime(time2);
                    footballService.addFootball(football2);
                    sumtime++;
                }
            }
        }
        return ServerResponse.createBySuccessMessage("小组赛生成完毕");
    }
    @ApiOperation("生成八分之一决赛")
    @PostMapping("/produceRace1")
    //@RequiresRoles(value = {"1"})
    //传入的football只需要有赛程名称，赛程类型(八分之一决赛)和比赛开始时间，
    public ServerResponse produceracetable1(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }
        if (footballService.getFootballByCondition(football) != null ) {
            return ServerResponse.createBySuccessMessage("不可重复生成");
        }
        Football match = new Football();
        match.setName(football.getName());
        match.setMatch_class("小组赛");
        List<Football>matches=footballService.queryWinnerName(match);
        for(Football to:matches) {
            if(to.getWinner()==null)
            {
                return ServerResponse.createBySuccessMessage("小组赛未完赛，不可生成八分之一决赛");
            }
        }
        TeamEnter teamEnter = new TeamEnter();
        teamEnter.setEventName(football.getName());
        List<TeamEnter> teamList=teamEnterService.getTeamByCondition(teamEnter);

        if (teamList == null || teamList.size() != 32) {
            return ServerResponse.createByErrorMessage("比赛队数不足");
        }

        // 1. 根据 groupName 进行排序
        Collections.sort(teamList, Comparator.comparing(TeamEnter::getGroupName));

        // 2. 获取比赛开始时间
        LocalDateTime time = football.getTime();
        int sumtime=1;

        // 3. 循环处理每个 TeamEnter，为其设置时间和地点

        for (int k=0;k<8;k++)
        {
                int i = k*4;
                TeamEnter teamA = teamList.get(i);
                TeamEnter teamB = teamList.get(i+1);
                TeamEnter teamC = teamList.get(i+2);
                TeamEnter teamD = teamList.get(i+3);
                Football football1 = new Football();
                football1.setName(football.getName());
                football1.setMatch_class("小组赛");
                football1.setWinner(teamA.getTeamName());
                List<Football>list1=footballService.queryWinnerName(football1);
                int AScore=list1.size()*3;
                Football football2 = new Football();
                football2.setWinner(teamB.getTeamName());
                football2.setName(football.getName());
                football2.setMatch_class("小组赛");
                List<Football>list2=footballService.queryWinnerName(football1);
                int BScore=list2.size()*3;
                Football football3 = new Football();
                football3.setWinner(teamC.getTeamName());
                football3.setName(football.getName());
                football3.setMatch_class("小组赛");
                List<Football>list3=footballService.queryWinnerName(football1);
                int CScore=list3.size()*3;
                Football football4 = new Football();
                football4.setWinner(teamD.getTeamName());
                football4.setName(football.getName());
                football4.setMatch_class("小组赛");
                List<Football>list4=footballService.queryWinnerName(football1);
                int DScore=list4.size()*3;
                Map<String, Integer> map = new HashMap<>();
                map.put(teamA.getTeamName(),AScore);
                map.put(teamB.getTeamName(),BScore);
                map.put(teamC.getTeamName(),CScore);
                map.put(teamD.getTeamName(),DScore);
                List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
                // 使用 Comparator 对 List 进行排序
                list.sort(Map.Entry.comparingByValue());
                String firstKey = list.get(0).getKey();
                String secondKey = list.get(1).getKey();

                LocalDateTime time1 = time.plusDays(sumtime);
                Football footballA = new Football();
                footballA.setTeam_a(firstKey);
                footballA.setTeam_b(secondKey);
                footballA.setName(football.getName());
                footballA.setMatch_class("八分之一决赛");
                footballA.setReferee("裁判D");
                footballA.setTime(time1);
                footballA.setLocation("第一运动场");
                footballService.addFootball(footballA);
                sumtime++;
        }
        return ServerResponse.createBySuccessMessage("八分之一决赛生成完毕");
    }

    @ApiOperation("生成四分之一决赛")
    @PostMapping("/produceRace2")
    //@RequiresRoles(value = {"1"})
    //传入的football只需要有赛程名称，赛程类型(四分之一决赛)和比赛开始时间，
    public ServerResponse produceracetable2(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }
        if (footballService.getFootballByCondition(football) != null ) {
            return ServerResponse.createBySuccessMessage("不可重复生成");
        }
        Football match = new Football();
        match.setName(football.getName());
        match.setMatch_class("八分之一决赛");
        List<Football>matches=footballService.queryWinnerName(match);
        for(Football to:matches) {
            if(to.getWinner()==null)
            {
                return ServerResponse.createBySuccessMessage("八分之一决赛未完赛，不可生成四分之一决赛");
            }
        }
        Football football1=new Football();
        football1.setName(football.getName());
        football1.setMatch_class("八分之一决赛");
        List<Football>list1=footballService.queryWinnerName(football1);
        // 使用 Collections.sort 方法排序
        Collections.sort(list1, new Comparator<Football>() {
            @Override
            public int compare(Football f1, Football f2) {
                // 按时间升序排序
                return f1.getTime().compareTo(f2.getTime());
            }
        });
        LocalDateTime time=football.getTime();
        int sumtime=1;
       for(int i=0;i<list1.size();i+=2){
           Football footballa=list1.get(i);
           Football footballb=list1.get(i+1);
           Football footballrace=new Football();
           LocalDateTime time1 = time.plusDays(sumtime);
           sumtime++;
           footballrace.setTeam_a(footballa.getWinner());
           footballrace.setTeam_b(footballb.getWinner());
           footballrace.setTime(time1);
           footballrace.setName("狮子山杯");
           footballrace.setReferee("裁判E");
           footballrace.setMatch_class("四分之一决赛");
           footballrace.setLocation("第二运动场");
           footballService.addFootball(footballrace);
       }

        return ServerResponse.createBySuccessMessage("四分之一决赛生成完毕");
    }
    @ApiOperation("生成半决赛")
    @PostMapping("/produceRace3")
    //@RequiresRoles(value = {"1"})
    //传入的football只需要有赛程名称(狮子山杯)，赛程类型(半决赛)和比赛开始时间，
    public ServerResponse produceracetable3(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }
        if (footballService.getFootballByCondition(football) != null ) {
            return ServerResponse.createBySuccessMessage("不可重复生成");
        }
        Football match = new Football();
        match.setName(football.getName());
        match.setMatch_class("四分之一决赛");
        List<Football>matches=footballService.queryWinnerName(match);
        for(Football to:matches) {
            if(to.getWinner()==null)
            {
                return ServerResponse.createBySuccessMessage("四分之一决赛未完赛，不可生成半决赛");
            }
        }
        Football football1=new Football();
        football1.setName(football.getName());
        football1.setMatch_class("四分之一决赛");
        List<Football>list1=footballService.queryWinnerName(football1);
        // 使用 Collections.sort 方法排序
        Collections.sort(list1, new Comparator<Football>() {
            @Override
            public int compare(Football f1, Football f2) {
                // 按时间升序排序
                return f1.getTime().compareTo(f2.getTime());
            }
        });
        LocalDateTime time=football.getTime();
        int sumtime=1;
        for(int i=0;i<list1.size();i+=2){
            Football footballa=list1.get(i);
            Football footballb=list1.get(i+1);
            Football footballrace=new Football();
            LocalDateTime time1 = time.plusDays(sumtime);
            sumtime++;
            footballrace.setTime(time1);
            footballrace.setTeam_a(footballa.getWinner());
            footballrace.setTeam_b(footballb.getWinner());
            footballrace.setName("狮子山杯");
            footballrace.setReferee("裁判F");
            footballrace.setMatch_class("半决赛");
            footballrace.setLocation("第二运动场");
            footballService.addFootball(footballrace);
        }
        return ServerResponse.createBySuccessMessage("半决赛生成完毕");
    }

    @ApiOperation("生成决赛")
    @PostMapping("/produceRace4")
    //@RequiresRoles(value = {"1"})
    //传入的football只需要有赛程名称(狮子山杯)，赛程类型(决赛)和比赛开始时间，
    public ServerResponse produceracetable4(@RequestBody Football football) {
        if (football == null ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }
        if (footballService.getFootballByCondition(football) != null ) {
            return ServerResponse.createBySuccessMessage("不可重复生成");
        }
        Football match = new Football();
        match.setName(football.getName());
        match.setMatch_class("半决赛");
        List<Football>matches=footballService.queryWinnerName(match);
        for(Football to:matches) {
            if(to.getWinner()==null)
            {
                return ServerResponse.createBySuccessMessage("半赛未完赛，不可生成决赛");
            }
        }
        Football football1=new Football();
        football1.setName(football.getName());
        football1.setMatch_class("半决赛");
        List<Football>list1=footballService.queryWinnerName(football1);
        LocalDateTime time=football.getTime();

        Football footballa=list1.get(0);
        Football footballb=list1.get(1);
        Football footballrace=new Football();
        LocalDateTime time1 = time.plusDays(1);
        footballrace.setTime(time1);
        footballrace.setTeam_a(footballa.getWinner());
        footballrace.setTeam_b(footballb.getWinner());
        footballrace.setName("狮子山杯");
        footballrace.setMatch_class("决赛");
        footballrace.setReferee("裁判G");
        footballrace.setLocation("第一运动场");
        footballService.addFootball(footballrace);

        return ServerResponse.createBySuccessMessage("决赛生成完毕");
    }

    @ApiOperation("生成赛程表")
    @PostMapping("/showRaceTable")
    //@RequiresRoles(value = {"1"})
    //传入的football只需要有赛程名称(如狮子山杯)
    public ServerResponse showRaceTable(@RequestBody Football football) {
        if (football.getName()=="" ) {
            return ServerResponse.createByErrorCodeMessage(400, "生成失败，比赛信息为空");
        }

        Football football1=new Football();
        football1.setName(football.getName());
        List<Football> list1=footballService.queryWinnerName(football1);
        if(list1.size()==0){
            return ServerResponse.createByErrorCodeMessage(400, "该比赛不存在");
        }
        // 使用 Collections.sort 方法排序
        Collections.sort(list1, new Comparator<Football>() {
            @Override
            public int compare(Football f1, Football f2) {
                // 按时间升序排序
                return f1.getTime().compareTo(f2.getTime());
            }
        });
        // 删除前96条数据
        if (list1.size() > 96) {
            list1.subList(0, 96).clear();
        } else {
            list1.clear(); // 如果列表长度小于等于96，清空整个列表
        }

        for(int i=list1.size();i<15;i++){
            Football newMatch=new Football();
            newMatch.setName(football.getName());
            list1.add(newMatch);
        }
        Collections.reverse(list1);

        Node ans=FootballController.convertToNestedData(list1,1);

        return ServerResponse.createBySuccess(ans);
    }


    public static Node convertToNestedData(List<Football> footballList, int index) {
        if (index > footballList.size()) {
            return null;
        }

        Football match = footballList.get(index-1);
        Node data = new Node();
        data.setId(index);
        Player player1 = new Player();
        Player player2 = new Player();
        if(match.getA_score()>match.getB_score())
        {
            player1.setName(match.getTeam_a());
            player2.setName(match.getTeam_b());
            player1.setScore(match.getA_score());
            player2.setScore(match.getB_score());
            data.setPlayer1(player1);
            data.setPlayer2(player2);
        }
        else{
            player1.setName(match.getTeam_b());
            player2.setName(match.getTeam_a());
            player1.setScore(match.getB_score());
            player2.setScore(match.getA_score());
            data.setPlayer1(player1);
            data.setPlayer2(player2);
        }
        List<Node> children = new ArrayList<>();
        Node child1 = convertToNestedData(footballList, 2 * index);
        if (child1 != null) {
            children.add(child1);
        }
        Node child2 = convertToNestedData(footballList, 2 * index + 1);
        if (child2 != null) {
            children.add(child2);
        }
        if (!children.isEmpty()) {
            data.setChildren(children);
        }
        return data;
    }
}

