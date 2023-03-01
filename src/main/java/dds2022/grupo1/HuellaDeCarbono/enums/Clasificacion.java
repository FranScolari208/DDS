package dds2022.grupo1.HuellaDeCarbono.enums;

public enum Clasificacion {

    MINISTERIO("Ministerio"),
    UNIVERSIDAD("Universidad"),
    ESCUELA("Escuela"),
    GUBERNAMENTAL("Del gobierno"),
    SECTOR_PRIMARIO("Empresa Sector Primario"),
    SECTOR_SECUNDARIO("Empresa Sector Secundario"),
    CAPITAL_ABIERTO("Empresa de Capital Abierto");
    private String clasificacion;

    Clasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
