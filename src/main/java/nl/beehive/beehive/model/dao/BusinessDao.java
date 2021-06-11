package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Business;
import nl.beehive.beehive.model.Company;
import nl.beehive.beehive.service.AccountType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusinessDao extends CrudRepository<Business, Company> {

    List<Business> findBusinessesByCompany(Company company);
    Business findByAccountId(int accountId);
    List<Business> findBusinessByCompany_Kvk(int kvk);
    List<Business> findAllByType(String type);
    List<Business> findAllByCompany(Company company);
}
