package authorization.project.delegate.impl;


import authorization.project.mapper.AccountDtoMapper;
import authorization.project.service.contract.IReaderService;
import authorization.project.delegate.contract.IAuthorizerDelegate;
import authorization.project.delegate.contract.ITransactionDelegate;
import authorization.project.dto.domain.AccountDto;
import authorization.project.dto.domain.TransactionDto;
import authorization.project.dto.response.ResponseDto;
import authorization.project.exception.BusinessException;
import authorization.project.mapper.ResponseDtoMapper;
import authorization.project.mapper.TransactionDtoMapper;
import authorization.project.service.contract.IAccountService;
import authorization.project.service.contract.IWriterService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.vavr.API.Try;

/**
 * Service Delegate to perform authorization operations
 *
 * @version 1.0
 * @since 1.0
 */
public class AuthorizerDelegate implements IAuthorizerDelegate {

    /**
     * Service instance of {@link IReaderService}
     */
    private final IReaderService readerService;

    /**
     * Mapper to convert LinkedHashMap objects to {@link AccountDto}
     */
    private final AccountDtoMapper accountDtoMapper;

    /**
     * Mapper to convert LinkedHashMap objects to {@link TransactionDto}
     */
    private final TransactionDtoMapper transactionDtoMapper;

    /**
     * Mapper to convert {@link ResponseDto} to String
     */
    private final ResponseDtoMapper responseDtoMapper;

    /**
     * Service instance of {@link IAccountService}
     */
    private final IAccountService accountService;

    /**
     * Service instance of {@link IWriterService}
     */
    private final IWriterService writerService;

    /**
     * Delegate instance of {@link ITransactionDelegate}
     */
    private final ITransactionDelegate transactionDelegate;

    /**
     * Constructor with autowired dependencies
     */
    public AuthorizerDelegate(
            IReaderService readerService,
            AccountDtoMapper accountDtoMapper,
            TransactionDtoMapper transactionDtoMapper,
            IAccountService accountService,
            ITransactionDelegate transactionDelegate,
            ResponseDtoMapper responseDtoMapper,
            IWriterService writerService) {
        this.readerService = readerService;
        this.accountDtoMapper = accountDtoMapper;
        this.transactionDtoMapper = transactionDtoMapper;
        this.accountService = accountService;
        this.transactionDelegate = transactionDelegate;
        this.responseDtoMapper = responseDtoMapper;
        this.writerService = writerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performActions() {
        writerService.writeWelcomeMessage();
        boolean shouldProcessInput;
        do {
            Optional<List<?>> finalReaderInput = readerService.readFromStdin();
            writerService.writeResponse(Try(() ->
                    responseDtoMapper.responseListDtoToString(
                            authorizeRequests(finalReaderInput))).toEither()
                    .fold(exception -> StringUtils.EMPTY, string -> string));
            shouldProcessInput = finalReaderInput.isPresent();
        } while (shouldProcessInput);
        writerService.writeExitMessage();
    }

    /**
     * {@inheritDoc}
     *
     * @param requestList
     */
    @Override
    public List<ResponseDto> authorizeRequests(Optional<List<? extends Object>> requestList) {
        List<ResponseDto> responses = new ArrayList<>();
        requestList.ifPresent(objects -> objects.forEach(object -> responses.add(processRequest(object))));
        return responses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto processRequest(Object jsonParsedObject) {
        List<String> businessErrors = Try(() -> {
            List<BusinessException> errorList = new ArrayList<>();
            Optional<AccountDto> accountCreation =
                    accountDtoMapper.objectToAccountCreationRequestDto(jsonParsedObject);
            Optional<TransactionDto> transactionAuthorization =
                    transactionDtoMapper.objectToTransactionAuthorizationRequestDto(jsonParsedObject);
            if (accountCreation.isPresent()) errorList =
                    accountService.createAccount(accountCreation.get());
            if (transactionAuthorization.isPresent()) errorList =
                    transactionDelegate.performTransactionAuthorization(transactionAuthorization.get());
            return errorList.stream().map(BusinessException::getMessage).collect(Collectors.toList());
        }).toEither().fold(exception -> new ArrayList<>(), errors -> errors);
        return ResponseDto.builder().account(accountService.getAccountStatus().orElse(new AccountDto()))
                .violations(businessErrors).build();
    }

}
