class Subscriber extends Demo.Observer {
    listeners = [];

    update(msg) {
        console.log("Mesaje de server: ", msg)
    }

    // updateCount(count) {
    //     if (typeof this.onCountUpdateInternal === 'function') {
    //         this.onCountUpdateInternal(count);
    //     }
    // }
    updateCount(count) {
        this.listeners.forEach(fn => fn(count));
    }

    addListener(fn) {
        this.listeners.push(fn);
    }

    removeListener(fn) {
        this.listeners = this.listeners.filter(f => f !== fn);
    }

}

export default new Subscriber();