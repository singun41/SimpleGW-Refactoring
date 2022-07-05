window.addEventListener('DOMContentLoaded', event => {
    copyArchive();
    document.getElementById('btnTemp').classList.add('d-none');   // 자료실은 임시저장 기능을 제공하지 않는다.
    document.getElementById('btnSave').setAttribute('onclick', 'saveArchive()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveArchive() {
    let docsId = await saveBoard('archive');
    if(docsId) {
        saveComplete = true;
        location.href = docsId;
    }
}

function copyArchive() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
