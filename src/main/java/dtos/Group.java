package main.java.dtos;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity(name = "GROUPS")
public class Group {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Team> teams;

	@ManyToMany(cascade = CascadeType.ALL)
	private Map<Team, Stats> teamsStats;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Game> games;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}

	public Map<Team, Stats> getTeamsStats() {
		return teamsStats;
	}

	public void setTeamsStats(Map<Team, Stats> teamsStats) {
		this.teamsStats = teamsStats;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", teams=" + teams + ", teamsStats=" + teamsStats + ", games=" + games + "]";
	}

}
