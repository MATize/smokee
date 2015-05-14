package at.mse.walchhofer.demo.presentation.rezepte;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Named("rezept")
@RequestScoped
public class RezeptViewModel implements Serializable {

    private static final long serialVersionUID = 680322268132733780L;

    private Long id;
    
    @NotNull
    @Size(min=3,max=200)
    private String name;
    
    @NotNull
    @Size(min=10,max=1024)
    private String beschreibung;

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
