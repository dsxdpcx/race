package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Notice;
import com.ashuo.scms.mapper.NoticeMapper;
import com.ashuo.scms.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
