package authorization.project.delegate;

import authorization.project.delegate.impl.AuthorizerDelegate;
import authorization.project.delegate.impl.TransactionDelegate;
import authorization.project.mapper.AccountDtoMapper;
import authorization.project.persistence.entity.AccountEntity;
import authorization.project.service.impl.AccountService;
import authorization.project.dto.response.ResponseDto;
import authorization.project.mapper.ResponseDtoMapper;
import authorization.project.mapper.TransactionDtoMapper;
import authorization.project.persistence.repository.IAccountRepository;
import authorization.project.service.contract.ITransactionService;
import authorization.project.service.impl.StandardReaderService;
import authorization.project.service.impl.StandardWriterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit and integration test for {@link AuthorizerDelegate}
 *
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
public class AuthorizerDelegateTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    /**
     * Mocks for transitive dependencies (2-level)
     */
    @Mock
    private IAccountRepository accountRepository;
    @Mock
    private BufferedReader bufferedReader;
    @Mock
    private ITransactionService transactionService;
    /**
     * Service Instances
     */
    @InjectMocks
    private ResponseDtoMapper responseDtoMapper;
    @InjectMocks
    private AccountDtoMapper accountDtoMapper;
    @InjectMocks
    private TransactionDtoMapper transactionDtoMapper;
    @InjectMocks
    private StandardReaderService standardReaderService;
    @InjectMocks
    private AccountService accountService;
    @InjectMocks
    private TransactionDelegate transactionDelegate;
    @InjectMocks
    private StandardWriterService standardWriterService;
    /**
     * Class under test
     */
    private AuthorizerDelegate authorizerDelegate;

    @BeforeEach
    public void setUp() {
        authorizerDelegate = new AuthorizerDelegate(
                standardReaderService, accountDtoMapper,
                transactionDtoMapper, accountService,
                transactionDelegate, responseDtoMapper, standardWriterService);
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void contextLoads() {
        assertNotNull(accountRepository);
        assertNotNull(bufferedReader);
        assertNotNull(transactionService);
        assertNotNull(outputStreamCaptor);
        assertNotNull(standardReaderService);
        assertNotNull(accountDtoMapper);
        assertNotNull(transactionDtoMapper);
        assertNotNull(accountService);
        assertNotNull(transactionDelegate);
        assertNotNull(responseDtoMapper);
        assertNotNull(standardWriterService);
    }

    /**
     * <b>Given<b/> an Account Json Parsed Object
     * <b>When<b/> processRequest method is called
     * <b>Then<b/> should perform all expected operations on given request
     */
    @Test
    public void givenAccountParsedObject_whenProcessRequestCalled_thenShouldPerformOperations()
            throws IOException {
        String jsonObject = "{\"account\": {\"active-card\": true, \"available-limit\": 100}}";
        when(bufferedReader.readLine()).thenReturn(jsonObject);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();

        ResponseDto responseDto = authorizerDelegate.processRequest(retrievedObjects.get().get(0));
        verify(accountRepository, times(1)).findFirstByIdIsNotNull();
        verify(accountRepository, times(1)).save(any());
        assertTrue(responseDto.getViolations().isEmpty());
    }

    /**
     * <b>Given<b/> a Transaction Json Parsed Object
     * <b>When<b/> processRequest method is called
     * <b>Then<b/> should perform all expected operations on given request
     */
    @Test
    public void givenTransactionParsedObject_whenProcessRequestCalled_thenShouldPerformOperations()
            throws IOException {
        String jsonObject = "{\"transaction\": {\"merchant\": \"Burger King\", " +
                "\"amount\": 22, \"time\":\"2019-02-13T13:00:00.000Z\"}}";
        when(bufferedReader.readLine()).thenReturn(jsonObject);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        when(accountRepository.findFirstByIdIsNotNull()).thenReturn(Optional.of(
                AccountEntity.builder().activeCard(true).id(1).availableLimit(200).build()
        ));
        ResponseDto responseDto = authorizerDelegate.processRequest(retrievedObjects.get().get(0));
        assertTrue(responseDto.getViolations().isEmpty());
    }

    /**
     * <b>When<b/> performActions method is called
     * <b>Then<b/> should read and process each request Until there is no more object is sdtin
     */
    @Test
    public void whenPerformActionsCalled_shouldReadAndProcessEachRequestUntilNoMoreInReader() throws IOException {
        String jsonObject = "EXIT";
        when(bufferedReader.readLine()).thenReturn(jsonObject);
        authorizerDelegate.performActions();
        assertTrue(outputStreamCaptor.toString().length() > 0);
    }


    /**
     * <b>Given<b/> a Request List of Json Parsed Object
     * <b>When<b/> authorizeRequests method is called
     * <b>Then<b/> process each request
     */
    @Test
    public void givenARequestListJson_whenAuthorizeRequestsMethodCalled_thenShouldProcessEachRequest() throws IOException {
        String jsonObject = "{\"transaction\": {\"merchant\": \"Burger King\", " +
                "\"amount\": 22, \"time\":\"2019-02-13T13:00:00.000Z\"}}";
        when(bufferedReader.readLine()).thenReturn(jsonObject);
        Optional<List<?>> retrievedObjects = standardReaderService.readFromStdin();
        List<ResponseDto> responses = authorizerDelegate.authorizeRequests(retrievedObjects);
        assertFalse(responses.isEmpty());
        assertTrue(responses.size() == 1);
    }

}
