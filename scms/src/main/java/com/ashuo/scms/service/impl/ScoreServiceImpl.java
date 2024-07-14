package com.ashuo.scms.service.impl;

import com.ashuo.scms.dto.AthleteScoreDto;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.entity.Score;
import com.ashuo.scms.entity.Winners;
import com.ashuo.scms.mapper.AthleteMapper;
import com.ashuo.scms.mapper.ItemMapper;
import com.ashuo.scms.mapper.ScoreMapper;
import com.ashuo.scms.mapper.WinnersMapper;
import com.ashuo.scms.service.ScoreService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author AShuo
 * @since 2021-04-05
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper,Score> implements ScoreService {
    @Autowired
    ScoreMapper scoreMapper;
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    AthleteMapper athleteMapper;

    @Autowired
    WinnersMapper winnersMapper;

    @Override
    public IPage<Score> getScoreByScoreCondition(Page<Score> page, Score score) {
        if (score == null) {
            return null;
        }
        IPage<Score> scoreList = scoreMapper.queryScoreByScoreCondition(page, score);
        return scoreList;
    }


    @Override
    public IPage<AthleteScoreDto> getAthleteScoreDto(Page<AthleteScoreDto> page, Score score) {
        IPage<AthleteScoreDto> scoreDtoList = scoreMapper.queryAthleteScoreDto(page, score);
        return scoreDtoList;
    }


    @Override
    public List<Score> getScoreByItemIdLimit(int itemId, String condition) {
        if (itemId == 0) {
            return null;
        }
        List<Score> scoreList = scoreMapper.queryScoreByItemIdLimit(itemId, condition);
        return scoreList;
    }

    @Override
    public Score getOneScoreByScoreId(int scoreId) {
        if (scoreId == 0) {
            return null;
        }
        Score score = scoreMapper.queryOneScoreByScoreId(scoreId);
        return score;
    }


    @Override
    public int addScore(Score score) {
        if (score == null) {
            return 0;
        } else {
            int effNum = scoreMapper.insertScore(score);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int modifyScore(Score score) {
        if (score == null) {
            return 0;
        } else {
            int effNum = scoreMapper.updateScore(score);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }
    @Override
    public void checkAndPromoteTopSixteen(int itemId, String process) {

            if (scoreMapper.countNonQualifiedAthletesswim(itemId, process) == 0) {
                scoreMapper.promoteTopSixteen(itemId, process);
                List<Integer> atheleteIds = scoreMapper.promoteTopTopSixteenAndGetAtheleteIds(itemId, process);

                System.out.println(atheleteIds);
                if (process.equals("heats")){
                    for (Integer athleteId:atheleteIds) {
                        Athlete athlete=athleteMapper.selectById(athleteId);
                        athlete.setProcess("semifinals");
                        athlete.setScoreStatus(0);
                        athleteMapper.insert(athlete);
                    }
                }else if(process.equals("semifinals")){
                    for (Integer athleteId:atheleteIds) {
                        Athlete athlete = athleteMapper.selectById(athleteId);
                        athlete.setProcess("finals");
                        athlete.setScoreStatus(0);
                        athleteMapper.insert(athlete);
                    }
                }
            }
    }

    @Override
    public void checkAndPromoteTopX(int number, int itemId, String process) {
        Item item = itemMapper.selectById(itemId);
        if (scoreMapper.countNonQualifiedAthletesswim(itemId, process) == 0) {
            scoreMapper.promoteTopX(number,itemId, process);
            List<Integer> atheleteIds = scoreMapper.promoteTopTopSixteenAndGetAtheleteIds(itemId, process);
            System.out.println(atheleteIds);
            if (process.equals("heats")&&item.getCatalog().length()==2){
                item.setProcess("finals");

                itemMapper.insert(item);
                Item item1 = itemMapper.queryOneItemByItemCondition(item);
                for (Integer athleteId:atheleteIds) {
                    Athlete athlete=athleteMapper.selectById(athleteId);
                    athlete.setIId(item1.getItemId());
                    athlete.setProcess("semifinals");
                    athlete.setScoreStatus(0);
                    athleteMapper.insert(athlete);
                }
            }else if (process.equals("heats")&&item.getCatalog().length()==3){
                item.setProcess("semifinals");
                itemMapper.insert(item);
                Item item1 = itemMapper.queryOneItemByItemCondition(item);
                for (Integer athleteId:atheleteIds) {
                    Athlete athlete=athleteMapper.selectById(athleteId);
                    athlete.setIId(item1.getItemId());
                    athlete.setProcess("semifinals");
                    athlete.setScoreStatus(0);
                    athleteMapper.insert(athlete);
                }
            }

            else if(process.equals("semifinals")){
                item.setProcess("finals");
                itemMapper.insert(item);
                Item item1 = itemMapper.queryOneItemByItemCondition(item);
                for (Integer athleteId:atheleteIds) {
                    Athlete athlete = athleteMapper.selectById(athleteId);
                    athlete.setIId(item1.getItemId());
                    athlete.setProcess("finals");
                    athlete.setScoreStatus(0);
                    athleteMapper.insert(athlete);
                }
            }
        }
    }

    @Override
    public List<Integer> getWinners(int itemId) {
        return scoreMapper.getWinners(itemId);
    }

    @Override
    public void produceWinners(int itemId) {
        String process="finals";
        if(scoreMapper.countNonQualifiedAthletesswim(itemId,process)==0){
            List<Integer> winners=scoreMapper.getWinners(itemId);
            for (Integer athleteId:winners) {
                Athlete athlete=athleteMapper.selectById(athleteId);
                athlete.setProcess("winners");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }
    }


    public void checkAndPromoteTopEight(int itemId, String process) {

        if (scoreMapper.countNonQualifiedAthletesswim(itemId, process) == 0) {
            scoreMapper.promoteTopSixteen(itemId, process);
            List<Integer> atheleteIds = scoreMapper.promoteTopTopSixteenAndGetAtheleteIds(itemId, process);
            System.out.println(atheleteIds);
            if (process.equals("heats")){
                for (Integer athleteId:atheleteIds) {
                    Athlete athlete=athleteMapper.selectById(athleteId);
                    athlete.setProcess("semifinals");
                    athlete.setScoreStatus(0);
                    athleteMapper.insert(athlete);
                }
            }else if(process.equals("semifinals")){
                for (Integer athleteId:atheleteIds) {
                    Athlete athlete = athleteMapper.selectById(athleteId);
                    athlete.setProcess("finals");
                    athlete.setScoreStatus(0);
                    athleteMapper.insert(athlete);
                }
            }
        }
    }

    @Override
    public void checkAndPromoteTwo(int itemId, String process) {


        Item item = itemMapper.selectById(itemId);
        List<Integer> groups = scoreMapper.findGroupsByItemAndProcess(itemId, process);
        System.out.println(groups);
        for (int group : groups) {
            if (scoreMapper.countNonQualifiedAthletes(itemId, group, process) == 0) {
                scoreMapper.promoteTopTwo(itemId, group, process);
            }
        }
        scoreMapper.promoteRemainTwo(itemId, process);
        List<Integer> atheleteIds = scoreMapper.promoteTopThreeAndGetAtheleteIds(itemId, process);
        if (process.equals("heats")&&item.getCatalog().length()==2){
            item.setProcess("finals");

            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);
            for (Integer athleteId:atheleteIds) {
                Athlete athlete=athleteMapper.selectById(athleteId);
                athlete.setIId(item1.getItemId());
                athlete.setProcess("finals");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }else if (process.equals("heats")&&item.getCatalog().length()==3){
            item.setProcess("semifinals");
            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);
            for (Integer athleteId:atheleteIds) {
                Athlete athlete=athleteMapper.selectById(athleteId);
                athlete.setIId(item1.getItemId());
                athlete.setProcess("semifinals");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }
        else if(process.equals("semifinals")){
            item.setProcess("finals");
            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);
            for (Integer athleteId:atheleteIds) {
                Athlete athlete = athleteMapper.selectById(athleteId);
                athlete.setIId(item1.getItemId());
                athlete.setProcess("finals");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }
    }

    @Override
    public void checkAndPromoteThree(int itemId,String process) {
        Item item = itemMapper.selectById(itemId);
        List<Integer> groups = scoreMapper.findGroupsByItemAndProcess(itemId, process);
        System.out.println(groups);
        for (int group : groups) {
            if (scoreMapper.countNonQualifiedAthletes(itemId, group, process) == 0) {
                scoreMapper.promoteTopThree(itemId, group, process);
            }
        }

        scoreMapper.promoteRemainThree(itemId, process);
        List<Integer> atheleteIds = scoreMapper.promoteTopThreeAndGetAtheleteIds(itemId, process);
        if (process.equals("heats")&&item.getCatalog().length()==2){
            item.setProcess("finals");

            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);

            for (Integer athleteId:atheleteIds) {
                Athlete athlete=athleteMapper.selectById(athleteId);
                athlete.setIId(item1.getItemId());
                athlete.setProcess("semifinals");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }else if(process.equals("heats")&&item.getCatalog().length()==3){
            item.setProcess("semifinals");
            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);
            for (Integer athleteId:atheleteIds) {
                Athlete athlete=athleteMapper.selectById(athleteId);
                athlete.setIId(item1.getItemId());
                athlete.setProcess("semifinals");
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }


        else if(process.equals("semifinals")){
            item.setProcess("finals");
            itemMapper.insert(item);
            Item item1 = itemMapper.queryOneItemByItemCondition(item);

            for (Integer athleteId:atheleteIds) {
                Athlete athlete = athleteMapper.selectById(athleteId);
                athlete.setProcess("finals");
                athlete.setIId(item1.getItemId());
                athlete.setScoreStatus(0);
                athleteMapper.insert(athlete);
            }
        }

    }

    @Override
    public boolean qualify(int itemId, String process) {

        if (scoreMapper.countNonQualifiedAthletesswim(itemId, process) == 0) {
            return true;
        }
        return false;
    }

}
