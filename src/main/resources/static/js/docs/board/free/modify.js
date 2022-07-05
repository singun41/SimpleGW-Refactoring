window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'updateFreeboard()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function updateFreeboard() {
    let docsId = await updateBoard('freeboard');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/freeboard/' + docsId;
    }
}
