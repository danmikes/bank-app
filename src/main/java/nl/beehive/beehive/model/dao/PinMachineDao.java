package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.PinMachine;
import org.springframework.data.repository.CrudRepository;

public interface PinMachineDao extends CrudRepository<PinMachine,Integer> {
    boolean existsPinMachineByPinAccount(Account account);
    PinMachine getPinMachineByPinAccount(Account account);
}
