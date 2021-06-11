package nl.beehive.beehive.controller;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.PinMachine;
import nl.beehive.beehive.model.Transaction;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.TransactionDao;
import nl.beehive.beehive.service.PinMachineService;
import nl.beehive.beehive.service.TransactionScreensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLOutput;

@RestController
public class PinMachineController {

    @Autowired
    PinMachineService pinMachineService;

    @Autowired
    TransactionDao transactionDao;

    @Autowired
    AccountDao accountDao;

    @PostMapping(value = "/pin/beehpinactivation", consumes = "application/json", produces = "application/json")
    public String ActivationRequest(@RequestBody PinMachine pinMachine) {
        return  pinMachineService.procesActivation(pinMachine);
    }

}
