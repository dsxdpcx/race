package com.ashuo.scms.controller;


import com.ashuo.scms.common.lang.ServerResponse;
import com.ashuo.scms.entity.Message;
import com.ashuo.scms.entity.Notice;
import com.ashuo.scms.service.MessageService;
import com.ashuo.scms.service.NoticeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "轮播图")
@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;


    @ApiOperation("轮播")
    @GetMapping("/queryNotice")
    public ServerResponse queryNotice() {
        List<Notice> noticeList = noticeService.list();
        return ServerResponse.createBySuccess(noticeList);
    }
}
