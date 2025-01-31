package com.softwaremind.odolczykd.recruitment.shared.rest;

import java.util.List;

public record ErrorMessagesResponse(List<String> errors) {
    public static ErrorMessagesResponse create(List<String> errors) {
        return new ErrorMessagesResponse(errors);
    }
}
