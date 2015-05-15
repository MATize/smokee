package at.mse.walchhofer.demo.presentation.rezepte;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import at.mse.walchhofer.demo.business.bilder.boundary.BildService;
import at.mse.walchhofer.demo.business.bilder.entity.Bild;
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
    
    @EJB
    BildService bildService;
    
    public String create() {
        Calendar now = Calendar.getInstance();
        
        Rezept rezept2create = new Rezept();
        rezept2create.setTitel(viewModel.getName());
        rezept2create.setAnleitung(viewModel.getBeschreibung());
        rezept2create.setAutor(viewModel.getAutor());
        rezept2create.setErstelltAm(now);
        
        
        Rezept created = rezeptService.create(rezept2create);
        
        Bild bild2create = new Bild();
        String fileName = Base64.getEncoder().encodeToString(viewModel.getFile().getName().getBytes());
        bild2create.setFileName(fileName);
        bild2create.setMimeType(viewModel.getFile().getContentType());
        bild2create.setSize(viewModel.getFile().getSize());
        bild2create.setFile(new byte[(int)viewModel.getFile().getSize()]);
        try {
            //not the best idea, should first check the size and read buffered
            new DataInputStream(viewModel.getFile().getInputStream()).readFully(bild2create.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bild2create.setErstelltAm(now);
        bild2create.setRezept(created);
        
        bildService.create(bild2create);
        
        if(created != null) {
            return navigation.getUebersicht();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rezept konnte nicht gespeichert werden!", ""));
            return "";
        }
    }
    
}
