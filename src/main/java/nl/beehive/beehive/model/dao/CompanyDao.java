package nl.beehive.beehive.model.dao;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyDao extends CrudRepository<Company, Integer> {
    List<Company> findCompanyByKvk(int kvk);
    //List<Company> findCompaniesByKvk(int kvk);
    List<Company> findCompaniesBySector(String sector);
    Company findCompanyByCompanyId(int number);
}
