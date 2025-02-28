package com.sounganization.botanify.domain.garden.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sounganization.botanify.domain.garden.entity.PlantAlarm;
import com.sounganization.botanify.domain.garden.entity.QPlant;
import com.sounganization.botanify.domain.garden.entity.QPlantAlarm;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PlantAlarmCustomRepositoryImpl implements PlantAlarmCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlantAlarm> findDueAlarms(LocalDateTime currentDateTime) {
        QPlantAlarm alarm = QPlantAlarm.plantAlarm;
        QPlant plant = QPlant.plant;

        return queryFactory
                .selectFrom(alarm)
                .join(alarm.plant, plant).fetchJoin()
                .where(
                        alarm.isEnabled.isTrue(),
                        alarm.nextAlarmDateTime.loe(currentDateTime)
                )
                .fetch();
    }
}
