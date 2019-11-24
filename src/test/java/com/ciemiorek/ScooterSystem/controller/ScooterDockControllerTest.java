package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.model.Scooter;
import com.ciemiorek.ScooterSystem.repository.ScooterDockRepository;
import com.ciemiorek.ScooterSystem.repository.ScooterRepository;
import com.ciemiorek.ScooterSystem.service.ScooterDockService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.constraints.AssertFalse;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScooterDockControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ScooterRepository  scooterRepository;

    @Test
    public void aIfGetScootersRequestIsCorrectShouldReturnHttpCode200AndInitialScooterList() throws Exception {
        mockMvc.perform(get("/scooter-dock/{scooterDockId}/scooters",1))
                .andExpect(status().is(200))
                .andExpect(content().json("[{\"id\":8,\"modelName\":\"V-SPEED-1\",\"maxSpeed\":40,\"rentalPrice\":39.99},{\"id\":5,\"modelName\":\"ERE-321\",\"maxSpeed\":25,\"rentalPrice\":19.99},{\"id\":7,\"modelName\":\"V-SPEED-1\",\"maxSpeed\":35,\"rentalPrice\":29.99},{\"id\":6,\"modelName\":\"RTT-43z\",\"maxSpeed\":20,\"rentalPrice\":15.5}]"
                ));

    }

    @Test
    public void bGetScooterDocWhichNotExistShouldReturnHttpCode409AndError() throws Exception {
        mockMvc.perform(get("/scooter-dock/{scooterDockId}/scooters",9999999))
                .andExpect(status().is(409))
                .andExpect(content().json("{\"errorCode\":\"ERR008\",\"errorMsg\":\"Dok o podanum id nie istnieje.\",\"status\":\"ERROR\"}"));
    }

    @Test
    public void cIfPutScooterDockWithIdWhichIsCorrectShouldReturnHttpCode200AndRemoveScooter() throws Exception {
        mockMvc.perform(put("/scooter-dock/{scooterDockId}/removeScooter",1).param("scooterId","7"))
                .andExpect(status().is(200));

        Optional<Scooter> optionalScooter = scooterRepository.findById((long) 7);
        Assert.assertTrue(optionalScooter.isPresent());
        Scooter scooter = optionalScooter.get();
        Assert.assertNull(scooter.getScooterDock());
    }

    @Test
    public void dIfPutScooterDockWithIdWhichIsInAnotherDockShouldReturnHttpCode409AndError() throws Exception {
        mockMvc.perform(put("/scooter-dock/{scooterDockId}/removeScooter",1).param("scooterId","14"))
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR018\",\n" +
                        "  \"errorMsg\": \"Hulajnoga jest w innym doku.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));

    }

    @Test
    public void eIfPutScooterDockWithIdWhichNotExistShouldReturnHttpCode409AndError() throws Exception {
        mockMvc.perform(put("/scooter-dock/{scooterDockId}/removeScooter",1).param("scooterId","99"))
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR010\",\n" +
                        "  \"errorMsg\": \"Hulajnnoga o podanym id nie istnieje.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));

    }


}

