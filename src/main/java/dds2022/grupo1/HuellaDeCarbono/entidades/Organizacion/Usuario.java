package dds2022.grupo1.HuellaDeCarbono.entidades.Organizacion;

import dds2022.grupo1.HuellaDeCarbono.Persistente;

import javax.persistence.*;

@Entity
@NamedQuery(query = "Select s from Usuario s where s.email = :emailUser", name = "find usuario id")
@NamedQuery(query = "Select s.contrasenia from Usuario s where s.email = :emailUser", name = "find usuario contrasenia")
@NamedQuery(query = "Select org from Usuario s join MiembroPorSector m on m.miembro=s.miembro join Sector sec on sec.id=m.sector join Organizacion org on org.id=sec.organizacion where s.email = :emailUser", name = "find usuario organizaciones")

@Table
public class Usuario extends Persistente {

    @Column
    protected String contrasenia;
    @Column
    public String email;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "miembro_id", referencedColumnName = "id")
    public Miembro miembro;

    public Usuario(String email) {
        this.email = email;
    }

    public Usuario() {

    }

    public Usuario(String contrasenia, String email, Miembro miembro) {
        this.contrasenia = contrasenia;
        this.email = email;
        this.miembro = miembro;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public String getEmail() {
        return email;
    }

    public String setEmail(String emailNuevo) {
        return email = emailNuevo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
