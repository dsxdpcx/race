package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Season;
import com.ashuo.scms.entity.Winners;
import com.ashuo.scms.mapper.SeasonMapper;
import com.ashuo.scms.mapper.WinnersMapper;
import com.ashuo.scms.service.SeasonService;
import com.ashuo.scms.service.WinnersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WinnersServiceImpl extends ServiceImpl<WinnersMapper, Winners> implements WinnersService {
}
