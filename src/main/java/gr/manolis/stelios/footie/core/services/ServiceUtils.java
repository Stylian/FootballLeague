package gr.manolis.stelios.footie.core.services;

import gr.manolis.stelios.footie.core.Utils;
import gr.manolis.stelios.footie.core.peristence.DataAccessObject;
import gr.manolis.stelios.footie.core.peristence.dtos.League;
import gr.manolis.stelios.footie.core.peristence.dtos.Team;
import gr.manolis.stelios.footie.core.peristence.dtos.groups.RobinGroup;
import gr.manolis.stelios.footie.core.peristence.dtos.groups.Season;
import gr.manolis.stelios.footie.core.peristence.dtos.rounds.GroupsRound;
import gr.manolis.stelios.footie.core.peristence.dtos.rounds.QualsRound;
import gr.manolis.stelios.footie.core.peristence.dtos.rounds.Round;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class ServiceUtils {

	@Autowired
	private SessionFactory sessionFactory;

	public boolean testDbConnection() {

		final boolean[] works = new boolean[1];

		Session session = sessionFactory.getCurrentSession();
		session.doWork( (c) -> works[0] = true );

		return works[0];
	}


	public League getLeague() {

		DataAccessObject<League> groupDao = new DataAccessObject<>(sessionFactory.getCurrentSession());

		List<League> ls = groupDao.list("LEAGUES");

		return ls.size() > 0 ? groupDao.list("LEAGUES").get(0) : null;

	}

	public Season loadCurrentSeason() {

		League league = getLeague();

		DataAccessObject<Season> dao = new DataAccessObject<>(sessionFactory.getCurrentSession());

		return dao.listByField("GROUPS", "SEASON_YEAR", "" + league.getSeasonNum()).get(0);

	}

	public Season loadSeason(int year) {

		return (Season) sessionFactory.getCurrentSession()
				.createQuery("from GROUPS where discriminator='S' and SEASON_YEAR=" + year).uniqueResult();

	}

	public int getNumberOfSeasons() {

		return getLeague().getSeasonNum();

	}
	
	@SuppressWarnings("unchecked")
	public List<Season> loadAllSeasons() {

		return sessionFactory.getCurrentSession().createQuery("from GROUPS where discriminator='S' ").list();

	}
	@SuppressWarnings("unchecked")
	public int getCoefficientsUntilSeason(Team team, int seasonUntil) {
		return Utils.getCoefficientsUntilSeason(loadAllSeasons(), team, seasonUntil);
	}

	public List<Team> loadTeams() {

		DataAccessObject<Team> dao = new DataAccessObject<>(sessionFactory.getCurrentSession());
		return dao.list("TEAMS");

	}

	public RobinGroup loadRobinGroup(int id) {

		return (RobinGroup) sessionFactory.getCurrentSession()
				.createQuery("from GROUPS where ID=" + id).uniqueResult();

	}

	public QualsRound getQualRound(Season season, int round) {

		List<Round> rounds = season.getRounds();
		return (QualsRound) rounds.get(round);

	}
	
	public GroupsRound getGroupsRound(Season season, int round) {
		
		List<Round> rounds = season.getRounds();
		return (GroupsRound) rounds.get(round + 2);
		
	}

}
