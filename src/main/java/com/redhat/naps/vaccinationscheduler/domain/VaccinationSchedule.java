/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.redhat.naps.vaccinationscheduler.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoftlong.HardMediumSoftLongScore;
import org.optaplanner.core.api.solver.SolverStatus;

@PlanningSolution
public class VaccinationSchedule {

    private List<VaccineType> vaccineTypeList;

    @ProblemFactCollectionProperty
    private List<PlanningVaccinationCenter> vaccinationCenterList;

    private List<LocalDateTime> timeslotDateTimeList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "personRange")
    private List<PlanningPerson> personList;

    @PlanningEntityCollectionProperty
    private List<PlanningInjection> injectionList;

    @PlanningScore
    private HardMediumSoftLongScore score;

    // Ignored by OptaPlanner, used by the UI to display solve or stop solving button
    private SolverStatus solverStatus;

    // No-arg constructor required for OptaPlanner
    public VaccinationSchedule() {
    }

    public VaccinationSchedule(List<VaccineType> vaccineTypeList, List<PlanningVaccinationCenter> vaccinationCenterList, List<LocalDateTime> timeslotDateTimeList, List<PlanningPerson> personList, List<PlanningInjection> injectionList) {
        this.vaccineTypeList = vaccineTypeList;
        this.vaccinationCenterList = vaccinationCenterList;
        this.timeslotDateTimeList = timeslotDateTimeList;
        this.personList = personList;
        this.injectionList = injectionList;
    }

    public List<VaccineType> getVaccineTypeList() {
        return vaccineTypeList;
    }

    public void setVaccineTypeList(List<VaccineType> vaccineTypeList) {
        this.vaccineTypeList = vaccineTypeList;
    }

    public void setVaccinationCenterList(List<PlanningVaccinationCenter> vaccinationCenterList) {
        this.vaccinationCenterList = vaccinationCenterList;
    }

    public void setTimeslotDateTimeList(List<LocalDateTime> timeslotDateTimeList) {
        this.timeslotDateTimeList = timeslotDateTimeList;
    }

    public void setPersonList(List<PlanningPerson> personList) {
        this.personList = personList;
    }

    public void setInjectionList(List<PlanningInjection> injectionList) {
        this.injectionList = injectionList;
    }

    public void setScore(HardMediumSoftLongScore score) {
        this.score = score;
    }

    public List<PlanningVaccinationCenter> getVaccinationCenterList() {
        return vaccinationCenterList;
    }

    public List<LocalDateTime> getTimeslotDateTimeList() {
        return timeslotDateTimeList;
    }

    public List<PlanningPerson> getPersonList() {
        return personList;
    }

    public List<PlanningInjection> getInjectionList() {
        return injectionList;
    }

    public HardMediumSoftLongScore getScore() {
        return score;
    }

    public SolverStatus getSolverStatus() {
        return solverStatus;
    }

    public void setSolverStatus(SolverStatus solverStatus) {
        this.solverStatus = solverStatus;
    }

}
