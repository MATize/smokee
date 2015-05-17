package at.mse.walchhofer.demo.business.bilder.entity;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import at.mse.walchhofer.demo.business.rezepte.entity.Rezept;

@Entity
@NamedQueries({
        @NamedQuery(name = "Bild.findAll",
                query = "SELECT b FROM Bild b")
})
public class Bild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rezept_id")
    private Rezept rezept;

    @NotNull
    private String mimeType;

    @NotNull
    private Long size;

    @NotNull
    private String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar erstelltAm;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Calendar getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(Calendar erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public Rezept getRezept() {
        return rezept;
    }

    public void setRezept(Rezept rezept) {
        this.rezept = rezept;
    }

}
