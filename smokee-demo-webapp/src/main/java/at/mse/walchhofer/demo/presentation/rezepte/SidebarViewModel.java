package at.mse.walchhofer.demo.presentation.rezepte;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("sideBar")
@RequestScoped
public class SidebarViewModel {

    private List<RezeptViewModel> neueRezepte;

    @Inject
    RezeptListController rezeptListController;

    @PostConstruct
    public void init() {
        setNeueRezepte(rezeptListController.neuesteRezepte());
    }

    public List<RezeptViewModel> getNeueRezepte() {
        return neueRezepte;
    }

    public void setNeueRezepte(List<RezeptViewModel> neueRezepte) {
        this.neueRezepte = neueRezepte;
    }

}
