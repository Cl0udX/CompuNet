module Demo
{
    interface Printer
    {
        void printString(string s);
    }

// Client
    interface Observer
    {
        void update(string s);
        void updateCount(int count);
    }

// Server
    interface Subject
    {
        void registerObserver(string name, Observer* o);
        void removeObserver(string name);

        void startCounting(string name);
        void stopCounting(string name);

        void update(string s);
    }
}