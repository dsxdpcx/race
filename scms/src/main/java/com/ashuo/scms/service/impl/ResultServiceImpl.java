package com.ashuo.scms.service.impl;

import com.ashuo.scms.entity.Athlete;
import com.ashuo.scms.entity.Result;
import com.ashuo.scms.mapper.AthleteMapper;
import com.ashuo.scms.mapper.ResultMapper;
import com.ashuo.scms.service.ResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ResultServiceImpl extends ServiceImpl<ResultMapper, Result> implements ResultService {
    @Autowired
    private AthleteMapper athleteMapper;

    @Autowired
    private ResultMapper resultMapper;



    public List<Athlete> getAdvancedAthletes(String round, int nPerGroup, int extraSpots) {
        Map<Integer, List<Result>> groupedResults;
        switch (round) {
            case "heats":
                groupedResults = resultMapper.findAll().stream()
                        .collect(Collectors.groupingBy(Result::getHeatsGroupNumber));
                break;
            case "semifinals":
                groupedResults = resultMapper.findAll().stream()
                        .collect(Collectors.groupingBy(Result::getSemifinalsGroupNumber));
                break;
            case "finals":
                groupedResults = resultMapper.findAll().stream()
                        .collect(Collectors.groupingBy(Result::getFinalsGroupNumber));
                break;
            default:
                throw new IllegalArgumentException("Invalid round: " + round);
        }

        List<Result> advancedResults = new ArrayList<>();

        for (List<Result> groupResults : groupedResults.values()) {
            List<Result> sortedGroupResults = groupResults.stream()
                    .sorted(Comparator.comparing(result -> getTimeByRound(result, round)))
                    .collect(Collectors.toList());
            advancedResults.addAll(sortedGroupResults.subList(0, Math.min(nPerGroup, sortedGroupResults.size())));
        }

        List<Result> remainingResults = resultMapper.findAll().stream()
                .filter(result -> !advancedResults.contains(result))
                .sorted(Comparator.comparing(result -> getTimeByRound(result, round)))
                .collect(Collectors.toList());

        advancedResults.addAll(remainingResults.subList(0, Math.min(extraSpots, remainingResults.size())));
        Athlete athlete= new Athlete();
        return advancedResults.stream()
                .map(result -> athleteMapper.selectById(result.getAthleteId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BigDecimal getTimeByRound(Result result, String round) {
        switch (round) {
            case "heats":
                return result.getHeatsTime();
            case "semifinals":
                return result.getSemifinalsTime();
            case "finals":
                return result.getFinalsTime();
            default:
                throw new IllegalArgumentException("Invalid round: " + round);
        }
    }

    @Override
    public void updatePromotion(List<Athlete> promotedAthletes) {
        promotedAthletes.forEach(athlete -> athleteMapper.updateById(athlete));
    }
}
