package com.ashuo.scms.service.impl;

import com.ashuo.scms.common.consant.Consant;
import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Item;
import com.ashuo.scms.entity.Season;
import com.ashuo.scms.mapper.AthleteMapper;
import com.ashuo.scms.mapper.ItemMapper;
import com.ashuo.scms.service.AthleteService;
import com.ashuo.scms.service.SeasonService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author AShuo
 * @since 2021-04-05
 */
@Service
public class AthleteServiceImpl extends ServiceImpl<AthleteMapper,Athlete> implements AthleteService {

    @Autowired
    AthleteMapper athleteMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    SeasonService seasonService;

    @Override
    public IPage<Athlete> getAthleteByCondition(Page<Athlete> page, Athlete athleteCondition) {
        if (athleteCondition == null) {
            return null;
        }
        IPage<Athlete> athleteList = athleteMapper.queryAthleteByAthleteCondition(page, athleteCondition);

        return athleteList;
    }



    @Override
    public int addAthlete(Athlete athlete) {
        if (athlete == null) {
            return 0;
        } else {
            int effNum = athleteMapper.insertAthlete(athlete);
            //将对应项目报名数量增加一
            Item item = itemMapper.queryOneItemByItemCondition(athlete.getItem());
            item.setAthleteAmount(item.getAthleteAmount() + 1);
            int effNum2 = itemMapper.updateItem(item);
            //将本届运动会的报名人数增加一
            Season season = seasonService.getSeasonById(item.getSeason());
            season.setSeasonAthleteAmount(season.getSeasonAthleteAmount() + 1);
            int effNum3 = seasonService.modifySeason(season);
            if (effNum != 1 || effNum2 != 1 || effNum3 != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int modifyAthlete(Athlete athlete) {
        if (athlete == null) {
            return 0;
        } else {
            int effNum = athleteMapper.updateAthlete(athlete);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    public int modifyShenhe(Athlete athlete) {
        if (athlete == null) {
            return 0;
        } else {
            int effNum = athleteMapper.updateShenhe(athlete);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }
    public int modifyGroup(Athlete athlete) {
        if (athlete == null) {
            return 0;
        } else {
            int effNum = athleteMapper.updateGroup(athlete);
            if (effNum != 1) {
                return 0;
            } else {
                return effNum;
            }
        }
    }

    @Override
    public int removeAthlete(int athleteId) {
        if (athleteId == 0) {
            return 0;
        } else {
            Athlete athlete = new Athlete();
            athlete.setAthleteId(athleteId);
            IPage<Athlete> athleteList = athleteMapper.queryAthleteByAthleteCondition(new Page<>(Consant.MINCURRENTPAGE, Consant.MINPAGESIZE), athlete);
            athlete = athleteList.getRecords().get(0);
            Item temp = (athlete.getItem());
            Item item = itemMapper.queryOneItemByItemCondition(temp);
            //将对应项目报名数量减一
            item.setAthleteAmount(item.getAthleteAmount() - 1);
            int effNum = itemMapper.updateItem(item);
            int effNum2 = athleteMapper.deleteAthlete(athleteId);
            //将本届运动会的报名人数减一
            Season season = seasonService.getSeasonById(item.getSeason());
            season.setSeasonAthleteAmount(season.getSeasonAthleteAmount() - 1);
            int effNum3 = seasonService.modifySeason(season);
            if (effNum <= 0 || effNum2 <= 0 || effNum3 <= 0) {
                return 0;
            } else {
                return effNum;
            }
        }
    }


}
