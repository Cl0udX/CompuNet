function formatTime(ms) {
    const totalSeconds = Math.floor(ms / 1000);
    const hours = Math.floor(totalSeconds / 3600);
    const minutes = Math.floor((totalSeconds % 3600) / 60);
    const seconds = totalSeconds % 60;
    const milliseconds = ms % 1000;
    return `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}.${String(milliseconds).padStart(3, '0')}`;
}

function Counter({ delegateIce }) {
    const container = document.createElement('div');
    container.className = 'counterContainer';

    const valueSpan = document.createElement('span');
    valueSpan.textContent = formatTime(0);

    const startBtn = document.createElement('button');
    startBtn.textContent = 'Iniciar';
    startBtn.onclick = () => delegateIce.startCount();

    const stopBtn = document.createElement('button');
    stopBtn.textContent = 'Detener';
    stopBtn.onclick = () => delegateIce.stopCount();

    container.appendChild(valueSpan);
    container.appendChild(startBtn);
    container.appendChild(stopBtn);

    delegateIce.onCountUpdate = (count) => {
        valueSpan.textContent = formatTime(count);
    };

    return container;
}

export default Counter;