package main.java.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import main.java.DataAccessObject;
import main.java.PropertyUtils;
import main.java.Utils;
import main.java.dtos.Game;
import main.java.dtos.Matchup;
import main.java.dtos.Team;
import main.java.dtos.groups.Group;
import main.java.dtos.groups.Season;
import main.java.dtos.rounds.GroupsRound;
import main.java.dtos.rounds.QualsRound;

public class SeasonService {

	final static Logger logger = Logger.getLogger(SeasonService.class);

	private Session session;

	public SeasonService(Session session) {
		this.session = session;
	}

	public void createSeason() {

		Properties properties = PropertyUtils.load();
		String strSeasonNum = properties.getProperty("season");

		int year = Integer.parseInt(strSeasonNum) + 1;
		properties.setProperty("season", Integer.toString(year));
		PropertyUtils.save(properties);

		logger.info("creating season " + year);

		Season season = new Season(year);

		List<Team> teams = ServiceUtils.loadTeams(session);

		for (Team team : teams) {

			season.addTeam(team);

		}

		DataAccessObject<Season> dao = new DataAccessObject<>(session);
		dao.save(season);

	}

	public void setUpSeason() {

		List<Team> teams = ServiceUtils.loadTeams(session);

		Season season = ServiceUtils.loadCurrentSeason(session);

		QualsRound qualsRound1 = new QualsRound(season, "1st Qualifying Round");
		QualsRound qualsRound2 = new QualsRound(season, "2nd Qualifying Round");
		GroupsRound groupsRound12 = new GroupsRound(season, "Groups Round of 12");

		if(season.getSeasonYear() == 1) {
			
			// 2nd round needs 24 teams so
			int diff = teams.size() - 24;
			
			Collections.shuffle(teams);

			List<Team> quals1 = new ArrayList<>();
			for(int index = 0; index < 2*diff; index++) {
				quals1.add(teams.remove(0));
			}
			
			logger.info("unseeded teams to 1st quals round: " + Utils.toString(quals1));
			qualsRound1.setTeams(quals1);
			
			logger.info("unseeded teams to 2nd quals round: " + Utils.toString(teams));
			qualsRound2.setTeams(teams);
			
			logger.info("1st season no teams go directly to groups");
			groupsRound12.setTeams(new ArrayList<>());
			
		}else { // needs more work
			
			// former champion promotes directly , to add later to groups round
			Team formerChampion = teams.remove(0); // TODO
			
			// top 3 seeded teams promote directly excluding champion, to add later to
			// groups round
			List<Team> top3Seeders = new ArrayList<>();
			top3Seeders.add(teams.remove(0)); // TODO
			top3Seeders.add(teams.remove(0));
			top3Seeders.add(teams.remove(0));
			
			// 2nd round needs 16 teams so
			int diff = teams.size() - 16;
			
			// so bottom 2*diff go to 1st quals, others directly to 2nd quals
			List<Team> quals1 = new ArrayList<>();
			for(int index = 0; index < 2*diff; index++) {
				quals1.add(teams.remove(0)); // TODO
			}
			
			// remaining go to 2nd quals
			List<Team> quals2 = teams;
			
			for(Team t : quals1)
				System.out.println(t);
			
			System.out.println("---------------");
			
			for(Team t : quals2)
				System.out.println(t);
			
			System.out.println("---------------");
			
			for(Team t : top3Seeders)
				System.out.println(t);
			
			System.out.println("---------------");
			System.out.println(formerChampion);
			
		}
		
		
		DataAccessObject<Season> seasonDao = new DataAccessObject<>(session);
		seasonDao.save(season);

	}
	
	// TODO to move somewhere
	int PROMOTION_POINTS_QUALS_1 = 1000;
	int PROMOTION_POINTS_QUALS_2 = 2000;
	int WIN_POINTS = 1000;
	int DRAW_POINTS = 500;
	int GOALS_POINTS = 100;
	
	public void endCurrentSeason() {
		logger.info("closing down season, calculating coefficients");
		// maybe set up winner later
		
		Group master = ServiceUtils.getMasterGroup(session);
		Season season = ServiceUtils.loadCurrentSeason(session);
		
		QualsRound roundQuals1 = (QualsRound) season.getRounds().get(0);
		
		// add coeffs to quals1 winners
		for(Matchup matchup : roundQuals1.getMatchups()) {
			
			matchup.getWinner().getStatsForGroup(master).addPoints(PROMOTION_POINTS_QUALS_1);
			matchup.getWinner().getStatsForGroup(season).addPoints(PROMOTION_POINTS_QUALS_1);
			
			// average out points per matchup
			addGamePointsForMatchup(master, season, matchup);
			
		}
		
		QualsRound roundQuals2 = (QualsRound) season.getRounds().get(1);
		
		// add coeffs to quals2 winners
		for(Matchup matchup : roundQuals2.getMatchups()) {
			
			matchup.getWinner().getStatsForGroup(master).addPoints(PROMOTION_POINTS_QUALS_2);
			matchup.getWinner().getStatsForGroup(season).addPoints(PROMOTION_POINTS_QUALS_2);
			
			// average out points per matchup
			addGamePointsForMatchup(master, season, matchup);
			
		}
		
		// TODO for more rounds
		
		
		// add points for goals scored
		List<Team> teams = ServiceUtils.loadTeams(session);
		
		for(Team team : teams) {
			
			int goalsScored = team.getStatsForGroup(season).getGoalsScored();
			team.getStatsForGroup(season).addPoints(goalsScored * GOALS_POINTS);
			
		}
		
		// hope it is enough, seems so
		DataAccessObject<Season> seasonDao = new DataAccessObject<>(session);
		seasonDao.save(season);
		
	}

	private void addGamePointsForMatchup(Group master, Season season, Matchup matchup) {
		int matchPointsHome = 0;
		int matchPointsAway = 0;
		int numberOfGames = 0;
		
		for(Game game : matchup.getGames()) {
		
			numberOfGames ++;
			
			if(game.getResult().homeTeamWon()) {
				
				if(game.getHomeTeam().equals(matchup.getTeamHome())) {
					matchPointsHome += WIN_POINTS;
				}else {
					matchPointsAway += WIN_POINTS;
				}
				
			}else if(game.getResult().tie()) {
				
				if(game.getHomeTeam().equals(matchup.getTeamHome())) {
					matchPointsHome += DRAW_POINTS;
				}else {
					matchPointsAway += DRAW_POINTS;
				}
				
			}
			
		}
		
		matchup.getTeamHome().getStatsForGroup(master).addPoints(2 * matchPointsHome / numberOfGames);
		matchup.getTeamHome().getStatsForGroup(season).addPoints(2 * matchPointsHome / numberOfGames);
		
		matchup.getTeamAway().getStatsForGroup(master).addPoints(2 * matchPointsAway / numberOfGames);
		matchup.getTeamAway().getStatsForGroup(season).addPoints(2 * matchPointsAway / numberOfGames);
	}

}
