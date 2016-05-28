import java.util.ArrayList;

public class Game {
	int players, survivalsCount;
	int[] lifePoints;
	MultiArray[] gameArrays;
	final double epsilon = 1e-22;
	public Game(int[] players, int survivalsCount) {
		boolean liveError = false;
		for(int i = 0; i < players.length; i++)
			if(players[i] < 1)
				liveError = true;
		if(players.length == 0 || survivalsCount <= 0 || survivalsCount >= players.length || liveError)
			try {
				throw new Exception("Game can\'t be initialized with such parameters!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		gameArrays = new MultiArray[players.length + 1];
		gameArrays[players.length] = new MultiArray(players);
		lifePoints = players.clone();
		try {
			Analyze(0, players, players.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean IsFinished(int[] coordinates) 
	{
		int survivals = 0; 
		for(int i = 0; i < coordinates.length; i++)
		{
			if(coordinates[i] > 0)
				survivals++;
		}
		if(survivals == survivalsCount)
			return true;
		else if(survivals == 0)
			try {
				throw new Exception("Number of survivals is less that it can be!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return false;
	}
	
	void Analyze(int activePlayer, int[] coordinate, int playersInGame) throws Exception
	{
		int[] prevCoordinate = coordinate;
		int prevPlayersInGame = playersInGame;
		if(gameArrays[playersInGame].Existing(coordinate))
			return;
		if(playersInGame == 2)
		{
			if(coordinate[0] > coordinate[1] || (coordinate[0] == coordinate[1] && activePlayer == 0))
				gameArrays[playersInGame].At(coordinate).Profit = new double[]{1, 0};
			else 
				gameArrays[playersInGame].At(coordinate).Profit = new double[]{0, 1};
			return;
		}
		if(IsFinished(coordinate))
		{
			for(int i = 0; i < playersInGame; i++)
				gameArrays[playersInGame].At(coordinate).Profit[i] = coordinate[i] == 0 ? 0 : 1;
			return;
		}
		int deadPlayer = playersInGame + 1;
		int prevActivePlayer = activePlayer;
		if(coordinate.length > playersInGame)
		{
			coordinate = new int[playersInGame];
			int j = 0, a = 0;
			int axisPlayer = 0;			
			for(int i = 0; i < prevCoordinate.length; i++)
				if(prevCoordinate[i] > 0)
				{
					coordinate[j] = prevCoordinate[i];
					axisPlayer += coordinate[j];
					j++;
				}
				else
				{
					deadPlayer = i;
					if(activePlayer > i)
						a++;
				}
			playersInGame = coordinate.length;
			axisPlayer %= playersInGame;
			//activePlayer -= a;
			int diff = axisPlayer - (activePlayer - a);
			int[] c = coordinate.clone();
			for(int i = 0; i < c.length; i++)
			{
				coordinate[i] = c[(i - diff) % c.length];
			}
			activePlayer = axisPlayer;
		}
		//boolean useCubeNew = DeathCount(coordinate) > 0 ? false : true;
		int[] target = new int[playersInGame - 1];
		//boolean[] available = new boolean[playersInGame - 1];  
		double bestResult = 0;
		ArrayList<Integer> bestChoice = new ArrayList<Integer>();
		for(int s = 0; s < playersInGame - 1; s++)
		{
			target[s] = (activePlayer + s + 1) % playersInGame;
			coordinate[target[s]]--;
			int nextTurn = activePlayer;
			do
			{
				nextTurn = (nextTurn + 1) % playersInGame;
			} while(coordinate[nextTurn] == 0);
			Analyze(nextTurn, coordinate, playersInGame);
			double result = gameArrays[playersInGame].At(coordinate).Profit[activePlayer];
			if(bestResult + epsilon < result)
			{
				bestResult = result;
				bestChoice.clear();
				bestChoice.add(s);
			}
			else if ((result < bestResult + epsilon) && (result > bestResult - epsilon))
			{
				bestChoice.add(s);
			}
		
			coordinate[target[s]]++;
		}
		double p = 1.0 / (double)bestChoice.size();
		double[] finalResult = new double[prevPlayersInGame];
		for(int i = 0; i < bestChoice.size(); i++)
		{
			coordinate[target[i]]--;
			for(int j = 0; j < prevPlayersInGame; j++)
				finalResult[j] += p * gameArrays[playersInGame].At(coordinate).Profit[j];
			coordinate[target[i]]++;
		}			

		return;		
	}
}
