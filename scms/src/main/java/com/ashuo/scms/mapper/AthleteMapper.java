package com.ashuo.scms.mapper;

import com.ashuo.scms.entity.Athlete;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author AShuo
 * @since 2021-04-05
 */
public interface AthleteMapper extends BaseMapper<Athlete>{
    //增加Athlete
    int insertAthlete(Athlete athlete);

    Athlete findById(Integer athleteId);
    //按条件查询Athlete列表
    IPage<Athlete> queryAthleteByAthleteCondition(Page<Athlete> page, Athlete athlete);

    int updateAthlete(Athlete athlete);
    int updateShenhe(Athlete athlete);
    int updateGroup(Athlete athlete);

    int deleteAthlete(int athleteId);




}