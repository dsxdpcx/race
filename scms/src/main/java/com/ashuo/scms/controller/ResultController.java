package com.ashuo.scms.controller;

import com.ashuo.scms.common.consant.Consant;
import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.*;
import com.ashuo.scms.service.ResultService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(tags = "成绩接口")
@RestController
@Slf4j
@RequestMapping("/result")
public class ResultController {


//    @Autowired
//    private ResultService resultService;

//    @ApiOperation("判断晋级")
//    @PostMapping("/judgePromotion")
////    @RequiresRoles(value = {"1"})
//    @Transactional
//    public ServerResponse judgePromotion(@RequestBody Item item) {
//
//        if (item == null) {
//            return ServerResponse.createByErrorCodeMessage(400, "判断失败，项目信息为空");
//        }
//
//        //获取晋级规则
//        Map<String, Object> promotionRule = item.getPromotionRule();
//        if (promotionRule == null) {
//            return ServerResponse.createByErrorCodeMessage(400, "判断失败，晋级规则为空");
//        }
//
//        //获取晋级人数
//        int nPerGroup = (int) promotionRule.get("nPerGroup");
//        //获取额外晋级人数
//        int extraSpots = (int) promotionRule.get("extraSpots");
//
//        //获取项目id
//        int itemId = item.getItemId();
//
//        //获取项目成绩
//        List<Result> results = resultService.getResultsByItemId(itemId);
//
//        //获取晋级人员
//        List<Athlete> advancedAthletes = resultService.getAdvancedAthletes(results, nPerGroup, extraSpots);
//
//        //更新晋级人员
//        List<Athlete> promotedAthletes = new ArrayList<>();
//        for (Athlete athlete : advancedAthletes) {
//            athlete.setPromoted(true);
//            promotedAthletes.add(athlete);
//        }
//
//        //更新晋级人员
//        resultService.updatePromotion(promotedAthletes);
//
//        return ServerResponse.createBySuccess("晋级成功");
//    }
}
