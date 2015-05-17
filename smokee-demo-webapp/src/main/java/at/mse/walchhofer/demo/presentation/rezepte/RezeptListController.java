package at.mse.walchhofer.demo.presentation.rezepte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import at.mse.walchhofer.demo.business.rezepte.boundary.RezeptService;
import at.mse.walchhofer.demo.business.rezepte.entity.Rezept;

@Named("rezeptListCtrl")
@RequestScoped
public class RezeptListController {

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    @EJB
    RezeptService rezeptService;

    public List<RezeptViewModel> neuesteRezepte() {
        List<RezeptViewModel> rezepteViewModel = new ArrayList<>();
        List<Rezept> rezepte = rezeptService.getAll();
        for (Rezept rezept : rezepte) {
            RezeptViewModel rezeptViewModel = new RezeptViewModel();
            rezeptViewModel.setAutor(rezept.getAutor());
            rezeptViewModel.setBeschreibung(rezept.getAnleitung());
            String erstelltAm = sdf.format(rezept.getErstelltAm().getTime());
            rezeptViewModel.setErstelltAm(erstelltAm);
            rezeptViewModel.setId(rezept.getId());
            rezeptViewModel.setName(rezept.getTitel());
            rezepteViewModel.add(rezeptViewModel);
            rezeptViewModel.setRandomBild(rezept.getBilder().get(0));
        }
        return rezepteViewModel;
    }

}
