package nl.beehive.beehive.service;

import nl.beehive.beehive.model.*;
import nl.beehive.beehive.model.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

@Service
public class TestDataService {
    private HashMap<String, Account> accountMap = new HashMap<>();
    private HashMap<String, User> userMap = new HashMap<>();
    private HashMap<String, Company> companyMap = new HashMap<>();

    @Autowired
    private CompanyDao companydao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private AccountManagerDao accountManagerDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private CustomerDao customerdao;

    @Autowired
    private ChiefBusinessDao chiefbusinessdao;

    @Autowired
    private ChiefRetailDao chiefretaildao;

    //Methode om de DB te vullen met testdata
    public void insertData() {
        System.out.println("Inserting users...");
        insertUsers("src/main/originaldata/userSample.csv");
        System.out.println("Inserting companies...");
        insertCompany("src/main/originaldata/companySample.csv");
        System.out.println("Inserting accounts...");
        insertAccounts("src/main/originaldata/accountsSample.csv",
                "src/main/originaldata/accountscustomersSample.csv");
        System.out.println("Inserting transactions...");
        insertTransaction("src/main/originaldata/transactionsSample.csv");
        System.out.println("Bringing back our number one customer");
        ourNumberOneCustomer();
        System.out.println("finished filling database.");
    }

    // lees file en schrijf bedrijven naar dbs
    private void insertCompany(String filePath){
        int counter =0;
        Company tempCompany;
        String[] lineCSV;
        Scanner sc = scanner(filePath);
        // skip header
        sc.nextLine();
        // maak bedrijven aan en sla op
        //while (sc.hasNextLine()&&counter++<41) {
        while (sc.hasNextLine()) {
                //header file: kvkNr,deptNr,name,sector,vatNr,accountManager
            lineCSV = sc.nextLine().split(",");
            tempCompany = new Company(Integer.valueOf(lineCSV[0]),lineCSV[2],lineCSV[3]);
            //sla bedrijf op in database
            companyMap.put(lineCSV[0],tempCompany);
            companydao.save(tempCompany);
        }
    }

    // lees file en schrijf gebruikers naar dbs
    private void insertUsers(String filePath) {
        String role;
        Customer tempCustomer;
        AccountManager tempAccountmanager;
        ChiefRetail tempChiefRetail;
        ChiefBusiness tempChiefBusiness;
        String[] lineCSV;
        Scanner sc = scanner(filePath);
        // skip header
        sc.nextLine();
        while (sc.hasNextLine()) {
            lineCSV = sc.nextLine().split(",");
            role = lineCSV[2];
            // string "null" are entries in user with no Role
            if(role.equals("null")){
                break;
            } else {
                //System.out.println(role);
                RoleUser roles = Enum.valueOf(RoleUser.class, role);
                switch(roles) {
                    case ACCOUNTMANAGER:
                        tempAccountmanager = new AccountManager(lineCSV[1],lineCSV[0],lineCSV[4]);
                        userMap.put(lineCSV[1],tempAccountmanager);
                        accountManagerDao.save(tempAccountmanager);
                        break;
                    case CUSTOMER:
                        // splits huisnummer van straat af
                        String[] splitStreet=lineCSV[6].split(" ");
                        int houseNumber = Integer.parseInt(splitStreet[(splitStreet.length-1)]);
                        String street = makeStreet(splitStreet);
                        //
                        tempCustomer = new Customer(lineCSV[1],lineCSV[0],lineCSV[4],lineCSV[3],lineCSV[13],lineCSV[14],Integer.parseInt(lineCSV[15])
                                ,lineCSV[12],street,houseNumber,"",lineCSV[8],lineCSV[7],lineCSV[10]);
                        userMap.put(lineCSV[1],tempCustomer);
                        customerdao.save(tempCustomer);
                        break;
                    case BUSINESSMANAGER:
                        tempChiefBusiness = new ChiefBusiness("Nathan", "123","Nathan");
                        //tempChiefBusiness = new ChiefBusiness(lineCSV[1],lineCSV[0],lineCSV[4]);
                        userMap.put("Nathan",tempChiefBusiness);
                        chiefbusinessdao.save(tempChiefBusiness);
                        break;
                    case RETAILMANAGER:
                        tempChiefRetail = new ChiefRetail("Daniel", "123", "Daniel");
                        //tempChiefRetail = new ChiefRetail(lineCSV[1],lineCSV[0],lineCSV[4]);
                        userMap.put("Daniel",tempChiefRetail);
                        chiefretaildao.save(tempChiefRetail);
                        break;
                }
            }
        }
    }

