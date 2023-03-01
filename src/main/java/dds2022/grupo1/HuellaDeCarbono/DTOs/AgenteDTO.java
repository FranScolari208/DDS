package dds2022.grupo1.HuellaDeCarbono.DTOs;

public class AgenteDTO {
    private int id;
    private String nombre;
    private String telefono;
    private String email;
    private int sector_id;

    public AgenteDTO(int id, String nombre, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public AgenteDTO(String nombre, String telefono, String email) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public int getSector(){
        return sector_id;
    }

    public void setSector(int id) {
        this.sector_id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }
}
