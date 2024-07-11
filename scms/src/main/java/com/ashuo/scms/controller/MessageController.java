package com.ashuo.scms.controller;

import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Borrow;
import com.ashuo.scms.entity.Message;
import com.ashuo.scms.service.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "消息提醒接口")
@RestController
@Slf4j
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;


    @ApiOperation("未读消息")
    @GetMapping("/queryMessage")
    public ServerResponse queryMessage(Integer userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_read", 0);
        List<Message> messageList = messageService.list(queryWrapper);
        Integer size = messageList.size();
        return ServerResponse.createBySuccess(size);

    }
    @ApiOperation("全部消息")
    @GetMapping("/queryAllMessage")
    public ServerResponse queryAllMessage(Integer userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Message> messageList = messageService.list(queryWrapper);
        for (Message message:messageList
             ){
            message.setIsRead(1);
        }
        return ServerResponse.createBySuccess(messageList);
    }

}
