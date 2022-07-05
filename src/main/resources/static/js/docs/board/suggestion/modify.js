window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'updateSuggestion()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function updateSuggestion() {
    let docsId = await updateBoard('suggestion');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/suggestion/' + docsId;
    }
}