   // lees file en schrijf rekeningen naar dbs
    private void insertAccounts(String accountFile,String koppelFile) {
        //counter limits accounts in database
        AccountManager tempAccountmanager;
        Customer tempCustomer;
        Account tempAccount;
        Company tempCompany;
        //Business tempBusiness;
        Account tempBusiness;
        List<String> koppelklanten = new ArrayList<>();
        AccountType typeAccount=null;
        String[] lineCSV;
        // maak scanner object
        Scanner sc = scanner(accountFile);
        // skip header
        sc.nextLine();
        // relatie met customer ophalen
        HashMap<String, List<String>> accountCustomer = getCustAccount(koppelFile);
        // voeg retail,zakelijke en interne rekeningen
        while (sc.hasNextLine()) {
            lineCSV = sc.nextLine().split(",");
            typeAccount =  typeAccount.valueOf(lineCSV[1]);
            tempAccount = new Account(lineCSV[0],lineCSV[1], new BigDecimal(Double.parseDouble(lineCSV[2])), lineCSV[3],1234);
            koppelklanten =accountCustomer.remove(lineCSV[0]);
            if((AccountType.RETAIL_CHECKING ==typeAccount) || (AccountType.RETAIL_SAVING == typeAccount)) {
                //account aanmaken
                //koppel klanten aan rekening
                tempAccount = connectCustomerToAccount(koppelklanten,tempAccount);
                // sla retailrekening op in dbs
                accountMap.put(lineCSV[0],tempAccount);
                accountDao.save(tempAccount);
            } else if ((AccountType.BUSINESS_CH == typeAccount)||(AccountType.BUSINESS_SV == typeAccount)){
                //maak zakelijke rekeningen aan
                //maak instantiehaal data op uit data base
                tempAccountmanager = (AccountManager)userMap.get(lineCSV[5]);
                tempCompany =companyMap.get(lineCSV[4]);
                // maak een zakelijke rekening
                tempBusiness = new Business(tempAccount,tempCompany,tempAccountmanager);
                if (!(koppelklanten==null)) {
                    for (int i = 0; i < koppelklanten.size(); i++) {
                       // tempBusiness.addHolder(customerdao.findCustomerByUsername(koppelklanten.get(i)).get(0));
                        tempBusiness.addHolder((Customer)userMap.get(koppelklanten.get(i)));
                    }
                }
                // sla rekening op in dbs
                accountMap.put(lineCSV[0],tempBusiness);
                accountDao.save(tempBusiness);
            } else {
                //maak interne rekening aan
                //tempAccount = new Account(lineCSV[0],lineCSV[1], Double.valueOf(lineCSV[2]), lineCSV[3],1234);
                accountMap.put(lineCSV[0],tempAccount);
                accountDao.save(tempAccount);
            }
            //counterRet++;
        }
    }

    //Methode om het csv bestand in te lezen
    private Scanner scanner(String filenaam) {
        Scanner sc = null;
        try
        {
            File file = new File(filenaam);
            sc = new Scanner(file);
        } catch(FileNotFoundException s) {
            System.out.println("File does Not Exist Please Try Again: ");
        }
        return sc;
    }

