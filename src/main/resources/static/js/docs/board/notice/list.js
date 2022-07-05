document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('btnSearch').setAttribute('onclick', 'search("notice")');
    search('notice');

    setTitleIcon('fa-solid fa-clipboard-list');
});
