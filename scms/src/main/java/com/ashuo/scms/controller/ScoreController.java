package com.ashuo.scms.controller;

import com.ashuo.scms.common.consant.Consant;
import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.dto.AthleteScoreDto;
import com.ashuo.scms.entity.*;
import com.ashuo.scms.entity.Record;
import com.ashuo.scms.service.*;
import com.ashuo.scms.util.ObjectUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "项目分数接口")
@RestController
@Slf4j
@RequestMapping("/score")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private AthleteService athleteService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private WinnersService winnersService;

    @ApiOperation("查询运动员项目分数")
    @GetMapping("/queryScore")
    @RequiresAuthentication
    public ServerResponse queryScore(QueryInfo queryInfo, Score score) {

        if (StringUtils.isBlank(queryInfo.getQuery())) {
            queryInfo.setQuery(null);
        }
        //分页查询
        Page<Score> page = new Page<>(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<Score> scoreList = scoreService.getScoreByScoreCondition(page, score);
        return ServerResponse.createBySuccess(scoreList);
    }


    @ApiOperation("添加分数")
    @PostMapping("/addScore")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse addScore(@RequestBody Score score) {
        if (score == null || score.getAthlete() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，分数信息为空");
        }
        Item item = itemService.getOneItemByCondition(score.getAthlete().getItem());
        if (item != null && item.getSeason() != null && item.getSeason().getSeasonStatus() == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "分数录入失败，该届运动会已结束");
        }
        //分数纪录处理，因为分数需要加上是否破纪录，所以记录在分数之前处理
        scoreRecordHandle(score);

        //设置创建、修改时间
        score.setCreateTime(LocalDateTime.now());
        score.setEditTime(LocalDateTime.now());
        //添加成绩
        scoreService.addScore(score);

        //设置1,表示已记分
        Athlete athlete = score.getAthlete();
        athlete.setScoreStatus(1);
        athleteService.modifyAthlete(athlete);

        if(item.getProcess().equals("finals")){
//        //分数排名处理
        scoreRankingHandle(score);
    }
        return ServerResponse.createBySuccessMessage("添加成功");

    }


    @ApiOperation("获取分数详情")
    @GetMapping("/getScore")
    @RequiresAuthentication
    public ServerResponse getScore(int scoreId) {

        Score score = scoreService.getOneScoreByScoreId(scoreId);
        if (score != null) {
            return ServerResponse.createBySuccess(score);
        }
        return ServerResponse.createByErrorMessage("查询不到该Score信息");
    }


    @ApiOperation("修改分数")
    @PutMapping("/editScore")
    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse editScore(@RequestBody Score score) {
        if (score == null || score.getScore() == null || score.getScore().compareTo(BigDecimal.ZERO) == 0 || score.getAthlete() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，分数信息为空");
        }

        Item item = itemService.getOneItemByCondition(score.getAthlete().getItem());
        if (item != null && item.getSeason() != null && item.getSeason().getSeasonStatus() == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "分数修改失败，该届运动会已结束");
        }
        //分数纪录处理
        scoreRecordHandle(score);
        //设置修改时间
        score.setEditTime(LocalDateTime.now());
        //修改分数
        scoreService.modifyScore(score);
        //分数排名处理
        if(item.getProcess().equals("finals")){
//        //分数排名处理
            scoreRankingHandle(score);
        }
        return ServerResponse.createBySuccessMessage("修改成功");

    }

    //用于返回所有运动员的分数，可用于导出Excel
    @ApiOperation("项目下所有运动员的分数")
    @GetMapping("/queryAthleteScore")
    @RequiresAuthentication
    public ServerResponse queryAthleteScore(QueryInfo queryInfo, Score score) {
        Athlete athlete = ObjectUtils.isNull(score.getAthlete()) ? new Athlete() : score.getAthlete();
        User user = ObjectUtils.isNull(athlete.getUser()) ? new User() : athlete.getUser();

        user.setNickname(queryInfo.getQuery());
        athlete.setUser(user);
        score.setAthlete(athlete);

        //分页查询
        Page<AthleteScoreDto> page = new Page<>(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<AthleteScoreDto> scoreList = scoreService.getAthleteScoreDto(page, score);
        return ServerResponse.createBySuccess(scoreList);
    }

    private void scoreRecordHandle(Score score) {
        //查询该项目成绩是否破纪录
        Record record = new Record();
        //获取该项目的有效记录列表
        record.setRecordStatus(1);
        List<Record> recordList = recordService.getRecordByRecordCondition(new Page<>(Consant.MINCURRENTPAGE, Consant.MAXPAGESIZE), record).getRecords();
        //设置分数数据
        record.setAthlete(score.getAthlete());
        record.setRecordScore(score.getScore());
        //记录生效
        record.setCreateTime(LocalDateTime.now());
        record.setEditTime(LocalDateTime.now());
        //如果该项目存在记录
        if (recordList != null && recordList.size() > 0) {
            //获取该项目之前记录
            Record itemRecord = recordList.get(0);
            //如果分数破纪录或持平记录
            if (score.getScore().compareTo(itemRecord.getRecordScore()) >= 0) {

                //如果分数破纪录，如果和记录持平则直接添加
                if (score.getScore().compareTo(itemRecord.getRecordScore()) == 1) {

                    //使之前记录失效
                    recordList.stream().forEach(r -> {
                        r.setRecordStatus(0);
                        recordService.modifyRecord(r);
                    });

                }
                //保存录入的分数记录
                recordService.addRecord(record);
                score.setIsBreakRecord(1);
            } else {
                //如果没有破纪录或者和记录持平
                score.setIsBreakRecord(0);
            }
        } else {
            recordService.addRecord(record);
            score.setIsBreakRecord(1);
        }
    }

    private void scoreRankingHandle(Score score) {
        //删除原ranking中对应item的排名数据
        rankingService.removeRanking(score.getAthlete().getItem().getItemId());
        System.out.println("删除原ranking中对应item的排名数据");
        //获取分数单位
        score = scoreService.getOneScoreByScoreId(score.getScoreId());

        //判断分数单位，如果单位为秒，则ASC升序排列
        String condition = "DESC";
        if ("秒".equals(score.getAthlete().getItem().getItemUnit())) {
            condition = "ASC";
        }
        List<Score> scoreList = scoreService.getScoreByItemIdLimit(score.getAthlete().getItem().getItemId(), condition);
        //添加到Ranking对象中
        List<Ranking> rankingList = new ArrayList<>();
        System.out.println("获取分数单位");
        Score tempScore = new Score();
        tempScore.setScore(new BigDecimal("-1"));
        System.out.println(scoreList.size());
        //获取前三，并重新计算排名
        int limitAmount = 4;
        int i = 0;
        while (limitAmount > 1) {
            if (scoreList.size() > i) {
                Score s = scoreList.get(i);
                //如果分数不相等
                if (s.getScore().compareTo(tempScore.getScore()) != 0) {
                    limitAmount--;
                }
                Ranking ranking = new Ranking();
                ranking.setAthlete(s.getAthlete());
                //设置排名得分3、2、1
                ranking.setRankk(limitAmount);
                ranking.setPoints(3-limitAmount);
                ranking.setEditTime(LocalDateTime.now());
                rankingList.add(ranking);
                tempScore = s;
                i++;
            } else {
                limitAmount--;
            }
        }
        rankingService.addRanking(rankingList);
    }


    @ApiOperation("获取比赛类别")
    @GetMapping ("/catalog")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse catalog(@RequestParam int itemId) {

            Item item = itemService.getById(itemId);
            if (item == null) {
                return ServerResponse.createByErrorMessage("查询不到该项目信息");
            }
            String catalog = item.getCatalog();
            return ServerResponse.createBySuccess(catalog.length());
    }


    @ApiOperation("半决赛")
    @PostMapping("/semi")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse semi(@RequestParam int itemId, @RequestParam String process) {

        if(!scoreService.qualify(itemId, process)){
            return ServerResponse.createByErrorCodeMessage(400, "比赛未完成，不能生成下一轮比赛");
        }
        Item item = itemService.getById(itemId);
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        String name = item.getItemName();
        queryWrapper.eq("item_name", name);
        queryWrapper.eq("process", "semifinals");
        List<Item> itemList = itemService.list(queryWrapper);
        if (itemList.size() > 0) {
            return ServerResponse.createByErrorCodeMessage(400, "不能重复生成");
        }
        String catalog = item.getCatalog();
        process="heats";
        if(catalog.charAt(1)=='1'){
            scoreService.checkAndPromoteThree(itemId, process);
        }
        else if(catalog.charAt(1)=='2'){
            scoreService.checkAndPromoteTwo(itemId, process);
        }
        else if(catalog.charAt(1)=='3'){
            fieldfinal(itemId, process);
        }else if(catalog.charAt(1)=='4'){
            scoreService.checkAndPromoteTopX(16,itemId, process);
        }else if(catalog.charAt(1)=='5'){
            scoreService.checkAndPromoteTopX(8,itemId, process);
        }
        return ServerResponse.createBySuccessMessage("结束");

    }
    @ApiOperation("决赛")
    @PostMapping("/finals")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse finals(@RequestParam int itemId, @RequestParam String process) {

        if(!scoreService.qualify(itemId, process)){
            return ServerResponse.createByErrorCodeMessage(400, "比赛未完成，不能生成下一轮比赛");
        }

        Item item = itemService.getById(itemId);
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        String name = item.getItemName();
        queryWrapper.eq("item_name", name);
        queryWrapper.eq("process", "finals");
        List<Item> itemList = itemService.list(queryWrapper);
        if (itemList.size() > 0) {
            return ServerResponse.createByErrorCodeMessage(400, "不能重复生成");
        }
        String catalog = item.getCatalog();

        if(catalog.length()==2) {
             process= "heats";
            if (catalog.charAt(1) == '2') {
                scoreService.checkAndPromoteThree(itemId, process);
            } else if (catalog.charAt(1) == '0') {
                scoreService.checkAndPromoteTwo(itemId, process);
            } else if (catalog.charAt(1) == '4') {
                fieldfinal(itemId, process);
            } else if (catalog.charAt(1) == '1') {
                scoreService.checkAndPromoteTopSixteen(itemId, process);
            } else if (catalog.charAt(1) == '3') {
                scoreService.checkAndPromoteTopX(8, itemId, process);
            }
        }
        else if(catalog.length()==3){
            process="semifinals";
            if (catalog.charAt(1) == '2') {
                scoreService.checkAndPromoteThree(itemId, process);
            } else if (catalog.charAt(1) == '0') {
                scoreService.checkAndPromoteTwo(itemId, process);
            } else if (catalog.charAt(1) == '4') {
                fieldfinal(itemId, process);
            } else if (catalog.charAt(1) == '1') {
                scoreService.checkAndPromoteTopSixteen(itemId, process);
            } else if (catalog.charAt(1) == '3') {
                scoreService.checkAndPromoteTopX(8, itemId, process);
            }
        }

        return ServerResponse.createBySuccessMessage("结束");



    }

        @ApiOperation("径赛晋级半决赛")
    @PostMapping("/tracksemi")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse tracksemi(@RequestParam int itemId, @RequestParam String process) {


            Item tempItem = itemService.getById(itemId);
        if(process.equals("heats")&&tempItem.getCatalog().length()==2){
            tempItem.setProcess("finals");
        }
        else if(process.equals("heats")&&tempItem.getCatalog().length()==3){
            tempItem.setProcess("semifinals");
        }
        else if(process.equals("semifinals")){
            tempItem.setProcess("finals");
        }

        if (itemService.getOneItemByCondition(tempItem) != null) {
            return ServerResponse.createByErrorCodeMessage(400, "不能重复生成");
        }
        scoreService.checkAndPromoteThree(itemId, process);

        return ServerResponse.createBySuccessMessage("结束");

    }



    @ApiOperation("径赛晋级决赛")
    @PostMapping("/trackfinal")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse trackfinal(@RequestParam int itemId, @RequestParam String process) {


        scoreService.checkAndPromoteTwo(itemId, process);

        return ServerResponse.createBySuccessMessage("结束");

    }


    @ApiOperation("游泳晋级半决赛")
    @PostMapping("/swimsemi")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse swimsemi(@RequestParam int itemId, @RequestParam String process) {


        scoreService.checkAndPromoteTwo(itemId, process);

        return ServerResponse.createBySuccessMessage("结束");

    }


    @ApiOperation("游泳晋级决赛")
    @PostMapping("/swimfinal")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse swimfinal(@RequestParam int itemId, @RequestParam String process) {

        scoreService.checkAndPromoteTopX(8,itemId, process);

        return ServerResponse.createBySuccessMessage("结束");

    }



    @ApiOperation("田赛晋级决赛")
    @PostMapping("/fieldfinal")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse fieldfinal(@RequestParam int itemId, @RequestParam String process) {


        QueryWrapper <Score> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("score",8.15);
        List<Score> scoreList=scoreService.list(queryWrapper);

        if(scoreList.size()<12){
            scoreService.checkAndPromoteTopX(12,itemId, process);
        }
        else{
            scoreService.checkAndPromoteTopX(scoreList.size(),itemId, process);
        }

        return ServerResponse.createBySuccessMessage("结束");

    }


    @ApiOperation("生成获奖名单")
    @PostMapping("/winners")
//    @RequiresRoles(value = {"1"})
    @Transactional
    public ServerResponse winners(@RequestParam int itemId) {
        scoreService.produceWinners(itemId);
        List<Integer> winnersid=scoreService.getWinners(itemId);
        for(Integer id:winnersid) {
            Winners winners = new Winners();
            winners.setItemId(itemId);
            winners.setUserId(id);
            winnersService.save(winners);
        }
        return ServerResponse.createBySuccessMessage("结束");
    }




}


