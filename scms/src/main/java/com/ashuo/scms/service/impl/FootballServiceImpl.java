package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Football;
import com.ashuo.scms.mapper.FootballMapper;
import com.ashuo.scms.service.FootballService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FootballServiceImpl implements FootballService {
    @Autowired
    FootballMapper footballMapper;

    @Override
    public IPage<Football> getAllFootball(Page<Football> page, Football football) {
        IPage<Football> footballList = footballMapper.queryByFootballName(page, football);
        List<Football> records = footballList.getRecords();

        System.out.println("hello");
        System.out.println(records);
        System.out.println("hello");

        return footballList;
    }


    @Override
    public Football getFootballByCondition(Football footballCondition) {
        Football football = footballMapper.queryByFootballCondition(footballCondition);
        return football;
    }


    @Override
    public int addFootball(Football football) {
        if (football == null) {
            return 0;
        } else {

            int effNum = footballMapper.insertFootball(football);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int modifyFootball(Football football) {
        if (football == null) {
            return 0;
        } else {
            int effNum = footballMapper.updateFootball(football);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int removeFootball(int schedule_id) {
        if (schedule_id == 0) {
            return 0;
        } else {
            int effNum = footballMapper.deleteFootball(schedule_id);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }
}
