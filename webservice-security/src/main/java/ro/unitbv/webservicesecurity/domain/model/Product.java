package ro.unitbv.webservicesecurity.domain.model;

import java.util.UUID;

public record Product(UUID id, String name, String description) {
}
