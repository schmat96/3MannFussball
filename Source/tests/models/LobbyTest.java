package models;

import static org.junit.Assert.*;

import java.util.Hashtable;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class LobbyTest {

	@Test
	public void testAddGame() {
		Lobby lobby = new Lobby();
		Game game1 = new Game(1, lobby);
		Game game2 = new Game(1, lobby);
		lobby.addGame(game1);
		try {
			Game game3 = new Game(2, null);
			Assert.fail("Es sollte nicht zulässig sein, ein Spiel ohne Lobby zu erstellen");
		} catch (Exception e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testAddGame': " + e.getMessage());
		}
		assertTrue(lobby.getGames().containsValue(game1));
		// spiel wird überschrieben
		lobby.addGame(game2);
		assertFalse(lobby.getGames().containsValue(game1));
		assertTrue(lobby.getGames().containsValue(game2));
	}

	@Test
	public void testAddPlayer() {
		Lobby lobby = new Lobby();
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(1, "Elias");
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		assertFalse(lobby.getPlayers().containsValue(player1));
		assertTrue(lobby.getPlayers().containsValue(player2));

		try {
			lobby.addPlayer(null);
			Assert.fail("Es sollte nicht zulässig sein, einen null-Spieler zu erstellen");
		} catch (Exception e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testAddPlayer': " + e.getMessage());
		}

		Player player3 = new Player(3, "Peter");
		Player player4 = new Player(4, "Peter");

		try {
			lobby.addPlayer(player3);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		try {
			lobby.addPlayer(player4);
			Assert.fail("This testcase should throw an Exception");
		} catch (Exception e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testAddPlayer': " + e.getMessage());
		}
	}

	@Test
	public void testRemovePlayer() {
		Lobby lobby = new Lobby();
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		assertFalse(lobby.getPlayers().contains(player3));
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		assertTrue(lobby.getPlayers().containsValue(player3));
		try {
			lobby.removePlayer(-12);
			Assert.fail("Es sollte nicht zulässig sein, ein Spieler, der nicht existiert zu entfernen");
		} catch (Exception e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testRemovePlayer': " + e.getMessage());
		}
		try {
			lobby.removePlayer(5);
			Assert.fail("Es sollte nicht zulässig sein, ein Spieler, der nicht existiert zu entfernen");
		} catch (Exception e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testRemovePlayer': " + e.getMessage());
		}
		lobby.removePlayer(4);
		assertTrue(lobby.getPlayers().contains(player3));
		lobby.removePlayer(3);
		assertFalse(lobby.getPlayers().containsValue(player3));
	}

	@Test
	public void testUpdatePlayerName() {
		Lobby lobby = new Lobby();
		Player player = new Player(321, "Abdelhakim");
		try {
			lobby.addPlayer(player);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}
		lobby.updatePlayerName(322, "Abi");
		try {
			lobby.updatePlayerName(321, null);
			Assert.fail("Es sollte nicht zulässig sein, ein leeres String Objekt als Namen zu wählen");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testUpdatePlayerName': " + e.getMessage());
		}
		lobby.updatePlayerName(321, "Abi");
		assertTrue(player.getPlayerName().equals("Abi"));
	}

	@Test
	public void testUpdatePlayer() {
		Lobby lobby = new Lobby();
		Player player = new Player(3241, "Sarah Arnold");
		try {
			lobby.addPlayer(player);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}
		
		assertFalse(lobby.getPlayerNamefromID(3241).equals("Sarah Arnold"));
		assertTrue(lobby.getPlayerNamefromID(3241).equals("SarahArnold"));
		
		String newName = "Sarah";
		try {
			lobby.updatePlayer(3241, newName, 45, 4);
			Assert.fail(
					"Es sollte nicht zulässig sein, als letztes Argument(Readyflag) einen Wert ausser 0 und 1 zu waehlen");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testUpdatePlayer': " + e.getMessage());
		}
		try {
			lobby.updatePlayer(3241, null, 45, 1);
			Assert.fail("Es sollte nicht zulässig sein, ein leeres String Objekt als Namen zu wählen");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testUpdatePlayer': " + e.getMessage());
		}

		assertFalse(player.isReadyflag());
		
		try {
			lobby.updatePlayer(3241, newName, 45, 1);
		} catch (IllegalArgumentException e) {
			Assert.fail("This testcase should work!");
		}
		
		assertTrue(player.isReadyflag());
	}

	@Test
	public void testPlayerIDExists() {
		Lobby lobby = new Lobby();
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}
		assertFalse(lobby.playerIDExists(0));
		assertTrue(lobby.playerIDExists(2));
	}

	@Test
	public void testRemoveGame() {
		Lobby lobby = new Lobby();
		Game game1 = new Game(1, lobby);
		Game game2 = new Game(2, lobby);
		Game game3 = new Game(3, lobby);
		Game game4 = new Game(4, lobby);
		assertFalse(lobby.getGames().containsKey(2));
		lobby.addGame(game1);
		lobby.addGame(game2);
		assertTrue(lobby.getGames().containsKey(2));
		lobby.addGame(game3);
		lobby.addGame(game4);
		lobby.removeGame(2);
		try {
			lobby.removeGame(5);
			Assert.fail("Es sollte nicht zulässig sein, einSpiel aus der Lobby zu entfernen, das nicht existiert");
		} catch (IllegalArgumentException e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testRemoveGame': " + e.getMessage());
		}
		assertFalse(lobby.getGames().containsKey(2));
		assertTrue(lobby.getGames().containsKey(3));
	}

	@Test
	public void testClearAll() {
		Lobby lobby = new Lobby();
		Game game1 = new Game(1, lobby);
		Game game2 = new Game(2, lobby);
		Game game3 = new Game(3, lobby);
		Game game4 = new Game(4, lobby);
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}
		lobby.addGame(game1);
		lobby.addGame(game2);
		lobby.addGame(game3);
		lobby.addGame(game4);
		lobby.clearAll();
		assertTrue(lobby.getGames().size() == 0 && lobby.getPlayers().size() == 0);
	}

	@Test
	public void testGetGames() {
		Lobby lobby = new Lobby();
		Game game1 = new Game(1, lobby);
		Game game2 = new Game(2, lobby);
		Game game3 = new Game(3, lobby);
		Game game4 = new Game(4, lobby);
		lobby.addGame(game1);
		lobby.addGame(game2);
		lobby.addGame(game3);
		lobby.addGame(game4);
		Hashtable<Integer, Game> gamesTable = new Hashtable<Integer, Game>();
		gamesTable.put(1, game1);
		gamesTable.put(2, game2);
		gamesTable.put(3, game3);
		int fehler1 = 0;
		for (Map.Entry<Integer, Game> htEntries : gamesTable.entrySet())
			if (lobby.getGames().containsKey(htEntries.getKey())
					&& lobby.getGames().get(htEntries.getKey()) != (htEntries.getValue()))
				fehler1++;
		assertTrue(fehler1 == 0);
		gamesTable.put(4, game4);
		int fehler2 = 0;
		for (Map.Entry<Integer, Game> htEntries : gamesTable.entrySet())
			if (lobby.getGames().containsKey(htEntries.getKey())
					&& lobby.getGames().get(htEntries.getKey()) != (htEntries.getValue()))
				fehler2++;

		assertTrue(fehler2 == 0);
	}

	@Test
	public void testGetPlayers() {
		Lobby lobby = new Lobby();
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		assertTrue(lobby.getPlayers() == lobby.getPlayers());
	}

	@Test
	public void testGetPlayerIDfromName() {
		Lobby lobby = new Lobby();
		int PlayerID = lobby.getPlayerIDfromName("Michael");

		assertTrue(PlayerID == -1);
	}

	@Test
	public void testGetPlayerNamefromID() {
		Lobby lobby = new Lobby();
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		String s = lobby.getPlayerNamefromID(3);

		assertTrue(s.equals("Sepp"));
	}

	@Test
	public void testPlayerInGame() {
		Lobby lobby = new Lobby();
		Game game = new Game(1, lobby);
		Player player1 = new Player(1, "Hans");
		Player player2 = new Player(2, "Felix");
		Player player3 = new Player(3, "Sepp");
		Player player4 = new Player(4, "Jürg");
		lobby.addGame(game);
		try {
			lobby.addPlayer(player1);
			lobby.addPlayer(player2);
			lobby.addPlayer(player3);
			lobby.addPlayer(player4);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}
		
		try {
			lobby.updatePlayer(3, "Sepp", 1, 1);
		} catch (Exception e) {
		}
		int GameID = lobby.playerInGame(3);

		assertTrue(GameID == 1);
	}

	@Test
	public void testPlayerJoinedGame() {
		Lobby lobby = new Lobby();
		Game game = new Game(53, lobby);
		Player player34 = new Player(34, "Hans");

		lobby.addGame(game);
		try {
			lobby.addPlayer(player34);
		}  catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		lobby.playerJoinedGame(34, 61);

		assertTrue(player34.getCurrentGameID() == 61);
	}

	@Test
	public void testPlayerLeftGame() {
		Lobby lobby = new Lobby();
		Game game = new Game(1, lobby);
		Player player1 = new Player(1, "Mathias");

		lobby.addGame(game);
		try {
			lobby.addPlayer(player1);
		} catch (Exception e) {
			Assert.fail("This testcase should work!");
		}

		lobby.playerLeftGame(1);

		assertFalse(player1.getCurrentGameID() == 1);
	}

	@Test
	public void testAddOldGame() {
		Lobby lobby = new Lobby();

		/*
		 * Füge zwei fiktive alte Spiele zum "oldGames"-Array hinzu
		 */
		String[] playernames1 = { "Mathias", "Andrea", "Maximilian" };
		int[] playergoals1 = { 1, 5, 3 };
		String dates1 = "irgendeinDatum";

		String[] playernames2 = { "Hans", "Ernst", "Sepp" };
		int[] playergoals2 = { 0, 0, 0 };
		String dates2 = "2000v.Chr";

		lobby.addOldGame(playernames1, playergoals1, dates1);
		lobby.addOldGame(playernames2, playergoals2, dates2);

		OldGame og1 = new OldGame(playernames1, playergoals1, dates1, 1);
		OldGame og2 = new OldGame(playernames2, playergoals2, dates2, 2);
		// System.out.println(lobby.getAllOldArrays());
		assertTrue(lobby.getOldgames().get(1).getResults().equals(og1.getResults())
				&& lobby.getOldgames().get(2).getResults().equals(og2.getResults()));

		try {
			String asdf = lobby.getAllOldArrays()[18];
			Assert.fail("Es sollte nicht zulässig sein, auf ein nicht existierenden Teil des Arrays zuzugreifen");
		} catch (ArrayIndexOutOfBoundsException e) {
			assertNotNull(e);
			System.out.println("Fehler abgefangen in 'testRemoveGame': " + e.getMessage());
		}

		assertTrue(lobby.getAllOldArrays()[4].equals("0") && lobby.getAllOldArrays()[13].equals("irgendeinDatum"));
	}

}
