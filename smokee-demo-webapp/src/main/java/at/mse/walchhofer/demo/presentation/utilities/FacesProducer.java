package at.mse.walchhofer.demo.presentation.utilities;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

public class FacesProducer {
    
    @Produces
    @RequestScoped
    FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
