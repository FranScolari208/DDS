package dds2022.grupo1.HuellaDeCarbono.services.Parsers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dds2022.grupo1.HuellaDeCarbono.Interfaces.Parser.Parser;


public abstract class ParserAbs implements Parser{
    protected String nombreArchivo;
    protected BufferedReader buffer;
    protected List<String> lineas = new ArrayList<>();

    public ParserAbs (String nombreArchivo) throws IOException {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public void leerArchivo() throws IOException{
        File file = new File(this.nombreArchivo);
        FileReader fr = new FileReader(file);
        this.buffer = new BufferedReader(fr);
    }
    
    protected void leerLineas() throws IOException {
            String line;
            while ((line = buffer.readLine()) != null) {
                if(line.length() > 0){
                    lineas.add(line);
                }
            }
            buffer.close();
    }

}