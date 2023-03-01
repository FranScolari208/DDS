package dds2022.grupo1.HuellaDeCarbono.Repositorios;

import dds2022.grupo1.HuellaDeCarbono.entidades.Sector.SectorTerritorial;

public class SectorTerritorialRepositorio extends RepositorioBase<SectorTerritorial> {
    private static SectorTerritorialRepositorio instancia = new SectorTerritorialRepositorio();

    public SectorTerritorialRepositorio() {
        super(SectorTerritorial.class);
    }

    public static SectorTerritorialRepositorio getInstancia() {
        return instancia;
    }

    @Override
    protected SectorTerritorial handleSaveDuplicated(SectorTerritorial obj) {
        return getBy("nombre", obj.getNombre());
    }

}
