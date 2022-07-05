document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnSearch').setAttribute('onclick', 'search("archive")');

    setTitleIcon('fa-solid fa-box-archive');

    flatpickr(searchDtp, {
        mode: "range",
        enableTime: false,
        dateFormat: 'Y. m. d.',
        defaultDate: [ dayjs('2000. 01. 01.').format('YYYY. MM. DD.'), dayjs().format('YYYY. MM. DD.') ],
        'locale': 'ko'
    });

    search('archive');
});
