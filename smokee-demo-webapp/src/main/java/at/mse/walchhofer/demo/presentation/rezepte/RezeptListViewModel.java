package at.mse.walchhofer.demo.presentation.rezepte;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;


@Named("rezeptList")
@RequestScoped
public class RezeptListViewModel implements Serializable {

    private static final long serialVersionUID = 680322268132733780L;

    private List<RezeptViewModel> rezepte;

    public List<RezeptViewModel> getRezepte() {
        return rezepte;
    }

    public void setRezepte(List<RezeptViewModel> rezepte) {
        this.rezepte = rezepte;
    }
    
}
