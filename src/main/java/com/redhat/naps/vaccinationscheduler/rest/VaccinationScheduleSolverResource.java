/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package com.redhat.naps.vaccinationscheduler.rest;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.optaplanner.core.api.score.ScoreManager;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.api.solver.SolverStatus;

import org.jboss.logging.Logger;

import com.redhat.naps.vaccinationscheduler.domain.VaccinationSchedule;
import com.redhat.naps.vaccinationscheduler.VaccineSchedulingService;


@Path("/vaccinationSchedule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VaccinationScheduleSolverResource {

    private static Logger log = Logger.getLogger(VaccinationScheduleSolverResource.class);

    @Inject
    VaccineSchedulingService vaccinationScheduleService;

    @Inject
    SolverManager<VaccinationSchedule, Long> solverManager;
    
    @Inject
    ScoreManager<VaccinationSchedule, HardMediumSoftScore> scoreManager;


    @GET
    @Path("/")
    public VaccinationSchedule get() {
        // Get the solver status before loading the solution
        // to avoid the race condition that the solver terminates between them
        SolverStatus solverStatus = getSolverStatus();
        VaccinationSchedule solution = vaccinationScheduleService.getVaccinationSchedule();
        scoreManager.updateScore(solution); // Sets the score
        solution.setSolverStatus(solverStatus);
        return solution;
    }


    @POST
    @Path("/refreshVaccinationSchedule")
    public Response refreshVaccinationSchedulingData() throws IOException {

            VaccinationSchedule vSchedule = vaccinationScheduleService.refreshVaccinationSchedule();
            return Response.ok(vSchedule.getPersonList().size()).build();
    }

    @POST
    @Path("/solve")
    public void solve() {
        log.info("solve() ... ");  
        solverManager.solveAndListen(1L,
                (problemId) -> vaccinationScheduleService.getVaccinationSchedule(),
                vaccinationScheduleService::saveVaccinationSchedule,
                vaccinationScheduleService::handleException);
    }

    public SolverStatus getSolverStatus() {
        return solverManager.getSolverStatus(1L);
    }

    @POST
    @Path("/stopSolving")
    public void stopSolving() {
        log.info("stopSolving() ... ");        
        solverManager.terminateEarly(1L);
    }

}
