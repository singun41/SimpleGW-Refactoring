window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'updateNotice()');
    getOptions();
});


async function updateNotice() {
    let docsId = await updateBoard();
    
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        location.href = '/page/' + docsType.toLowerCase() + '/' + docsId;
    }
}
