package at.mse.walchhofer.demo.presentation.navigation;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named("navigation")
@ApplicationScoped
public class NavigationController {

    private String uebersicht = "/views/overview";
    private String suchen = "/views/search";
    private String neu = "/views/new";

    
    @Inject
    FacesContext facesContext;
    
    public String isActive(String navViewId) {
        String viewId = facesContext.getViewRoot().getViewId();
        if(viewId.startsWith(navViewId)) {
            return "active";
        } else {
            return "";
        }
    }
    
    public String getUebersicht() {
        return uebersicht;
    }


    public String getSuchen() {
        return suchen;
    }


    public String getNeu() {
        return neu;
    }
    
}
