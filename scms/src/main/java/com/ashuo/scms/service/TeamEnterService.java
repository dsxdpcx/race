package com.ashuo.scms.service;

import com.ashuo.scms.entity.TeamEnter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;


public interface TeamEnterService {
    IPage<TeamEnter> getAllTeam(Page<TeamEnter> page, TeamEnter team);

    List<TeamEnter> getTeamByCondition(TeamEnter teamEnter);

    int addTeam(TeamEnter team);

    int modifyTeam(TeamEnter team);

    int removeTeam(int Id);
}

