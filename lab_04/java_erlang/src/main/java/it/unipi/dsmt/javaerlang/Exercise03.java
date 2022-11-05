package it.unipi.dsmt.javaerlang;

import com.ericsson.otp.erlang.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Exercise03
{
    public static void main( String[] args ) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your fullname: ");
        String fullname = scanner.nextLine();
        String serverMailBox = "HelloMailBox";
        String serverName = "hello_server@127.0.0.1";
        String cookie = "hellocookie";

        String clientNode = "hello_client@127.0.0.1";

        OtpNode node = new OtpNode(clientNode, cookie);
        OtpMbox clientMailBox = node.createMbox();

        OtpErlangObject[] content = new OtpErlangObject[2];
        content[0] = clientMailBox.self();
        content[1] = new OtpErlangString(fullname);
        OtpErlangTuple message = new OtpErlangTuple(content);

        System.out.println("Sending message to the server");
        clientMailBox.send(serverMailBox, serverName, message);

        try {
            OtpErlangObject o = clientMailBox.receive();
            if (o instanceof OtpErlangTuple) {
                OtpErlangTuple msg = (OtpErlangTuple)o;
                OtpErlangAtom atom = (OtpErlangAtom)(msg.elementAt(0));
                OtpErlangString response = (OtpErlangString)(msg.elementAt(1));
                System.out.println("Atom: " + atom.toString());
                System.out.println("Response: " + response.toString());
            }
        } catch (OtpErlangDecodeException e) {
            throw new RuntimeException(e);
        } catch (OtpErlangExit e) {
            throw new RuntimeException(e);
        }
    }
}
