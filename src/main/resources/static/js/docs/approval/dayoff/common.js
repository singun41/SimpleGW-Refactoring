document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    datePickerInit();
});
const rowGroup = document.getElementById('rowGroup');
const defaultRow = document.getElementById('defaultRow').cloneNode(true);

function datePickerInit() {
    let pickr = document.getElementsByClassName('input-date');
    flatpickr(pickr[pickr.length - 1], {
        mode: "range",
        enableTime: false,
        dateFormat: 'Y. m. d.',
        defaultDate: [ dayjs().format('YYYY. MM. DD.'), dayjs().format('YYYY. MM. DD.') ],
        'locale': 'ko'
    });
}

function addRow() {
    rowGroup.append( defaultRow.cloneNode(true) );
    datePickerInit();
}

function deleteRow() {
    if(document.getElementsByClassName('data-row').length === 1)
        return;

    if(rowGroup.hasChildNodes())
        rowGroup.removeChild(rowGroup.lastChild);
}
