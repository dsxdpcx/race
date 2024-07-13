package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Cheer;
import com.ashuo.scms.mapper.AthleteMapper;
import com.ashuo.scms.mapper.CheerMapper;
import com.ashuo.scms.service.CheerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheerServiceImpl extends ServiceImpl<CheerMapper, Cheer> implements CheerService {

    /*@Autowired
    private CheerMapper cheerMapper;

    @Override
    public IPage<Cheer> getCheerByCondition(Page<Cheer> page, Cheer cheerCondition) {
        if (cheerCondition == null) {
            return null;
        }
        return cheerMapper.queryCheerByCheerCondition(page, cheerCondition);
    }


    public int modifycheerShenhe(Cheer cheer) {
        if (cheer == null) {
            return 0;
        }
        return cheerMapper.updateShenhe(cheer);
    }
*/
}
