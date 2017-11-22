package core.peristence.dtos.rounds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import core.peristence.dtos.Team;
import core.peristence.dtos.groups.RobinGroup;
import core.peristence.dtos.groups.Season;

@Entity
@DiscriminatorValue(value = "G")
public class GroupsRound extends Round {

	@OneToMany(fetch = FetchType.LAZY)
	private List<RobinGroup> groups = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GROUPS_STRONG_TEAMS")
	private List<Team> strongTeams;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GROUPS_MEDIUM_TEAMS")
	private List<Team> mediumTeams;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GROUPS_WEAK_TEAMS")
	private List<Team> weakTeams;

	public GroupsRound() {
	}

	public GroupsRound(Season season, String name) {
		super(season, name);
	}

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public List<Team> getStrongTeams() {
		return strongTeams;
	}

	public void setStrongTeams(List<Team> strongTeams) {
		this.strongTeams = strongTeams;
	}

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public List<Team> getMediumTeams() {
		return mediumTeams;
	}

	public void setMediumTeams(List<Team> mediumTeams) {
		this.mediumTeams = mediumTeams;
	}

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public List<Team> getWeakTeams() {
		return weakTeams;
	}

	public void setWeakTeams(List<Team> weakTeams) {
		this.weakTeams = weakTeams;
	}

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public List<RobinGroup> getGroups() {
		return groups;
	}

	public void addGroup(RobinGroup group) {
		this.groups.add(group);
	}

}
