package startspring.common.codes;

import lombok.Getter;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리
 * Custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성
 *
 */
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */

    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "Server-Error", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "Missing-Body", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "Invalid-type", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "Missing-RequestParam","Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "I/O Exception","I/O Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "Forbidden","Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "Not Found Resource", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "Null", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "Validation-Invalid","handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "Not Header", "Header에 데이터가 존재하지 않는 경우 "),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "Sever-Error Global","Internal Server Error Exception"),

    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // Transaction Insert Error
    INSERT_ERROR(200, "Transaction-Error", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(200, "Update-Error", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "Delete-Error", "Delete Transaction Error Exception");


    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;


    ErrorCode(int status, String divisionCode, String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