    // Methode om koppeling tussen rekening en accounthouders op te halen
    private HashMap<String, List<String>> getCustAccount(String filepath) {
        HashMap<String, List<String>> accountCustomer = new HashMap<>();
        List<String> values = new ArrayList<>();
        //iban,username,type
        Scanner scAcCu = scanner(filepath);
        scAcCu.nextLine();
        String[] lineCSVCU;
        //initialiseer
        lineCSVCU = scAcCu.nextLine().split(",");
        String key = lineCSVCU[0];
        values.add(lineCSVCU[1]);
        while(scAcCu.hasNextLine()){
            // krijg volgende regel
            lineCSVCU = scAcCu.nextLine().split(",");
            // check of het een andere iban is
            if(!(key.equals(lineCSVCU[0]))){
                // ja:  put() && krijg nieuw entry
                accountCustomer.put(key,values);
                key= lineCSVCU[0];
                values= new ArrayList<>();
            }
            // anders: voeg aan arraylist toe
            values.add(lineCSVCU[1]);
        }
        return accountCustomer;
    }

    // lees file en koppel transactie aan rekeningen
    private void insertTransaction(String filePath) {
        Scanner sc = scanner(filePath);
        // sla header info over
        sc.nextLine();
        // zet transactie in dbs en koppel aan rekeningen
        TransactionToClassToDbs(sc);
    }

    private void TransactionToClassToDbs(Scanner sc){
        String inAccount;
        String outAccount;
        BigDecimal amount;
        Transaction tempTransaction;
        String[] lineCSV;
        String ibanExternal = "";
        // haal data uit scannerobject en maak instanties aan en maak
        while (sc.hasNextLine()) {
            lineCSV = sc.nextLine().split(",");
            if (accountMap.containsKey(lineCSV[0])) {
                inAccount=lineCSV[0];
            } else {
                // als IBAN van externe bank
                ibanExternal = lineCSV[0];
                inAccount="NL09BEEH0000100000";
            }
            // outAccount niet in dbs staat
            if (accountMap.containsKey(lineCSV[1])) {
                outAccount = lineCSV[1];
            } else {
                // als IBAN van externe bank
                ibanExternal = lineCSV[1];
                outAccount ="NL09BEEH0000100000";
            }
            // maak transactie opbject en sla op in
            amount = new BigDecimal(Double.parseDouble(lineCSV[2]));
            tempTransaction = new Transaction(accountMap.get(inAccount), accountMap.get(outAccount),amount, lineCSV[3], lineCSV[4], lineCSV[5]);
            Transaction tempTransactionExternal = new TransactionSenderExternal(tempTransaction,"John Doe", ibanExternal);
            tempTransaction.setDescription("Geen");
            transactionDao.save(tempTransactionExternal);
            accountMap.get(inAccount).addInTransaction(tempTransaction);
            accountMap.get(inAccount).addBalance(amount);
            accountMap.get(outAccount).addInTransaction(tempTransaction);
            accountMap.get(outAccount).subtractBalance(amount);
        }
    }
    private Account connectCustomerToAccount(List<String> koppelklanten,Account tempAccount){
        if (!(koppelklanten==null)) {
            for (int i = 0; i < koppelklanten.size(); i++) {
                tempAccount.addHolder((Customer)userMap.get(koppelklanten.get(i)));
            }
        }
        return tempAccount;
    }

    private void ourNumberOneCustomer(){
        Customer huub = customerdao.findCustomerByUserId(1);
        huub.setUsername("Huub");
        huub.setLastName("Thienen");
        huub.setFirstName("Huub");
        huub.setPassword("inloggen");
        customerdao.save(huub);
    }
    private String makeStreet(String[] splitStreet){
        String street ="";
        for (int i = 0; i <splitStreet.length-2 ; i++) {
            street+=splitStreet[i]+ " ";
        }
        street+=splitStreet[splitStreet.length-2];
        return street;
    }

}
