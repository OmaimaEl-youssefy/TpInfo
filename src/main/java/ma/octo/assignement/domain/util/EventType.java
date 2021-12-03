package ma.octo.assignement.domain.util;

public enum EventType {

    VIREMENT("virement"),
    VERSEMENT("Versement d'argent");

    private final String type;

    EventType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
