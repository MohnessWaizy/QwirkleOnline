package de.upb.swtpra1819interface.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.upb.swtpra1819interface.messages.AbortGame;
import de.upb.swtpra1819interface.messages.AccessDenied;
import de.upb.swtpra1819interface.messages.BagRequest;
import de.upb.swtpra1819interface.messages.BagResponse;
import de.upb.swtpra1819interface.messages.ConnectAccepted;
import de.upb.swtpra1819interface.messages.ConnectRequest;
import de.upb.swtpra1819interface.messages.CurrentPlayer;
import de.upb.swtpra1819interface.messages.EndGame;
import de.upb.swtpra1819interface.messages.GameDataRequest;
import de.upb.swtpra1819interface.messages.GameDataResponse;
import de.upb.swtpra1819interface.messages.GameJoinAccepted;
import de.upb.swtpra1819interface.messages.GameJoinRequest;
import de.upb.swtpra1819interface.messages.GameListRequest;
import de.upb.swtpra1819interface.messages.GameListResponse;
import de.upb.swtpra1819interface.messages.LeavingPlayer;
import de.upb.swtpra1819interface.messages.LeavingRequest;
import de.upb.swtpra1819interface.messages.Message;
import de.upb.swtpra1819interface.messages.MessageSend;
import de.upb.swtpra1819interface.messages.MessageSignal;
import de.upb.swtpra1819interface.messages.MoveValid;
import de.upb.swtpra1819interface.messages.NotAllowed;
import de.upb.swtpra1819interface.messages.ParsingError;
import de.upb.swtpra1819interface.messages.PauseGame;
import de.upb.swtpra1819interface.messages.PlayTiles;
import de.upb.swtpra1819interface.messages.PlayerHandsRequest;
import de.upb.swtpra1819interface.messages.PlayerHandsResponse;
import de.upb.swtpra1819interface.messages.ResumeGame;
import de.upb.swtpra1819interface.messages.ScoreRequest;
import de.upb.swtpra1819interface.messages.ScoreResponse;
import de.upb.swtpra1819interface.messages.SendTiles;
import de.upb.swtpra1819interface.messages.SpectatorJoinAccepted;
import de.upb.swtpra1819interface.messages.SpectatorJoinRequest;
import de.upb.swtpra1819interface.messages.StartGame;
import de.upb.swtpra1819interface.messages.StartTiles;
import de.upb.swtpra1819interface.messages.TileSwapRequest;
import de.upb.swtpra1819interface.messages.TileSwapResponse;
import de.upb.swtpra1819interface.messages.TileSwapValid;
import de.upb.swtpra1819interface.messages.TotalTimeRequest;
import de.upb.swtpra1819interface.messages.TotalTimeResponse;
import de.upb.swtpra1819interface.messages.TurnTimeLeftRequest;
import de.upb.swtpra1819interface.messages.TurnTimeLeftResponse;
import de.upb.swtpra1819interface.messages.Update;
import de.upb.swtpra1819interface.messages.Winner;
import de.upb.swtpra1819interface.models.Configuration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Parser {
	private Gson gson;
	private Gson gsonPp;

	public Parser() {
		GsonBuilder builder = new GsonBuilder();
		builder.enableComplexMapKeySerialization();
		gson = builder.create();
		builder.setPrettyPrinting();
		gsonPp = builder.create();
	}

	public String serialize(Message message) {
		return gson.toJson(message);
	}

	public Message deserialize(String jsonString) throws ParsingException {
		JsonParser jp = new JsonParser();
		JsonElement jsonTree = jp.parse(jsonString);
		JsonObject jsonObject = jsonTree.getAsJsonObject();
		int id = 0;
		try {
			id = jsonObject.get("uniqueId").getAsInt();
		} catch (NullPointerException e) {
			throw new ParsingException("Given jsonString has no uniqueID", e);
		}
		Message message = null;
		switch (id) {
		case 100:
			message = (Message) gson.fromJson(jsonTree, ConnectRequest.class);
			break;
		case 300:
			message = (Message) gson.fromJson(jsonTree, GameListRequest.class);
			break;
		case 302:
			message = (Message) gson.fromJson(jsonTree, GameJoinRequest.class);
			break;
		case 304:
			message = (Message) gson.fromJson(jsonTree, SpectatorJoinRequest.class);
			break;
		case 306:
			message = (Message) gson.fromJson(jsonTree, MessageSend.class);
			break;
		case 405:
			message = (Message) gson.fromJson(jsonTree, LeavingRequest.class);
			break;
		case 411:
			message = (Message) gson.fromJson(jsonTree, TileSwapRequest.class);
			break;
		case 414:
			message = (Message) gson.fromJson(jsonTree, PlayTiles.class);
			break;
		case 417:
			message = (Message) gson.fromJson(jsonTree, ScoreRequest.class);
			break;
		case 419:
			message = (Message) gson.fromJson(jsonTree, TurnTimeLeftRequest.class);
			break;
		case 421:
			message = (Message) gson.fromJson(jsonTree, TotalTimeRequest.class);
			break;
		case 423:
			message = (Message) gson.fromJson(jsonTree, BagRequest.class);
			break;
		case 425:
			message = (Message) gson.fromJson(jsonTree, PlayerHandsRequest.class);
			break;
		case 498:
			message = (Message) gson.fromJson(jsonTree, GameDataRequest.class);
			break;

		case 101:
			message = (Message) gson.fromJson(jsonTree, ConnectAccepted.class);
			break;
		case 301:
			message = (Message) gson.fromJson(jsonTree, GameListResponse.class);
			break;
		case 303:
			message = (Message) gson.fromJson(jsonTree, GameJoinAccepted.class);
			break;
		case 305:
			message = (Message) gson.fromJson(jsonTree, SpectatorJoinAccepted.class);
			break;
		case 307:
			message = (Message) gson.fromJson(jsonTree, MessageSignal.class);
			break;
		case 400:
			message = (Message) gson.fromJson(jsonTree, StartGame.class);
			break;
		case 401:
			message = (Message) gson.fromJson(jsonTree, EndGame.class);
			break;
		case 402:
			message = (Message) gson.fromJson(jsonTree, AbortGame.class);
			break;
		case 403:
			message = (Message) gson.fromJson(jsonTree, PauseGame.class);
			break;
		case 404:
			message = (Message) gson.fromJson(jsonTree, ResumeGame.class);
			break;
		case 406:
			message = (Message) gson.fromJson(jsonTree, LeavingPlayer.class);
			break;
		case 407:
			message = (Message) gson.fromJson(jsonTree, Winner.class);
			break;
		case 408:
			message = (Message) gson.fromJson(jsonTree, StartTiles.class);
			break;
		case 409:
			message = (Message) gson.fromJson(jsonTree, CurrentPlayer.class);
			break;
		case 410:
			message = (Message) gson.fromJson(jsonTree, SendTiles.class);
			break;
		case 412:
			message = (Message) gson.fromJson(jsonTree, TileSwapValid.class);
			break;
		case 413:
			message = (Message) gson.fromJson(jsonTree, TileSwapResponse.class);
			break;
		case 415:
			message = (Message) gson.fromJson(jsonTree, MoveValid.class);
			break;
		case 416:
			message = (Message) gson.fromJson(jsonTree, Update.class);
			break;
		case 418:
			message = (Message) gson.fromJson(jsonTree, ScoreResponse.class);
			break;
		case 420:
			message = (Message) gson.fromJson(jsonTree, TurnTimeLeftResponse.class);
			break;
		case 422:
			message = (Message) gson.fromJson(jsonTree, TotalTimeResponse.class);
			break;
		case 424:
			message = (Message) gson.fromJson(jsonTree, BagResponse.class);
			break;
		case 426:
			message = (Message) gson.fromJson(jsonTree, PlayerHandsResponse.class);
			break;
		case 499:
			message = (Message) gson.fromJson(jsonTree, GameDataResponse.class);
			break;
		case 900:
			message = (Message) gson.fromJson(jsonTree, AccessDenied.class);
			break;
		case 910:
			message = (Message) gson.fromJson(jsonTree, ParsingError.class);
			break;
		case 920:
			message = (Message) gson.fromJson(jsonTree, NotAllowed.class);
			break;
		}

		if (message == null) {
			throw new ParsingException("Could not parse message");
		}
		return message;
	}

	public Configuration loadConfig(String filePath) throws ParsingException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			StringBuilder stringBuilder = new StringBuilder();
			String line = bufferedReader.readLine();
			while (line != null) {
				stringBuilder.append(line);
				stringBuilder.append(System.lineSeparator());
				line = bufferedReader.readLine();
			}
			String jsonString = stringBuilder.toString();
			Configuration configuration = (Configuration) gsonPp.fromJson(jsonString, Configuration.class);
			if (configuration == null) {
				throw new ParsingException("Could not parse file to Configuration");
			}
			return configuration;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void saveConfig(Configuration config, String filePath) {
		if (filePath.contains(File.separator)) {
			String[] directories = filePath.split(File.separator);
			for (int i = 0; i < directories.length - 1; i++) {
				StringBuilder currentPath = new StringBuilder();
				for (int j = 0; j < i; j++) {
					currentPath.append(directories[j] + File.separator);
				}
				String path = currentPath.toString();
				File file = new File(path + directories[i]);
				file.mkdir();
			}
		}
		String jsonString = gsonPp.toJson(config);
		File file = new File(filePath);
		try {
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(jsonString);
			fileWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
