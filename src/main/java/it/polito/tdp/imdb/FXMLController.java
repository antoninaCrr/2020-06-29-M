/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.Neighbour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.boxRegista.getItems().clear();
    	this.txtResult.clear();
    	
    	Integer anno = this.boxAnno.getValue();
    	
    	if(anno == null) {
    		this.txtResult.appendText("Devi selezionare un anno\n");
    		return;
    	}
    	
    	this.model.creaGrafo(anno);
    	
    	this.txtResult.appendText("GRAFO CREATO\n");
    	this.txtResult.appendText("#VERTICI: "+this.model.nVertici()+"\n");
    	this.txtResult.appendText("#ARCHI: "+this.model.nArchi()+"\n");
    	
    	this.boxRegista.getItems().addAll(this.model.getAllVertices());
    
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Director d = this.boxRegista.getValue();
    	
    	if(d == null) {
    		this.txtResult.appendText("Devi selezionare un regista\n");
    		return;
    	}
    	
    	List<Neighbour> vicini = new ArrayList<Neighbour>(this.model.getAllNeighbours(d));
    	
    	this.txtResult.appendText("REGISTI ADIACENTI A: "+d.getId()+"-"+d.getFirstName()+" "+d.getLastName()+"\n");
    	
    	for(Neighbour ni : vicini) {
    		this.txtResult.appendText(ni.toString()+"\n");
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	String num = this.txtAttoriCondivisi.getText();
    	int soglia;
   
    	try {
    		soglia = Integer.parseInt(num);
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Inserire un valore soglia numerico\n");
    		return;
    	}
    	
        Director d = this.boxRegista.getValue();
    	
    	if(d == null) {
    		this.txtResult.appendText("Devi selezionare un regista\n");
    		return;
    	}
    	
    	
    	List<Director> direttoriCercati = model.startRicorsione(d, soglia);
    	
    	for(Director di : direttoriCercati) {
    		this.txtResult.appendText(di.toString()+"\n");
    	}
    	this.txtResult.appendText("Num tot attori condivisi: "+model.sommaPeso(direttoriCercati));
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    	for(int anno = 2004; anno <= 2006; anno++) {
    		this.boxAnno.getItems().add(anno);
    	}
    	
    }
    
}
