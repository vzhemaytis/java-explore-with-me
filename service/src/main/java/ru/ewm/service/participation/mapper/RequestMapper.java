package ru.ewm.service.participation.mapper;

import ru.ewm.service.participation.dto.ParticipationRequestDto;
import ru.ewm.service.participation.model.ParticipationRequest;

public class RequestMapper {
    private RequestMapper() {
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }
}
