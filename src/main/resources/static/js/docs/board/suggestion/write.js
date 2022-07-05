window.addEventListener('DOMContentLoaded', event => {
    copySuggestion();
    document.getElementById('btnTemp').setAttribute('onclick', 'saveTempSuggestion()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveSuggestion()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveSuggestion() {
    let docsId = await saveBoard('suggestion');
    if(docsId) {
        saveComplete = true;
        location.href = docsId;
    }
}

async function saveTempSuggestion() {
    let docsId = await saveTempBoard('suggestion');
    if(docsId) {
        saveComplete = true;
        location.href = '/page/suggestion/temp/' + docsId;
    }
}

function copySuggestion() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
