package org.sid.cinema.web;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.sid.ciname.entities.Film;
import org.sid.ciname.entities.Ticket;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
@RestController
public class CinemaRestController {
	@Autowired
	private FilmRepository filmRespository;
	@Autowired
	private TicketRepository ticketRespository;
	
	@GetMapping("/listFilms")
	public List<Film> getFilms(){
		return filmRespository.findAll();
	}
	@GetMapping(path="/image/Film/{id}", produces=MediaType.IMAGE_JPEG_VALUE)
	public byte[] image(@PathVariable (name="id")Long id)throws Exception{
		Film f = filmRespository.findById(id).get();
		String photoName = f.getPhoto();
		File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
		Path path = Paths.get(file.toURI());
		return Files.readAllBytes(path);
	}
	@PostMapping("/payerTickets")
	public List<Ticket> payerTickets(@RequestBody TicketForm ticketFrom){
		List<Ticket> listTickets = new ArrayList<>();
		ticketFrom.getTickets().forEach(idTicket->{
			Ticket ticket = ticketRespository.findById(idTicket).get();
			ticket.setNomClient(ticketFrom.getNomClient());
			ticket.setReserve(true);
			ticketRespository.save(ticket);
			listTickets.add(ticket);
			
		});
		return listTickets;
	}
}

@Data
class TicketForm{
	private String nomClient;
	private List<Long> tickets = new ArrayList<>();
}
