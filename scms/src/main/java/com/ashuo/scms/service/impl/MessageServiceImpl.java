package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Message;
import com.ashuo.scms.mapper.MessageMapper;
import com.ashuo.scms.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper,Message> implements MessageService {
}
