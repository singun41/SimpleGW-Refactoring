window.addEventListener('DOMContentLoaded', event => {
    document.getElementById('btnSave').setAttribute('onclick', 'updateNotice()');
    getOptions();
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function updateNotice() {
    let docsId = await updateBoard('notice');
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        location.href = '/page/notice/' + docsId;
    }
}
