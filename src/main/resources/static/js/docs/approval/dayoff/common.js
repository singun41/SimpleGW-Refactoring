document.addEventListener('DOMContentLoaded', () => {
    dayjs.locale('ko');
    datePickerInit();
});
const rowGroup = document.getElementById('rowGroup');
const defaultRow = document.getElementsByClassName('data-row')[0].cloneNode(true);

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


// 아래 두 function은 write, modify 페이지에서 사용.
function setDatePicker(from, to) {
    let pickr = document.getElementsByClassName('input-date');
    flatpickr(pickr[pickr.length - 1], {
        mode: "range",
        enableTime: false,
        dateFormat: 'Y. m. d.',
        defaultDate: [ dayjs(from).format('YYYY. MM. DD.'), dayjs(to).format('YYYY. MM. DD.') ],
        'locale': 'ko'
    });
}

async function getDetails(docsId) {
    let response = await fetchGet(`approval/dayoff/details/${docsId}`);
    let result = await response.json();
    let details = Array.from(result.obj);
    
    for(let i=0; i<details.length; i++) {
        document.getElementsByClassName('dayoff-code')[i].value = details[i].code;
        setDatePicker(details[i].dateFrom, details[i].dateTo);

        if(i < details.length - 1)   // 기본 1행은 있으므로
            rowGroup.append( defaultRow.cloneNode(true) );
    }
}
