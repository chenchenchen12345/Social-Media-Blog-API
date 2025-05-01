package Service;

import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account registerUser(Account account) {
        try {
            if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
                return null;
            }

            if (account.getPassword() == null || account.getPassword().length() < 4) {
                System.out.println("Password must be at least 4 characters long.");
                return null;
            }

            if (accountDAO.usernameExists(account.getUsername())) {
                System.out.println("Username already exists.");
                return null;
            }

            return accountDAO.createAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account loginUser(Account account) {
        try {
            Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
            if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
                return existingAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountById(int id) throws SQLException {
        return accountDAO.getAccountById(id);
    }
}









