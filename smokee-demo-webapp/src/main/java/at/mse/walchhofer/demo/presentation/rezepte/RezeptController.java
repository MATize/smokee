package at.mse.walchhofer.demo.presentation.rezepte;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.mse.walchhofer.demo.business.rezepte.boundary.RezeptService;
import at.mse.walchhofer.demo.business.rezepte.entity.Rezept;
import at.mse.walchhofer.demo.presentation.navigation.NavigationController;

@Named("rezeptCtrl")
@RequestScoped
public class RezeptController {

    @Inject
    RezeptViewModel viewModel;
    
    @Inject
    NavigationController navigation;
    
    @Inject
    FacesContext facesContext;
    
    @EJB
    RezeptService rezeptService;
    
    public String create() {
        Rezept rezept2create = new Rezept();
        rezept2create.setTitel(viewModel.getName());
        rezept2create.setAnleitung(viewModel.getBeschreibung());
        Rezept created = rezeptService.create(rezept2create);
        if(created != null) {
            return navigation.getUebersicht();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rezept konnte nicht gespeichert werden!", ""));
            return "";
        }
    }
    
}
