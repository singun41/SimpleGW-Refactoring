document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnSearch').setAttribute('onclick', 'search("freeboard")');
    search('freeboard');

    setTitleIcon('fa-solid fa-message');
});
