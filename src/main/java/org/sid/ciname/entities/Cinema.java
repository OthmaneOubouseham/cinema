package org.sid.ciname.entities;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString // pour gérer les geter et seter
public class Cinema implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double longitude,latitude,altitude;
	private int nombreSalles;
	@OneToMany(mappedBy="cinema")// la relation entre cinema et Salle 
	private Collection<Salle> salles;
	@ManyToOne
	private Ville ville;
	
	
}
