package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.TeamEnter;
import com.ashuo.scms.mapper.TeamEnterMapper;
import com.ashuo.scms.service.TeamEnterService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamEnterServiceImp implements TeamEnterService {
    @Autowired
    TeamEnterMapper teamMapper;

    @Override
    public IPage<TeamEnter> getAllTeam(Page<TeamEnter> page, TeamEnter team) {
        IPage<TeamEnter> teamList = teamMapper.queryTeamEnterByevent(page, team);
        return teamList;
    }


    @Override
    public List<TeamEnter> getTeamByCondition(TeamEnter teamCondition) {
        List<TeamEnter> team =  teamMapper.queryTeamEnterByCondition(teamCondition);
        return team;
    }


    @Override
    public int addTeam(TeamEnter team) {
        if (team == null) {
            return 0;
        } else {

            int effNum = teamMapper.insertTeamEnter(team);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int modifyTeam(TeamEnter team) {
        if (team == null) {
            return 0;
        } else {
            int effNum = teamMapper.updateTeamEnter(team);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int removeTeam(int teamId) {
        if (teamId == 0) {
            return 0;
        } else {
            int effNum = teamMapper.deleteTeamEnter(teamId);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }
}
