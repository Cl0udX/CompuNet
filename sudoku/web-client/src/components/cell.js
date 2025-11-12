const createCell = (cellData) => {
    const { valueCurrent, isEditable, id, onSetValue, ...rest } = cellData;

    const cell = document.createElement('div');
    cell.classList.add('box');
    cell.tabIndex = 0;

    const inputElement = document.createElement('input');
    inputElement.type = 'text';
    inputElement.value = valueCurrent || '';
    inputElement.maxLength = 2;
    inputElement.classList.add('sudokuInput');

    const spanElement = document.createElement('span');
    spanElement.classList.add('sudokuSpan');

    spanElement.textContent = valueCurrent || '';

    if (!isEditable) {
        inputElement.disabled = true;
        spanElement.style.color = 'black';
    }

    cell.onclick = (e) => {
        if (!isEditable) return;
        cell.style.outline = '2px solid blue';
        inputElement.style.pointerEvents = 'auto';
        inputElement.style.opacity = '1';
        inputElement.focus();
    };

    inputElement.oninput = (e) => {
        const val = e.target.value.replace(/[^1-9]/g, '').slice(-1);
        inputElement.value = val;
        spanElement.textContent = val;
        if (onSetValue) onSetValue({ val, id });
    };

    inputElement.onblur = (e) => {
        cell.style.outline = 'none';
        inputElement.style.pointerEvents = 'none';
        inputElement.style.opacity = '0';
    };

    cell.appendChild(inputElement);
    cell.appendChild(spanElement);
    cell.id = id;
    return cell;
}

export default createCell;
