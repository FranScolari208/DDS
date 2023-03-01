package Spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;
public enum IndexHelper implements Helper<Integer> {

    increment{
        @Override
        public CharSequence apply(Integer arg0, Options arg1) throws IOException {
            Integer indice = arg0+1;
            return indice.toString();
        }
    }
}