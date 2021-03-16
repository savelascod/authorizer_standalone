package authorization.project;

import authorization.project.delegate.impl.TransactionDelegate;
import authorization.project.mapper.AccountDtoMapper;
import authorization.project.service.impl.AccountService;
import authorization.project.delegate.contract.IAuthorizerDelegate;
import authorization.project.delegate.impl.AuthorizerDelegate;
import authorization.project.mapper.ResponseDtoMapper;
import authorization.project.mapper.TransactionDtoMapper;
import authorization.project.persistence.impl.CustomAccountRepository;
import authorization.project.persistence.impl.CustomTransactionRepository;
import authorization.project.service.impl.StandardReaderService;
import authorization.project.service.impl.StandardWriterService;
import authorization.project.service.impl.TransactionService;

/**
 * Spring Boot Application
 */
public class AuthorizerApplication {

    /**
     * instance of
     */
    private final IAuthorizerDelegate authorizerDelegate;

    /**
     * Constructor with autowired dependencies
     */
    public AuthorizerApplication(IAuthorizerDelegate authorizerDelegate) {
        this.authorizerDelegate = authorizerDelegate;
    }

    /**
     * Spring boot initializer method
     *
     * @param args -> Initialization arguments
     */
    public static void main(String[] args) {
        AccountService accountService = new AccountService(new CustomAccountRepository());
        TransactionService transactionService = new TransactionService(new CustomTransactionRepository());

        AuthorizerApplication authorizerApplication =
                new AuthorizerApplication(new AuthorizerDelegate(
                        new StandardReaderService(),
                        new AccountDtoMapper(),
                        new TransactionDtoMapper(),
                        accountService,
                        new TransactionDelegate(transactionService, accountService),
                        new ResponseDtoMapper(),
                        new StandardWriterService()));

        authorizerApplication.run(args);
    }

    /**
     * Methods that execute the application
     *
     * @param args -> Command line arguments
     */
    public void run(String... args) {
        authorizerDelegate.performActions();
    }

}
