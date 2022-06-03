package woowacourse.shoppingcart.ui;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import woowacourse.shoppingcart.dto.ErrorResponse;
import woowacourse.shoppingcart.exception.DuplicateCustomerException;
import woowacourse.shoppingcart.exception.InvalidCustomerException;
import woowacourse.shoppingcart.exception.ShoppingCartException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomerControllerAdvice {

    private static final int INVALID_FORMAT_ERROR_CODE = 1000;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleInvalidRequest(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findAny()
                .orElse("잘못된 형식입니다.");
        return ResponseEntity.badRequest().body(new ErrorResponse(INVALID_FORMAT_ERROR_CODE, message));
    }

    @ExceptionHandler({
            InvalidCustomerException.class,
            DuplicateCustomerException.class
    })
    public ResponseEntity<ErrorResponse> handleShoppingCartException(final ShoppingCartException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}