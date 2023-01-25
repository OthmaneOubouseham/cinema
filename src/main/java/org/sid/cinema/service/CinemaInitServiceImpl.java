package org.sid.cinema.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.sid.ciname.entities.Categorie;
import org.sid.ciname.entities.Cinema;
import org.sid.ciname.entities.Film;
import org.sid.ciname.entities.Place;
import org.sid.ciname.entities.Projection;
import org.sid.ciname.entities.Salle;
import org.sid.ciname.entities.Seance;
import org.sid.ciname.entities.Ticket;
import org.sid.ciname.entities.Ville;
import org.sid.cinema.dao.VilleRepository;
import org.sid.cinema.dao.CategorieRepository;
import org.sid.cinema.dao.CinemaRepository;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.PlaceRepository;
import org.sid.cinema.dao.ProjectionRepository;
import org.sid.cinema.dao.SalleRepository;
import org.sid.cinema.dao.SeanceRepository;
import org.sid.cinema.dao.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CinemaInitServiceImpl implements ICinemaInitService {
	@Autowired
	private VilleRepository villeRepository;
	@Autowired
	private CinemaRepository cinemaRepository;
	@Autowired
	private SalleRepository salleRepository;
	@Autowired
	private PlaceRepository placeRepository;
	@Autowired
	private SeanceRepository seanceRepository;
	@Autowired
	private FilmRepository filmRepository;
	@Autowired
	private ProjectionRepository projectionRepository;
	@Autowired
	private CategorieRepository categorieRepository;
	@Autowired
	private TicketRepository ticketRepository;
	
	
	
	@Override
	@Transactional
	public void initVilles() {
		Stream.of("Casablanca","Marrackech", "Rabat","Tanger").forEach(v ->{
			Ville ville = new Ville();
			ville.setNom(v);
			villeRepository.save(ville);
			
		});
	}

	@Override
	@Transactional
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MegaRama", "IMAX", "FOUNOUN")
			.forEach(nameCinema->{
				Cinema cinema = new Cinema();
				cinema.setName(nameCinema);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	@Transactional
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0; i<cinema.getNombreSalles(); i++){
				Salle salle= new Salle();
				salle.setName("Salle"+(i+1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	@Transactional
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNombrePlace();i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	@Transactional
	public void initSeances() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
			Seance seance = new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			}catch(ParseException e) {
				e.printStackTrace();
			}
		});
		
		
	}

	@Override
	@Transactional
	public void initCategories() {
		Stream.of("Histoire", "Action", "Fiction", "Drama").forEach(cat ->{
			Categorie categorie = new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	@Transactional
	public void initFilms() {
		double[] durees  = new double[] {1,1.5,2,2.5,3};
		List<Categorie> categories = categorieRepository.findAll();
		Stream.of("12 Homes en colaire", "Forrset Gump", "Green Book", "La ligne Vert", "Cat Women").forEach(titreFilm->{
			Film film = new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll("", ""));
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
		
	}

	@Override
	@Transactional
	public void initProjection() {
		double [] prices = new double[] {30, 50, 60, 70, 90, 100};
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema ->{
				cinema.getSalles().forEach(salle ->{
					filmRepository.findAll().forEach(film ->{
						seanceRepository.findAll().forEach(seance->{
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
							
						});
					});
				});
			});
		});
	}

	@Override
	@Transactional
	public void initTickets() {
		projectionRepository.findAll().forEach(projection->{
			projection.getSalle().getPlaces().forEach(place->{
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(projection.getPrix());
				ticket.setProjection(projection);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
				
			});
		});
	}

}
