package com.changas.service;

import com.changas.dto.auth.LoginRequest;
import com.changas.dto.auth.SignupRequest;
import com.changas.exceptions.customer.CustomerAlreadyRegisteredException;
import com.changas.exceptions.customer.CustomerAuthenticationException;
import com.changas.model.Customer;
import com.changas.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;
    private Customer customer;
    private SignupRequest signupRequest;

    @BeforeEach
    void setup() {

        customer = createTestCustomer();
        signupRequest = fromCustomerToSignupRequest(customer);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("A successful signup saves a new customer")
    void signUpSuccessSavesANewCustomerTest() {
        SignupRequest request = new SignupRequest("test@example.com", "https://example.com/photo.jpg", "Test User", "password");
        when(customerRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        assertDoesNotThrow(() -> authService.signup(request));
        verify(customerRepository).save(any(Customer.class));

    }

    @Test
    @DisplayName("Attempting to signup with an already taken email throws an authentication exception")
    void signUpWithAlreadyTakenEmailThrowsAnAuthenticationException() {
        when(customerRepository.findByEmail(any())).thenReturn(Optional.of(customer));
        Exception exception = assertThrows(CustomerAlreadyRegisteredException.class, () -> authService.signup(signupRequest));
        assertEquals("User already registered with email: " + customer.getEmail(), exception.getMessage());
    }

    @Test
    @DisplayName("A successful login returns an access token")
    void loginSuccessTest() throws CustomerAuthenticationException {
        LoginRequest loginRequest = fromCustomerToLoginRequest(customer);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(customerRepository.findByEmail("user@example.com")).thenReturn(Optional.of(customer));
        when(authentication.getName()).thenReturn("user@example.com");

        String token;
        token = authService.login(loginRequest);

        assertNotNull(token);
    }

    @Test
    @DisplayName("Attempting to login with wrong credentials throws an authentication exception")
    void loginWithBadCredentialsTest() {
        LoginRequest loginRequest = fromCustomerToLoginRequest(customer);
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        Exception exception = assertThrows(CustomerAuthenticationException.class, () -> authService.login(loginRequest));
        assertEquals("Failed to retrieve user on login", exception.getMessage());
    }

    @Test
    @DisplayName("Can retrieve the current logged in user")
    void retrieveCurrentLoggedInUserTest() {
        // Mocks for Authentication and SecurityContext
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Expected output from mocks
        String customerEmail = "test@example.com";
        when(authentication.getName()).thenReturn(customerEmail);

        Customer expectedCustomer = Customer.builder().email(customerEmail).build();
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(expectedCustomer));

        // Exec
        Optional<Customer> result = authService.getCustomerLoggedIn();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(customerEmail, result.get().getEmail());
        verify(customerRepository).findByEmail(customerEmail);

        // Clean up
        SecurityContextHolder.clearContext();
    }

    private Customer createTestCustomer() {
        return Customer.builder().name("Pepe").email("pepe@email.com").password("password").photoUrl("photoUrl").posts(new HashSet<>()).hirings(new HashSet<>()).build();
    }

    private SignupRequest fromCustomerToSignupRequest(Customer customer) {
        return new SignupRequest(customer.getName(), customer.getPhotoUrl(), customer.getEmail(), customer.getPassword());
    }

    private LoginRequest fromCustomerToLoginRequest(Customer customer) {
        return new LoginRequest(customer.getEmail(), customer.getPassword());
    }


}
