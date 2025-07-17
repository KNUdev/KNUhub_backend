package ua.knu.knudev.knuhubsecurityapi.dto;

import lombok.Builder;

@Builder
public record Tokens(String accessToken, String refreshToken) {
}

