package com.ashuo.scms.mapper;

import com.ashuo.scms.dto.AthleteScoreDto;
import com.ashuo.scms.entity.Score;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author AShuo
 * @since 2021-04-05
 */
public interface ScoreMapper {
    //增加单个Score
    int insertScore(Score score);

    //修改Score
    int updateScore(Score score);

    //按条件查询Score
    IPage<Score> queryScoreByScoreCondition(Page<Score> page, @Param("score") Score score);

    //查询分数排名并限制数量
    List<Score> queryScoreByItemIdLimit(@Param("itemId") Integer itemId, @Param("condition") String condition);

    //获取单个Score
    Score queryOneScoreByScoreId(@Param("scoreId") Integer scoreId);


    List<Integer> findGroupsByItemAndProcess(@Param("itemId") int itemId, @Param("process") String process);

    int countNonQualifiedAthletes(@Param("itemId") int itemId, @Param("group") int group, @Param("process") String process);


    void promoteTopThree(@Param("itemId") int itemId, @Param("group") int group, @Param("process") String process);

    void promoteTopTwo(@Param("itemId") int itemId, @Param("group") int group, @Param("process") String process);
    //查询返回Dto对象
    IPage<AthleteScoreDto> queryAthleteScoreDto(Page<AthleteScoreDto> page, @Param("score") Score score);

    List<Integer> promoteTopThreeAndGetAtheleteIds(int itemId, int group, String process);


    void promoteRemainThree(@Param("itemId") int itemId, @Param("process") String process);

    void promoteRemainTwo(@Param("itemId") int itemId, @Param("process") String process);
}
