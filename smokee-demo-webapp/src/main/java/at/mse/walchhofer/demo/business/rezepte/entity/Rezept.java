package at.mse.walchhofer.demo.business.rezepte.entity;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import at.mse.walchhofer.demo.business.bilder.entity.Bild;

@Entity
@NamedQueries({
    @NamedQuery(name="Rezept.findAll",
            query="SELECT r FROM Rezept r")
})
public class Rezept {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String titel;
    
    @NotNull
    private String anleitung;
    
    @NotNull
    private String autor;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar erstelltAm;
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="rezept") 
    private List<Bild> bilder;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAnleitung() {
        return anleitung;
    }

    public void setAnleitung(String anleitung) {
        this.anleitung = anleitung;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Bild> getBilder() {
        return bilder;
    }

    public void setBilder(List<Bild> bilder) {
        this.bilder = bilder;
    }

    public Calendar getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(Calendar erstelltAm) {
        this.erstelltAm = erstelltAm;
    }
    
}
