package ro.unitbv.webservicesecurity.dto.jwt;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(@NotNull String username, @NotNull String password) {
}
