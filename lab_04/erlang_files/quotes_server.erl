-module(quotes_server).
-export([init/0]).

init() ->
    ServerMailBox = "QuotesMailBox",
    register(list_to_atom(ServerMailBox), self()),
    io:format("Registered MailBox alias: ~p for PID: ~p ~n", [ServerMailBox, self()]),
    QuotesFileName = "quotes.txt",
    Quotes = readlines(QuotesFileName),
    io:format("Loaded quotes: ~p ~n", [Quotes]),
    loop(Quotes).

readlines(FileName) ->
    {ok, Device} = file:open(FileName, [read]),
    try get_all_lines(Device, [])
      after file:close(Device)
    end.

get_all_lines(Device, Quotes) ->
    case io:get_line(Device, "") of
        eof  -> Quotes;
        Line -> get_all_lines(Device, Quotes ++ [string:trim(Line)])
    end.

loop(Quotes) ->
    io:format("quotes_server: waiting for new messages... ~n", []),
    receive
        {ClientPID, FirstName, LastName} -> 
                io:format("quotes_server: incoming message from ~p ~p (~p) ~n", [FirstName, LastName, ClientPID]), 
                RandomIndex = rand:uniform(length(Quotes)),
                RandomQuote = lists:nth(RandomIndex, Quotes),
                ClientPID ! {ok, "Hello " ++ FirstName ++ " " ++ LastName ++ ", this quote if for you: " ++ RandomQuote},
                loop(Quotes);
        T ->
            io:format("quotes_server: loop - No handler defined for message: ~p ~n", [T]),
            loop(Quotes)
    end.
