package org.GameLogic.Util;

/**
 * Class hosting all message codes for responses or requests
 */

public final class MessageCodes {

	/*
	 * Offical codes
	 */

	public final static int CONNECTREQUEST = 100;
	public final static int GAMELISTREQUEST = 300;
	public final static int GAMEJOINREQUEST = 302;
	public final static int SPECTATORJOINREQUEST = 304;
	public final static int MESSAGESEND = 306;
	public final static int LEAVINGREQUEST = 405;
	public final static int TILESWAPREQUEST = 411;
	public final static int PLAYTILES = 414;
	public final static int SCOREREQUEST = 417;
	public final static int TURNTIMELEFTREQUEST = 419;
	public final static int TOTALTIMEREQUEST = 421;
	public final static int BAGREQUEST = 423;
	public final static int PLAYERHANDSREQUEST = 425;
	public final static int GAMEDATAREQUEST = 498;
	public final static int CONNECTACCEPTED = 101;
	public final static int GAMELISTRESPONSE = 301;
	public final static int GAMEJOINACCEPTED = 303;
	public final static int SPECTATORJOINACCEPTED = 305;
	public final static int MESSAGESIGNAL = 307;
	public final static int STARTGAME = 400;
	public final static int ENDGAME = 401;
	public final static int ABORTGAME = 402;
	public final static int PAUSEGAME = 403;
	public final static int RESUMEGAME = 404;
	public final static int LEAVINGPLAYER = 406;
	public final static int WINNER = 407;
	public final static int STARTTILES = 408;
	public final static int CURRENTPLAYER = 409;
	public final static int SENDTILES = 410;
	public final static int TILESWAPVALID = 412;
	public final static int TILESWAPRESPONSE = 413;
	public final static int MOVEVALID = 415;
	public final static int UPDATE = 416;
	public final static int SCORERESPONSE = 418;
	public final static int TURNTIMELEFTRESPONSE = 420;
	public final static int TOTALTIMERESPONSE = 422;
	public final static int BAGRESPONSE = 424;
	public final static int PLAYERHANDSRESPONSE = 426;
	public final static int GAMEDATARESPONSE = 499;
	public final static int ACCESSDENIED = 900;
	public final static int PARSINGERROR = 910;
	public final static int NOTALLOWED = 920;

	/*
	 * own codes
	 */

	public final static int CLIENTCONNECT = 1000;

	public final static int GAMESTART = 1400;
	public final static int GAMEABORT = 1402;
	public final static int GAMEPAUSED = 1403;
	public final static int GAMERESUME = 1404;

	public final static int DISCONNECT = 1100;
	public final static int FINISHGAME = 2110;
	public final static int CLIENTJOINGAME = 2120;
	public final static int NEWGAME = 2130;
	public final static int NEWTOURNAMENT = 2140;

	public final static int CHANGEGAMESTATE = 3110;
	public final static int SHUTDOWN = 3120;
	public final static int MOVETOGAME = 3130;
	public final static int FINISHTOURNAMENT = 3140;
	
	public final static int GAMESTARTERROR = 3990;
	public final static int GAMEABORTERROR = 3991;
	public final static int GAMEPAUSEDERROR = 3992;
	public final static int GAMERESUMEERROR = 3993;
}
