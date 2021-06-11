package nl.beehive.beehive.service;

import nl.beehive.beehive.model.Account;
import nl.beehive.beehive.model.Business;
import nl.beehive.beehive.model.Company;
import nl.beehive.beehive.model.dao.AccountDao;
import nl.beehive.beehive.model.dao.BusinessDao;
import nl.beehive.beehive.model.dao.CompanyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

import static java.math.RoundingMode.CEILING;
import static java.math.RoundingMode.HALF_EVEN;

@Service
public class ChiefService {

    private final AccountDao accountDao;
    private final CompanyDao companyDao;
    private final BusinessDao businessDao;

    public ChiefService(AccountDao accountDao, CompanyDao companyDao, BusinessDao businessDao) {
        this.accountDao = accountDao;
        this.companyDao = companyDao;
        this.businessDao = businessDao;
    }

    //Methode om de top 10 klanten te vinden
    public List<Account> getTop10Klanten(String accountType) {

        //Query alle zakelijke rekeningen
        List<Account> allAccounts = accountDao.findAccountsByType(accountType);

        //Sorteer alle zakelijke rekeningen op saldo
        allAccounts.sort(Comparator.comparing(Account::getBalance).reversed());

        //Geef de eerste 10 resultaten terug
        return allAccounts.size() < 10 ? allAccounts : allAccounts.subList(0, 10);
    }

    //Methode om de gemiddelde saldo's van bedrijven per sector te krijgen
    public List<String> getGemideldeSaldoPerSector() {

        //Maak een lege lijst voor de resultaten
        List<String> gemiddeldeSaldoPerSector = new ArrayList<>();

        for (BusinessSector sector : BusinessSector.values()) {
            BigDecimal totaalSaldo = new BigDecimal((0.00));

            List<Company> bedrijvenPerSector = companyDao.findCompaniesBySector(sector.label);

            if (bedrijvenPerSector.size() > 0) {
                for (Company company : bedrijvenPerSector) {
                    List<Business> accountsPerBedrijf = businessDao.findAllByCompany(company);
                    for (Business account : accountsPerBedrijf) {
                        totaalSaldo = totaalSaldo.add(account.getBalance());
                    }
                }

                gemiddeldeSaldoPerSector.add(sector + "," + balanceToString(totaalSaldo.divide(new BigDecimal(bedrijvenPerSector.size()), CEILING)));
            }
        }

        return gemiddeldeSaldoPerSector;
    }


    //Methode om de top 10 rekeningen te krijgen op basis van transactie volume
    public Map<String,Integer> getTopRekeningenVolume() {

        //Lege "lijst" aanmaken voor de resultaten
        Map<String, Integer> topRekeningenVolume = new LinkedHashMap<>();

        //Query alle zakelijke rekeningen
        List<Business> zakelijkAccounts = businessDao.findAllByType( "BUSINESS_CH");
        zakelijkAccounts.addAll(businessDao.findAllByType("BUSINESS_SV"));

        //Sorteer de rekeningen op transactie volume
        zakelijkAccounts.sort((b1, b2) -> (b2.getInTransactions().size() + b2.getOutTransactions().size()) -
                    (b1.getInTransactions().size() + b1.getOutTransactions().size()));


        //Format voor de resultaten
        if (zakelijkAccounts.size() > 10) {
            zakelijkAccounts.subList(0, 10).forEach(e -> topRekeningenVolume.put(e.getCompany().getCompanyName(), e.getInTransactions().size() + e.getOutTransactions().size()));
        } else {
            zakelijkAccounts.forEach(e -> topRekeningenVolume.put(e.getCompany().getCompanyName(), e.getInTransactions().size() + e.getOutTransactions().size()));
        }

        return topRekeningenVolume;
    }

    public String balanceToString(BigDecimal balance) {
        Locale netherlands = new Locale("nl", "NL");
        NumberFormat dutchMoneyformat = NumberFormat.getCurrencyInstance(netherlands);
        return dutchMoneyformat.format(balance);
    }
}