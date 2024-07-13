package com.ashuo.scms.controller;

import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Cheer;
import com.ashuo.scms.service.CheerService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "加油稿管理接口")
@RestController
@Slf4j
@RequestMapping("/cheer")
public class CheerController {
    @Autowired
    CheerService cheerService;

    @ApiOperation("添加加油稿")
    @PostMapping("/addcheer")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse addCheer(@RequestBody Cheer cheer) {
        if (cheer == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，加油稿信息为空");
        }
        boolean effCum = false;
        try {
            effCum = cheerService.save(cheer);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        if (!effCum) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @ApiOperation("查询所有加油稿")
    @GetMapping("/querycheer")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<List<Cheer>> findAll() {
        List<Cheer> cheerList = cheerService.list();
        return ServerResponse.createBySuccess(cheerList);
    }

    @ApiOperation("查询加油稿")
    @GetMapping("/queryone")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Cheer> findOne(Integer id) {
        Cheer cheer = cheerService.getById(id);
        return ServerResponse.createBySuccess(cheer);
    }

    @ApiOperation("修改加油稿信息")
    @PutMapping("/updatecheer")
    @Transactional(rollbackFor = Exception.class)
    public String update(@RequestBody Cheer cheer) {
        System.out.println(cheer);
        try {
            cheerService.updateById(cheer);
        } catch (Exception e) {
            return "修改失败!";
        }
        return "200";
    }

    @ApiOperation("删除加油稿")
    @DeleteMapping("/deletecheer")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponse<Object> deleteByIds(Integer id) {
        cheerService.removeById(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }
}
