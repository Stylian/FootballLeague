package service.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import service.peristence.dtos.Stats;
import service.peristence.dtos.Team;
import service.peristence.dtos.groups.Group;
import service.tools.Ordering;

public class GroupService {

	final static Logger logger = Logger.getLogger(GroupService.class);

	private Session session;

	public GroupService(Session session) {
		this.session = session;
	}

	public List<Team> getTeams(Group group) {
		return getTeams(group, null);
	}

	public List<Team> getTeams(Group group, Ordering orderingStrategy) {

		Map<Team, Stats> teamsWithStats = group.getTeamsStats();

		List<Team> teams = new ArrayList<>();

		for (Team team : teamsWithStats.keySet()) {
			teams.add(team);
		}

		if (orderingStrategy != null) {
			Collections.sort(teams, orderingStrategy);
		}

		return teams;

	}

}