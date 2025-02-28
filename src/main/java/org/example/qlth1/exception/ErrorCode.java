package org.example.qlth1.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    STUDENT_NOT_FOUND(1009, "No student found", HttpStatus.NOT_FOUND),
    SCORE_NOT_FOUND(1010, "No score found", HttpStatus.NOT_FOUND),
    INVALID_TOKEN(1011, "Invalid token", HttpStatus.UNAUTHORIZED),
    TEACHER_NOT_FOUND(2001, "No teacher found", HttpStatus.NOT_FOUND),
    TEACHER_EXISTED(2002, "The teacher code already exists", HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_FOUND(2003, "No subjects found", HttpStatus.NOT_FOUND),
    SUBJECT_EXISTED(2004, "The subject code already exists", HttpStatus.BAD_REQUEST),
    CLASS_NOT_FOUND(2005, "Class not found", HttpStatus.NOT_FOUND),
    CLASS_EXISTED(2006, "The class code already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(2007, "Role not found", HttpStatus.NOT_FOUND);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
