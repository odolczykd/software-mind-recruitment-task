package product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Product category is not valid")
public class InvalidProductCategoryException extends RuntimeException {
    public InvalidProductCategoryException(String category) {
        super("Invalid product category: " + category);
    }
}
