package com.ciemiorek.ScooterSystem.controller;

import com.ciemiorek.ScooterSystem.api.response.BalanceResponse;
import com.ciemiorek.ScooterSystem.api.response.CreateUserAccountResponse;
import com.ciemiorek.ScooterSystem.model.UserAccount;
import com.ciemiorek.ScooterSystem.repository.UserAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void aIfCreateAccountRequestContainsWrongEmailAddressShouldReturnHttpCode400AndErrorMsg() throws Exception {
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 20,\n" +
                                "  \"ownerEmail\": \"asdasd\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        )
                .andExpect(status().is(400))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR002\",\n" +
                        "  \"errorMsg\": \"Podaj poprawny adres e-mail.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));


    }

    @Test
    public void bIfCreateAccountRequestContainsWrongAgeShouldReturnHttpCode400AndErrorMsg() throws Exception {
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 111,\n" +
                                "  \"ownerEmail\": \"asda@sd.pl\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        )
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR003\",\n" +
                        "  \"errorMsg\": \"Wiek powinien micŜieć sieę w zakresie od 1 do 100.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));


    }

    @Test
    public void cIfCreateAccountRequestContainsCorrectDataShouldReturnHttpCode200AndCreateAccount() throws Exception {

        //mvcResult is to get object in  response
        MvcResult mvcResult = mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"artur.ciemiorek@gmail.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        )
                .andExpect(status().is(200))
                .andExpect(content().json("{\n" +
                        "  \"responseMsg\": \"Poprawnie utorzono konto użytkownika.\",\n" +
                        "  \"status\": \"SUCCESS\",\n" +
                        "  \"accountId\": 16\n" +
                        "}"))
                .andReturn();
        //getting back account to know Id
        ObjectMapper objectMapper = new ObjectMapper();
        CreateUserAccountResponse response = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                CreateUserAccountResponse.class
        );
        //find account by id to check if existing
        Optional<UserAccount> optionalUserAccount = userAccountRepository.findById(response.getAccountId());
        Assert.assertTrue(optionalUserAccount.isPresent());
    }

    @Test
    public void dIfCreateAccountRequestContainsAlreadyExistingEmailAddressShouldReturnHttpCode409AndErrorMsg() throws Exception {

        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"dddd@gmail.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        );
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"dddd@gmail.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        )
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR004\",\n" +
                        "  \"errorMsg\": \"Konto o podanym adresie e-mail już istnieje.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));


    }




    @Test
    public void eIfDeleteAccountRequestContainsCorrectAddressEmailShouldReturnHttpCode200AndDeleteAccount() throws Exception {
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"TO@DELETE.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}")

        );
        mockMvc.perform(
                delete("/user-account/delete")
                        .param("email", "TO@DELETE.com"))
                .andExpect(status().is(200));

        List<UserAccount> userAccountList = userAccountRepository.findByOwnerEmail("TO@DELETE.com");
        Assert.assertTrue(userAccountList.isEmpty());

    }

    @Test
    public void fIfDeleteAccountRequestContainsWrongAddressEmailShouldReturnHttpCode409AndErrorMsg() throws Exception {
        mockMvc.perform(
                delete("/user-account/delete")
                        .param("email", "TODELETE.com"))
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR014\",\n" +
                        "  \"errorMsg\": \"Nie ma konta z takim adresem email.\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));
    }




    @Test
    public void gIfGetAccountBalanceRequestContainsIdWhichNotExistShouldReturnHttpCode409AndErrorMsg() throws Exception {
        mockMvc.perform(
                get("/user-account/balance")
                        .param("userID", "99"))
                .andExpect(status().is(409))
                .andExpect(content().json("{\n" +
                        "  \"errorCode\": \"ERR006\",\n" +
                        "  \"errorMsg\": \"Konto użytkownika o danym id nie istnieje,\",\n" +
                        "  \"status\": \"ERROR\"\n" +
                        "}"));
    }

    @Test
    public void hIfGetAccountBalanceRequestContainsIdWhichExistShouldReturnHttpCode200AndReturnRightBalance() throws Exception {
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"artur.ciemiorek@gmail.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}"));
        mockMvc.perform(
                put("/user-account/{accountId}/recharge", 16)
                        .param("amount", "45"));


        MvcResult mvcResult = mockMvc.perform(
                get("/user-account/balance")
                        .param("userID", "16"))
                .andExpect(status().is(200))
                .andExpect(content().string(Matchers.containsString("Poprawie zwrocone informacje o środkach.")))
                .andExpect(content().string(Matchers.containsString("balance")))
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        BalanceResponse balanceResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BalanceResponse.class);
        Assert.assertTrue(balanceResponse.getBalance().floatValue() == 45f);

    }





    @Test
    public void iIfPutAddressEmailRequestContainsCorrectDataShouldReturnHttpCode200AndChangeAddressEmail() throws Exception {
        mockMvc.perform(
                post("/user-account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"ownerAge\": 25,\n" +
                                "  \"ownerEmail\": \"artur.ciemiorek@gmail.com\",\n" +
                                "  \"ownerUsername\": \"Artur\"\n" +
                                "}"));
        mockMvc.perform(
                put("/user-account/{accountEmail}/email", "artur.ciemiorek@gmail.com")
                        .param("emailToReplace", "ciemiorek.artur@gmail.com"))
                .andExpect(status().is(200))
                .andReturn();

        List<UserAccount> userAccountListByOldEmail = userAccountRepository.findByOwnerEmail("artur.ciemiorek@gmail.com");
        Assert.assertTrue(userAccountListByOldEmail.isEmpty());

        List<UserAccount> userAccountListByNewEmail = userAccountRepository.findByOwnerEmail("ciemiorek.artur@gmail.com");
        Assert.assertFalse(userAccountListByNewEmail.isEmpty());




    }


}
