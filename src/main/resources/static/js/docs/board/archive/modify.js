window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'updateArchive()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function updateArchive() {
    let docsId = await updateBoard('archive');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/archive/' + docsId;
    }
}
