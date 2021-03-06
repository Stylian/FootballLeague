package gr.manolis.stelios.footie.core.peristence.dtos.games;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gr.manolis.stelios.footie.core.peristence.dtos.Team;
import gr.manolis.stelios.footie.core.peristence.dtos.matchups.Matchup;

@Entity
@DiscriminatorValue(value = "MG")
public class MatchupGame extends Game {

	@ManyToOne(cascade = CascadeType.ALL)
	private Matchup matchup;

	public MatchupGame() {
	}

	public MatchupGame(Team homeTeam, Team awayTeam, int day, Matchup matchup) {
		super(homeTeam, awayTeam, day);
		this.matchup = matchup;
	}

	@JsonIgnore
	public Matchup getMatchup() {
		return matchup;
	}

}
