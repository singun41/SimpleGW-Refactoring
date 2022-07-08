window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'saveNotice()');
});

async function saveNotice() {
    let docsId = await saveBoard();
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        location.href = docsId;
    }
}
