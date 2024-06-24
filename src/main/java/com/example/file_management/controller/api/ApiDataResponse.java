package com.example.file_management.controller.api;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author aldi
 * @since 18.06.2024
 */

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiDataResponse<T> extends ApiResponse {
    private T data;

    public ApiDataResponse(T data) {
        this.data = data;
        setSuccess(true);
    }

    public static <E> ApiDataResponse<E> create(E data) {
        return new ApiDataResponse<>(data);
    }

    public static <E> ApiDataResponse<E> failed(String msg) {
        ApiDataResponse<E> response = new ApiDataResponse<>();
        response.setSuccess(false);
        response.setMsg(msg);
        return response;
    }
}
