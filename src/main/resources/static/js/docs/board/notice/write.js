window.addEventListener('DOMContentLoaded', event => {
    copyNotice();
    document.getElementById('btnTemp').setAttribute('onclick', 'saveTempNotice()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveNotice()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveNotice() {
    let docsId = await saveBoard('notice');
    if(docsId) {
        saveComplete = true;
        await saveOptions(docsId);
        location.href = docsId;
    }
}

async function saveTempNotice() {
    let docsId = await saveTempBoard('notice');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/notice/temp/' + docsId;
    }
}

function copyNotice() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
