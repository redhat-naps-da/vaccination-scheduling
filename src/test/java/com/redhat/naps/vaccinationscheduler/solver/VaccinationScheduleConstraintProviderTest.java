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

package com.redhat.naps.vaccinationscheduler.solver;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.redhat.naps.vaccinationscheduler.domain.PlanningInjection;
import com.redhat.naps.vaccinationscheduler.domain.PlanningLocation;
import com.redhat.naps.vaccinationscheduler.domain.PlanningPerson;
import com.redhat.naps.vaccinationscheduler.domain.PlanningPractitionerRole;
import com.redhat.naps.vaccinationscheduler.domain.PlanningVaccinationCenter;
import com.redhat.naps.vaccinationscheduler.domain.VaccinationSchedule;
import com.redhat.naps.vaccinationscheduler.domain.VaccineType;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.optaplanner.test.api.score.stream.ConstraintVerifier;

class VaccinationScheduleConstraintProviderTest {

    private static final PlanningVaccinationCenter VACCINATION_CENTER_1 = new PlanningVaccinationCenter("1", "Alpha", new PlanningLocation(0, 0), 3);
    private static final PlanningPractitionerRole PRACTITIONER_ROLE_1 = new PlanningPractitionerRole("1", "Dr Steve Smith", "1", "Alpha");
    private static final LocalDate MONDAY = LocalDate.of(2021, 2, 1);
    private static final LocalDate TUESDAY = LocalDate.of(2021, 2, 2);
    private static final LocalDate WEDNESDAY = LocalDate.of(2021, 2, 3);
    private static final LocalDateTime MONDAY_0900 = LocalDateTime.of(2021, 2, 1, 9, 0);
    private static final LocalDateTime MONDAY_1000 = LocalDateTime.of(2021, 2, 1, 10, 0);
    private static final LocalDateTime MONDAY_1100 = LocalDateTime.of(2021, 2, 1, 11, 0);
    private static final LocalDateTime TUESDAY_0900 = LocalDateTime.of(2021, 2, 2, 9, 0);
    private static final LocalDateTime WEDNESDAY_0900 = LocalDateTime.of(2021, 2, 3, 9, 0);
    private static final PlanningPerson ANN = new PlanningPerson(Long.toString(1), "Ann", new PlanningLocation(1, 0), LocalDate.of(1950, 1, 1), 71);
    private static final PlanningPerson BETH = new PlanningPerson(Long.toString(2), "Beth", new PlanningLocation(2, 0), LocalDate.of(1980, 1, 1), 41);
    private static final PlanningPerson CARL = new PlanningPerson(Long.toString(3), "Carl", new PlanningLocation(3, 0), LocalDate.of(1970, 1, 1), 51,
            true, VaccineType.MODERNA, MONDAY);

    private final ConstraintVerifier<VaccinationScheduleConstraintProvider, VaccinationSchedule> constraintVerifier =
            ConstraintVerifier.build(new VaccinationScheduleConstraintProvider(), VaccinationSchedule.class, PlanningInjection.class);

    @Ignore
    @Test
    void personConflict() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::personConflict)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN),
                        new PlanningInjection(2, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1000, VaccineType.PFIZER, BETH),
                        new PlanningInjection(3, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1100, VaccineType.PFIZER, ANN)
                        )
                .penalizesBy(1);
    }

    @Ignore
    @Test
    void ageLimitAstrazeneca() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::ageLimitAstrazeneca)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN)
                )
                .penalizesBy(0);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::ageLimitAstrazeneca)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.ASTRAZENECA, ANN)
                )
                .penalizesBy(1);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::ageLimitAstrazeneca)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.ASTRAZENECA, BETH)
                )
                .penalizesBy(0);
    }

    @Ignore
    @Test
    void secondShotInvalidVaccineType() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotInvalidVaccineType)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN),
                        new PlanningInjection(2, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1000, VaccineType.PFIZER, CARL)
                )
                .penalizesBy(1);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotInvalidVaccineType)
                .given(
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN),
                        new PlanningInjection(2, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1000, VaccineType.MODERNA, CARL)
                )
                .penalizesBy(0);
    }

    @Ignore
    @Test
    void secondShotMustBeAssigned() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotMustBeAssigned)
                .given(ANN, BETH, CARL,
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, CARL)
                        )
                .penalizesBy(0);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotMustBeAssigned)
                .given(ANN, BETH, CARL,
                        new PlanningInjection(1, VACCINATION_CENTER_1,PRACTITIONER_ROLE_1,  0, MONDAY_0900, VaccineType.PFIZER, ANN)
                        )
                .penalizesBy(1);
    }

    @Ignore
    @Test
    void assignAllOlderPeople() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::assignAllOlderPeople)
                .given(ANN, BETH, CARL,
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN)
                        )
                .penalizesBy(51 + 41);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::assignAllOlderPeople)
                .given(ANN, BETH, CARL,
                        new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN),
                        new PlanningInjection(2, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1000, VaccineType.PFIZER, CARL)
                        )
                .penalizesBy(41);
    }

    @Ignore
    @Test
    void secondShotIdealDay() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotIdealDay)
                .given(new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.MODERNA, CARL))
                .penalizesBy(0);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotIdealDay)
                .given(new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, TUESDAY_0900, VaccineType.MODERNA, CARL))
                .penalizesBy(1);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::secondShotIdealDay)
                .given(new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, WEDNESDAY_0900, VaccineType.MODERNA, CARL))
                .penalizesBy(2);
    }

    @Ignore
    @Test
    void distanceCost() {
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::distanceCost)
                .given(new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN))
                .penalizesBy((long) PlanningLocation.METERS_PER_DEGREE);
        constraintVerifier.verifyThat(VaccinationScheduleConstraintProvider::distanceCost)
                .given(new PlanningInjection(1, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_0900, VaccineType.PFIZER, ANN),
                        new PlanningInjection(2, VACCINATION_CENTER_1, PRACTITIONER_ROLE_1, 0, MONDAY_1000, VaccineType.PFIZER, BETH))
                .penalizesBy(3L * (long) PlanningLocation.METERS_PER_DEGREE);
    }

}
