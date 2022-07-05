document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnSearch').setAttribute('onclick', 'search("suggestion")');
    search('suggestion');

    setTitleIcon('fa-solid fa-chalkboard-user');
});
