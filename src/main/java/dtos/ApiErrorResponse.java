package dtos;

import java.util.Map;

public record ApiErrorResponse(String code, String message, Map<String, String> fieldErrors) {
    public static ApiErrorResponse of(String code, String message) {
        return new ApiErrorResponse(code, message, null);
    }

    public static ApiErrorResponse validation(String message, Map<String, String> fieldErrors) {
        return new ApiErrorResponse("VALIDATION_ERROR", message, fieldErrors);
    }
}

