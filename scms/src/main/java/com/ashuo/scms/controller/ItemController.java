package com.ashuo.scms.controller;


import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.entity.QueryInfo;
import com.ashuo.scms.entity.Season;
import com.ashuo.scms.service.ItemService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "运动项目接口")
@RestController
@Slf4j
@RequestMapping("/item")
public class ItemController {
    @Autowired
    ItemService itemService;

    @ApiOperation("查询项目")
    @GetMapping("/queryItem")
//    @RequiresAuthentication
    public ServerResponse queryItem(QueryInfo queryInfo, Item item) {
        item.setItemName(queryInfo.getQuery());
        Page<Item> page = new Page(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<Item> itemList = itemService.getItemByItemCondition(page, item);
        return ServerResponse.createBySuccess(itemList);
    }

    @ApiOperation("查询可报名项目")
    @GetMapping("/queryItem2")
    @RequiresAuthentication
    public ServerResponse queryItem2(QueryInfo queryInfo, Item item) {
        item.setItemName(queryInfo.getQuery());
        Page<Item> page = new Page(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        item.setProcess("heats");
        IPage<Item> itemList = itemService.getItemBysignCondition(page, item);


        return ServerResponse.createBySuccess(itemList);
    }
    @ApiOperation("查询多人项目")
    @GetMapping("/queryItem3")
//    @RequiresAuthentication
    public ServerResponse queryItem3(QueryInfo queryInfo, Item item) {
        item.setItemName(queryInfo.getQuery());
        Page<Item> page = new Page(queryInfo.getCurrentPage(), queryInfo.getPageSize());
        IPage<Item> itemList = itemService.getItemByExclude(page, item);
        return ServerResponse.createBySuccess(itemList);
    }


    @ApiOperation("查询当前项目")
    @GetMapping("/querytimeItem")
//    @RequiresAuthentication
    public ServerResponse querytimeItem() {

        LocalDateTime now = LocalDateTime.now();
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("start_time", now);  // 使用gt（"greater than"）来查询之后的日期
        List<Item> itemList = itemService.list(queryWrapper);
        itemList.forEach(item -> {
            // 假设 Item 类有 getSex() 和 getType() 方法来获取性别和类型
            String newName = item.getItemName() + "(" + item.getItemSex() + ")";
            item.setItemName(newName);  // 设置新的 name
        });
        itemList.forEach(item -> {
            String process = item.getProcess();
            if ("finals".equals(process)) {
                item.setItemName(item.getItemName() + " (决赛)");
            } else if ("semifinals".equals(process)) {
                item.setItemName(item.getItemName() + " (半决赛)");
            } else {
                item.setItemName(item.getItemName() + " (预赛)");
            }
        });


        return ServerResponse.createBySuccess(itemList);
    }
    @ApiOperation("添加项目")
    @PostMapping("/addItem")
    @Transactional
//    @RequiresRoles(value = {"1"})
    public ServerResponse addItem(@RequestBody Item item) {

        if (item == null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，项目信息为空");
        }
        if(item.getCatalog()==null){
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，请先选择项目类型");
        }
        if(item.getStartTime()!=null) {
            if (item.getEndTime().isBefore(item.getStartTime())) {
                return ServerResponse.createByErrorCodeMessage(400, "添加失败，结束时间必须晚于开始时间");
            }
        }
        //通过seasonId、parentId和性别判断是否已存在相同Item
        Item tempItem = new Item();
        tempItem.setParentId(item.getParentId());
        tempItem.setItemSex(item.getItemSex());
        tempItem.setProcess(item.getProcess());
        if (item.getSeason() != null && item.getSeason().getSeasonId() != null && item.getSeason().getSeasonId() != 0) {
            Season season = item.getSeason();
            tempItem.setSeason(season);
        }
        if (itemService.getOneItemByCondition(tempItem) != null) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败，该项目已存在");
        }
        System.out.println(item.getItemName());
        Item itemTemplate = itemService.getItemTemplateDetail(item);

        //根据模板设置item名称等信息
        if (itemTemplate != null) {
            item.setItemName(itemTemplate.getItemName());
            item.setItemUnit(itemTemplate.getItemUnit());
            //设置报名人数
            item.setItemAmount(itemTemplate.getItemAmount());
        }
        //设置初始人数
        item.setAthleteAmount(0);
        item.setItemAmount(1);
        //设置创建时间
        item.setCreateTime(LocalDateTime.now());
        item.setEditTime(LocalDateTime.now());
        if(item.getCatalog().length()==1){
            item.setProcess("finals");
        }
        else {
            item.setProcess("heats");
        }
        int effNum = 0;
        try {
            effNum = itemService.addItem(item);
        } catch (Exception e) {
             e.printStackTrace(); // 打印堆栈跟踪
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "添加失败");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @ApiOperation("删除项目")
    @DeleteMapping("/deleteItem")
    @RequiresRoles(value = {"1"})
    public ServerResponse deleteItem(Integer itemId) {
        int effNum = 0;
        try {
            effNum = itemService.removeItem(itemId);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @ApiOperation("获取项目详情信息")
    @GetMapping("/getItem")
//    @RequiresRoles(value = {"1"})
    public ServerResponse getItem(Item itemCondition) {

        Item item = itemService.getOneItemByCondition(itemCondition);
        if (item != null) {
            return ServerResponse.createBySuccess(item);
        }
        return ServerResponse.createByErrorMessage("查询不到该项目信息");
    }

    @ApiOperation("获取项目所有信息")
    @GetMapping("/getAll")
//    @RequiresRoles(value = {"1"})
    public ServerResponse getAll() {
        List<Item> itemList = itemService.list();
        return ServerResponse.createBySuccess(itemList);
    }

    @ApiOperation("修改项目")
    @PutMapping("/editItem")
    @RequiresRoles(value = {"1"})
    public ServerResponse editItem(@RequestBody Item item) {
        if (item == null || item.getItemName() == null) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，项目信息为空");
        }

        //通过Item名称和性别判断是否已存在相同Item
        Item tempItem = new Item();
        tempItem.setItemName(item.getItemName());
        tempItem.setItemSex(item.getItemSex());
        tempItem.setProcess(item.getProcess());
        //如果不是添加模板，则需要加上seasonId来区分是否会和之前届的冲突
        if (item.getSeason() != null && item.getSeason().getSeasonId() != null && item.getSeason().getSeasonId() != 0) {
            Season season = item.getSeason();
            tempItem.setSeason(season);
        }
        if (item.equals(itemService.getOneItemByCondition(tempItem))) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败，项目已存在");
        }
        item.setUId(item.getUser().getUserId());

        item.setEditTime(LocalDateTime.now());
        int effNum = 0;
        try {
            effNum = itemService.modifyItem(item);
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    @ApiOperation("查询项目模板")
    @GetMapping("/queryItemTemplate")
//    @RequiresAuthentication
    public ServerResponse queryItemTemplate() {
        List<Item> itemList = itemService.getItemTemplateList();
        return ServerResponse.createBySuccess(itemList);
    }

    @ApiOperation("获取项目模板详情信息")
    @GetMapping("/getItemTemplate")
    @RequiresRoles(value = {"1"})
    public ServerResponse getItemTemplate(Item itemCondition) {
        Item item = itemService.getItemTemplateDetail(itemCondition);
        if (item != null) {
            return ServerResponse.createBySuccess(item);
        }
        return ServerResponse.createByErrorMessage("查询不到该项目信息");
    }

    @ApiOperation("删除项目模板")
    @DeleteMapping("/deleteItemTemplate")
    @RequiresRoles(value = {"1"})
    public ServerResponse deleteItemTemplate(Item item) {
        Item tempItem = new Item();
        tempItem.setParentId(item.getItemId());
        IPage<Item> itemList = itemService.getItemByItemCondition(new Page(1, 9999), tempItem);
        if (itemList != null && itemList.getRecords() != null && itemList.getRecords().size() != 0) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败,该模板下有已举行的项目");
        }
        int effNum = 0;
        try {
            effNum = itemService.removeItem(item.getItemId());
        } catch (Exception e) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        if (effNum == 0) {
            return ServerResponse.createByErrorCodeMessage(400, "删除失败");
        }
        return ServerResponse.createBySuccessMessage("删除成功");
    }
}
