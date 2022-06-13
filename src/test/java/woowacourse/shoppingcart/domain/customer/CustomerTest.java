package woowacourse.shoppingcart.domain.customer;

import static Fixture.CustomerFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import woowacourse.shoppingcart.domain.customer.password.EncodedPassword;

class CustomerTest {

    @DisplayName("고객을 생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(
                () -> new Customer(MAT_USERNAME, MAT_EMAIL, MAT_ENCODED_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER));
    }

    @DisplayName("고객 생성을 위해 필요한 데이터가 null인 경우 예외를 던진다.")
    @ParameterizedTest
    @MethodSource("generateInvalidCustomer")
    void create_error_null(String username, String email, String password, String address, String phoneNumber) {
        assertThatThrownBy(() -> new Customer(username, email, new EncodedPassword(password), address, phoneNumber))
                .isInstanceOf(NullPointerException.class);
    }

    private static Stream<Arguments> generateInvalidCustomer() {
        return Stream.of(
                Arguments.of(null, YAHO_EMAIL, YAHO_ENCODED_PASSWORD.getValue(), YAHO_ADDRESS, YAHO_PHONE_NUMBER),
                Arguments.of(YAHO_USERNAME, null, YAHO_ENCODED_PASSWORD.getValue(), YAHO_ADDRESS, YAHO_PHONE_NUMBER),
                Arguments.of(YAHO_USERNAME, YAHO_EMAIL, null, YAHO_ADDRESS, YAHO_PHONE_NUMBER),
                Arguments.of(YAHO_USERNAME, YAHO_EMAIL, YAHO_ENCODED_PASSWORD.getValue(), null, YAHO_PHONE_NUMBER),
                Arguments.of(YAHO_USERNAME, YAHO_EMAIL, YAHO_ENCODED_PASSWORD.getValue(), YAHO_ADDRESS, null)
        );
    }

    @DisplayName("username 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"test", "testtesttesttesttesttest", "TESTTEST", "test.test"})
    void create_error_usernameFormat(String username) {
        assertThatThrownBy(() -> new Customer(username, MAT_EMAIL, MAT_ENCODED_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("username 형식에 맞으면 customer가 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"pup-paw", "pup_paw", "123456", "a1b2c3_-"})
    void create_valid_username(String username) {
        assertDoesNotThrow(
                () -> new Customer(username, MAT_EMAIL, MAT_ENCODED_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER));
    }

    @DisplayName("이메일 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"@email.com", "test@", "testemail.com", "test@email", "email"})
    void create_error_emailFormat(String email) {
        assertThatThrownBy(
                () -> new Customer(YAHO_USERNAME, email, YAHO_ENCODED_PASSWORD, YAHO_ADDRESS, YAHO_PHONE_NUMBER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"0000-0000-0000", "-0000-0000", "000-0000", "0000"})
    void create_error_phoneNumberFormat(String phoneNumber) {
        assertThatThrownBy(() -> new Customer(MAT_USERNAME, MAT_EMAIL, MAT_ENCODED_PASSWORD, MAT_ADDRESS, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("customer의 내부 정보를 수정한다.")
    @Test
    void modify() {
        Customer customer = YAHO;

        customer.modify(UPDATE_ADDRESS, UPDATE_PHONE_NUMBER);

        assertAll(() -> {
            assertThat(customer.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(customer.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(customer.getPassword()).isEqualTo(YAHO_ENCODED_PASSWORD);
            assertThat(customer.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(customer.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        });
    }
}