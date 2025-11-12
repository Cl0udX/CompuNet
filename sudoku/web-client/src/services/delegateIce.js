import Subscriber from './subscriber.js';

export class DelegateIce {

    usuario;

    constructor(usuario) {
        this.communicator = Ice.initialize();
        this.usuario = usuario;
    }

    async init() {
        if (this.pri) {
            return;
        }
        const host = 'localhost';
        const port = 25565;
        const objectProxy = this.communicator.stringToProxy(`SimplePrinter:ws -h ${host} -p ${port}`);
        this.pri = await Demo.PrinterPrx.checkedCast(objectProxy);

        const subjectPrx = this.communicator.stringToProxy(`Subject:ws -h ${host} -p ${port}`);
        this.subject = await Demo.SubjectPrx.checkedCast(subjectPrx);

        const adapter = await this.communicator.createObjectAdapter("");
        const conn = this.subject.ice_getCachedConnection();
        conn.setAdapter(adapter);

        const callbackPrx = Demo.ObserverPrx.uncheckedCast(adapter.addWithUUID(Subscriber));

        await this.subject.registerObserver(this.usuario, callbackPrx);
        this.startPingLoop();
    }

    async printMessage(message) {
        if (!this.pri) {
            await this.init();
        }
        this.pri.printString(message);
    }

    async startCount() {
        if (!this.subject) {
            await this.init();
        }
        this.subject.startCounting(this.usuario);
    }

    async stopCount() {
        if (!this.subject) {
            await this.init();
        }
        this.subject.stopCounting(this.usuario);
    }

    // set onCountUpdate(fn) {
    //     Subscriber.onCountUpdateInternal = fn;
    // }
    set onCountUpdate(fn) {
        Subscriber.addListener(fn);
    }

    startPingLoop() {
        if (this.pingInterval) return;
        this.pingInterval = setInterval(() => {
            this.ping();
        }, 20000); // cada 20 segundos
    }

    async ping() {
        if (this.subject) {
            await this.subject.update('ping');
        }
    }
}
export default DelegateIce;