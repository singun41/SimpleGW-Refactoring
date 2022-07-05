document.addEventListener('DOMContentLoaded', () => {
    flatpickr('.input-date', {
        enableTime: false,
        dateFormat: 'Y. m. d.',
        'locale': 'ko'
    });

    flatpickr('.input-time', {
        enableTime: true,
        noCalendar: true,
        dateFormat: 'H : i',
        time_24hr: true,
        'locale': 'ko'
    });
});

async function create() {
    if(!confirm('등록하시겠습니까?'))
        return;
    
    let params = {
        alarmDate: document.getElementById('date').value.replaceAll('. ', '-').replaceAll('.', ''),
        alarmTime: document.getElementById('time').value.replace(' : ', ':'),
        title: document.getElementById('title').value,
        remarks: (document.getElementById('remarks').value === '' ? null : document.getElementById('remarks').value)
    };

    let response = await fetchPostParams('alarm', params);
    let result = await response.json();
    alert(result.msg);
    if(response.ok) {
        if(opener)
            opener.afterCUD();
        window.close();
    }
}
