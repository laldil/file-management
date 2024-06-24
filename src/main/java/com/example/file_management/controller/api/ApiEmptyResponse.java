package com.example.file_management.controller.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author aldi
 * @since 18.06.2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiEmptyResponse extends ApiResponse {
    public ApiEmptyResponse() {
        setSuccess(true);
    }

    public static ApiEmptyResponse create() {
        return new ApiEmptyResponse();
    }

    public static ApiEmptyResponse failed(String msg) {
        var response = new ApiEmptyResponse();
        response.setSuccess(false);
        response.setMsg(msg);
        return response;
    }
}
