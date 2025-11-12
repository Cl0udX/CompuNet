import com.printer.obse.ObserverImpl;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.ObjectPrx;

import Demo.Observer;
import Demo.ObserverPrx;
import Demo.PrinterPrx;
import Demo.SubjectPrx;

public class Client
{
    public static void main(String[] args)
    {
        String name = "pepito";
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "properties.cfg"))
        {
            Observer observer = new ObserverImpl();
            ObjectAdapter adapter = communicator.createObjectAdapter("Observer");
            ObjectPrx proxyObs = adapter.add(observer, com.zeroc.Ice.Util.stringToIdentity("notNecessaryName"));
            adapter.activate();
            ObserverPrx observerPrx = ObserverPrx.checkedCast(proxyObs);
            
            PrinterPrx printer = PrinterPrx.checkedCast(communicator.propertyToProxy("proxy"));
            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }else
            {
                printer.printString("Se conecta el observer: "+name);
            }

            SubjectPrx subject = SubjectPrx.checkedCast(communicator.propertyToProxy("subject.proxy"));
            if(subject == null)
            {
                throw new Error("Invalid proxy");
            }
            subject.registerObserver(name,observerPrx);

            communicator.waitForShutdown();
        }
    }
}