import java.util.Scanner;

import com.printer.obse.ObservableI;

public class Server
{
    public static void main(String[] args)
    {
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "properties.cfg"))
        {
            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("services");
            com.zeroc.Ice.Object object = new PrinterI();
            adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SimplePrinter"));

            ObservableI subject = new ObservableI();
            adapter.add(subject, com.zeroc.Ice.Util.stringToIdentity("Subject"));
            adapter.activate();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the name of the observers <-> message: ");
            String n = scanner.nextLine();
            while (!n.equals("exit")) {
                String ms[] = n.split( "-");
                subject.notifyObservers(ms[0], ms[1]);
                n = scanner.nextLine();
            }


            communicator.waitForShutdown();
        }
    }
}