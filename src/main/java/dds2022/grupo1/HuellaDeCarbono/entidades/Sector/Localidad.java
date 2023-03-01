package dds2022.grupo1.HuellaDeCarbono.entidades.Sector;


import org.springframework.security.core.Transient;

@Transient
public class Localidad {

        public int id;
        public String nombre;
        public int codPostal;

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getNombre() {
                return nombre;
        }

        public void setNombre(String nombre) {
                this.nombre = nombre;
        }

        public int getCodPostal() {
                return codPostal;
        }

        public void setCodPostal(int codPostal) {
                this.codPostal = codPostal;
        }
}
