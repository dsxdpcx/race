package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.TeamEnter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeamEnterMapper {
    //增加Team
    int insertTeamEnter(TeamEnter team);

    //修改Team
    int updateTeamEnter(TeamEnter team);

    //删除Team
    int deleteTeamEnter(int Id);

    //按条件查询Team
    IPage<TeamEnter> queryTeamEnterByevent(Page<TeamEnter> page, @Param("TeamEnter") TeamEnter team);


    List<TeamEnter> queryTeamEnterByCondition(@Param("teamEnter") TeamEnter teamEnter);

}
