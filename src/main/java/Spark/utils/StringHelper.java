package Spark.utils;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

public enum StringHelper implements Helper<String> {

    append{
        @Override
        public CharSequence apply(String arg0, Options arg1) throws IOException {
            return arg0+arg1.param(0);
        }
    },

    selected{
        @Override
        public CharSequence apply(String arg0, Options arg1) throws IOException {
            if((arg1.param(0) == null)) return "";
            else {
                if(arg0.equals(arg1.param(0).toString()))
                    return "selected";
                else
                    return "";
            }
        }
    }
}