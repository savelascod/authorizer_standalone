package authorization.project;

import authorization.project.delegate.contract.IAuthorizerDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit and integration test cases for {@link AuthorizerApplication}
 */
@ExtendWith(MockitoExtension.class)
class AuthorizerApplicationTest {

    /**
     * Dependency loading
     */
    @Mock
    private IAuthorizerDelegate authorizerDelegate;

    /**
     * Class Under Test
     */
    @InjectMocks
    private AuthorizerApplication authorizerApplication;

    @BeforeEach
    public void setUp() {
        authorizerApplication = new AuthorizerApplication(authorizerDelegate);
    }

    @Test
    public void contextLoads() {
        assertNotNull(authorizerDelegate);
    }

}
