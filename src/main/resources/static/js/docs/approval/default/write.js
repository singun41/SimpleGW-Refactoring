window.addEventListener('DOMContentLoaded', event => {
    copyDefaultReport();
    document.getElementById('btnTemp').setAttribute('onclick', 'saveTempDefaultReport()');
    document.getElementById('btnSave').setAttribute('onclick', 'saveDefaultReport()');
});

let saveComplete = false;
window.addEventListener('beforeunload', event => {
    // 페이지를 나갈 때
    event.preventDefault();
    if(CKEDITOR.instances.ckeditorTextarea.getData() !== '' && !saveComplete) {
        event.returnValue = '';
    }
});

async function saveDefaultReport() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };
    let docsId = await saveApprovalDocs('default', params);
    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/default/' + docsId;
    }
}

async function saveTempDefaultReport() {
    let params = {
        content: CKEDITOR.instances.ckeditorTextarea.getData()
    };
    let docsId = await saveTempApprovalDocs(params, type);
    if(docsId) {
        saveComplete = true;
        location.href = '/page/approval/default/temp/' + docsId;
    }
}

function copyDefaultReport() {
    let docs = JSON.parse(localStorage.getItem('docs'));
    if(docs) {
        document.getElementById('title').value = docs.title;
        CKEDITOR.instances.ckeditorTextarea.setData(docs.content);
        localStorage.removeItem('docs');
    }
}
