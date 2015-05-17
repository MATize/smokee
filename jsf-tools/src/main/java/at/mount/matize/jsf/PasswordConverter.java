package at.mount.matize.jsf;

import java.nio.charset.Charset;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "passwordConverter")
public class PasswordConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        if (value == null || value.length() == 0) {
            return null;
        } else {
            return new String(value.getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8")).toCharArray();
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value != null) {
            if (value instanceof char[]) {
                return String.valueOf((char[]) value);
            }
        }
        return "";
    }

}
