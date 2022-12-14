package it.unipi.dsmt.microservices.msbeer.dto.base;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class ResponseDTO <T> {
    @NonNull
    private T data;
}
