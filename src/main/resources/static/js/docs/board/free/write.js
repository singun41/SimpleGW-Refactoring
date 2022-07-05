window.addEventListener('DOMContentLoaded', event => {
    copyFreeboard();
    document.getElementById('btnTemp').setAttribute('onclick', 'saveTempFreeboard()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveFreeboard()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveFreeboard() {
    let docsId = await saveBoard('freeboard');
    if(docsId) {
        saveComplete = true;
        location.href = docsId;
    }
}

async function saveTempFreeboard() {
    let docsId = await saveTempBoard('freeboard');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/freeboard/temp/' + docsId;
    }
}

function copyFreeboard() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
