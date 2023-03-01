package dds2022.grupo1.HuellaDeCarbono;

import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.Parada;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TipoTransporte;
import dds2022.grupo1.HuellaDeCarbono.entidades.Transporte.TransportePublico;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Tramo;
import dds2022.grupo1.HuellaDeCarbono.entidades.Trayecto.Trayecto;
import dds2022.grupo1.HuellaDeCarbono.entidades.misc.Ubicacion;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HuellaDeCarbonoApplication {

	public static void main(String[] args) {

		//SpringApplication.run(HuellaDeCarbonoApplication.class, args);

		System.out.println("**********************************************************");
		System.out.println("**************BIENVENIDO A HUELLA DE CARBONO**************");
		System.out.println("**********************************************************");

		Ubicacion lomasDeZamora = new Ubicacion("Lomas De Zamora 600", 400f,500f);
		Ubicacion lanus = new Ubicacion("Lanus 500", 410f,510f);
		Ubicacion palermo = new Ubicacion("Thames",300.6f,550.2f);
		Ubicacion obelisco = new Ubicacion("Av 9 de Julio 1000", 700f,800f);
		Ubicacion canning = new Ubicacion("Sargento Cabral 3450", 300f, 400f);

		List<Tramo> tramos = new ArrayList<>();

		List<Parada> paradas = new ArrayList<>();
		TipoTransporte colectivo = new TipoTransporte("Colectivo");
		TransportePublico colectivo107 = new TransportePublico (colectivo,paradas,"107");



		Tramo tramo1 = new Tramo(lomasDeZamora, canning); // distancia = 141.4213562
		Tramo tramo2 = new Tramo(canning, lomasDeZamora, palermo); // distancia = 150.2011984
		Tramo tramo3 = new Tramo(palermo, canning, obelisco); // distancia = 471.0842812
		Tramo tramo4 = new Tramo(obelisco, palermo, lanus); // distancia = 410.1219331
		Tramo tramo5 = new Tramo(canning, lomasDeZamora); // distancia = 141.4213562
		Tramo tramo6 = new Tramo(obelisco, obelisco); // distancia = 0

		Trayecto trayecto1 = new Trayecto(lomasDeZamora,lanus,colectivo107,tramos, 2);

		//Trayecto de lomasDeZamora a lanus
		//Tramos: lomasdeZamora-canning .. canning-palermo .. palermo-obelisco .. obelisco-lanus
		tramos.add(tramo1); //lomasdeZamora-canning
		tramos.add(tramo2); //canning-palermo
		tramos.add(tramo3); //palermo-obelisco
		tramos.add(tramo4); //obelisco-lanus

		float distanciaTrayecto = trayecto1.calcularDistancia();

		System.out.println("Distancia: "+distanciaTrayecto);

	}


}
