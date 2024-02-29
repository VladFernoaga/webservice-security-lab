package ro.unitbv.webservicesecurity.dto;

import jakarta.validation.constraints.NotNull;

public record ProductRequestDto(@NotNull String name,  String description) {

}
