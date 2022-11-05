-module(hello_server).
-export([init/0]).

init() ->
    ServerMailBox = "HelloMailBox",
    register(list_to_atom(ServerMailBox), self()),
    io:format("Registered MailBox alias: ~p for PID: ~p ~n", [ServerMailBox, self()]),
    loop().

loop() ->
    io:format("hello_server: waiting for new messages... ~n", []),
    receive
        {ClientPID, FullName} -> 
                io:format("hello_server: incoming message from ~p (~p) ~n", [FullName, ClientPID]), 
                ClientPID ! {ok, "Hello " ++ FullName ++ ". Nice Job!. "},
                loop();
        T ->
            io:format("hello_server: loop - No handler defined for message: ~p ~n", [T]),
            loop()
    end.
